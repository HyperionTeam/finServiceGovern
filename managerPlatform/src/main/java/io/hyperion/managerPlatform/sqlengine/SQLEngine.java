package io.hyperion.managerPlatform.sqlengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SQLEngine {
	
	public static final String INC = "inc";
	public static final String SET = "set";

	public static final String COMMAND = "command";
	public static final String KEYOFKNOWLEDGE = "keyOfKnowledge";
	public static final String VALUE = "value";

	public static Select parse(String sql) {
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			return (Select) statement;
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean checkCompare(Double a, Double b, Expression rExpression) {
		if (rExpression instanceof EqualsTo) {
			return (a.equals(b));
		} else if (rExpression instanceof NotEqualsTo) {
			return (!a.equals(b));
		} else if (rExpression instanceof GreaterThanEquals) {
			return (a >= b);
		} else if (rExpression instanceof GreaterThan) {
			return (a > b);
		} else if (rExpression instanceof MinorThanEquals) {
			return (a <= b);
		} else if (rExpression instanceof MinorThan) {
			return (a < b);
		} else {
			return false;
		}
	}
	
	/**
	 * 检查是否符合知识条件
	 * @param expression
	 * @param data
	 * @return
	 */
	private static boolean checkKnowledgeCondition(Expression expression, Map<String, Object> data) {
		if(expression == null) {
			return true;
		}
		Expression lExpression = null;
		Expression rExpression = null;
		if (!expression.getClass().equals(AndExpression.class)) {	// 如果左操作数已经是column，则意味着该分支是单个条件，已到尽头
			rExpression = expression;
		} else {
			AndExpression wExpression = (AndExpression) expression;
			lExpression = wExpression.getLeftExpression();
			rExpression = wExpression.getRightExpression();
			if(checkKnowledgeCondition(lExpression, data) == false) {	// 只要有一个条件不符合，就直接返回false
				return false;
			}
		}

		BinaryExpression condition = null;
		if (rExpression instanceof EqualsTo) {
			condition = (EqualsTo) rExpression;
		} else if (rExpression instanceof NotEqualsTo) {
			condition = (NotEqualsTo) rExpression;
		} else if (rExpression instanceof GreaterThanEquals) {
			condition = (GreaterThanEquals) rExpression;
		} else if (rExpression instanceof GreaterThan) {
			condition = (GreaterThan) rExpression;
		} else if (rExpression instanceof MinorThanEquals) {
			condition = (MinorThanEquals) rExpression;
		} else if (rExpression instanceof MinorThan) {
			condition = (MinorThan) rExpression;
		}
		
		Column column = (Column) condition.getLeftExpression();
		String key = column.getColumnName();
		Expression right = condition.getRightExpression();
		Object value = data.get(key);
		if(value == null) {
			return false;
		}
		if (right instanceof StringValue) {
			int compare = value.toString().compareTo(((StringValue) right).getValue());
			return checkCompare(Double.valueOf(compare), Double.valueOf(0), rExpression);
		} else if (right instanceof DoubleValue || right instanceof LongValue) {
			Double dataValue = Double.valueOf(value.toString());
			Double sqlValue = Double.valueOf(right.toString());
			return checkCompare(dataValue, sqlValue, rExpression);
		} else {
			return false;
		}
	}
	
	/**
	 * 分析数据流，检查是否符合知识条件，如果符合则产生知识数据
	 * @param sql
	 * @param data
	 * @return 返回一个map：一个key是command，表示知识数据应该inc还是set进相应的redis；一个key是value，表示更新进redis的知识数据；一个key是keyOfKnowledge，表示redis中知识数据的key。如果不符合条件就返回null
	 */
	public static Map<String, Object> createKnowledge(String sql, Map<String, Object> data) {
		
		Select select = parse(sql);
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		boolean knowledgeConditionPass = true;	//是否符合知识条件，默认为符合
		
		// 获取where条件
		if(plain.getWhere() != null) {
			Expression expression = plain.getWhere();
			knowledgeConditionPass = checkKnowledgeCondition(expression, data);
		}
		// 如果不符合知识条件
		if(knowledgeConditionPass == false) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取select元素
		List selectitems = plain.getSelectItems();
		//由于一个sql条件只对应一个知识数据，所以只支持一个select元素
		Expression selectExpressionItem = ((SelectExpressionItem) selectitems.get(0)).getExpression();
		if (selectExpressionItem instanceof Column) {	// 如果知识数据类型是field
			Column col = (Column) selectExpressionItem;
			String key = col.getColumnName();
			result.put(COMMAND, SET);
			result.put(VALUE, data.get(key));
		} else if (selectExpressionItem instanceof Function) {	// 如果知识数据类型是count()、sum(field)
			Function function = (Function) selectExpressionItem;
			String functionName = function.getName().toLowerCase();
			if(functionName.equals("count")) {
				result.put(COMMAND, INC);
				result.put(VALUE, 1);
			} else if(functionName.equals("sum")) {
				for (Expression expression : function.getParameters().getExpressions()) {
					Column col = (Column) expression;
					String key = col.getColumnName();
					result.put(COMMAND, INC);
					result.put(VALUE, data.get(key));
					break;	// 只取第一个field的值
				}
			}
		}
		
		// 获取表名
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		List<String> tableList = tablesNamesFinder.getTableList(select);
		String tableName = tableList.get(0);
		result.put(KEYOFKNOWLEDGE, tableName);
		
		return result;
	}
	
	/**
	 * 解析sql，转换成操作db（redis、mongodb）的命令：inc或set
	 * @param sql
	 * @return
	 */
	public static String sql2Command(String sql) {
		try {
			
			Select select = parse(sql);
			PlainSelect plain = (PlainSelect) select.getSelectBody();
			// 获取select元素
			List selectitems = plain.getSelectItems();
			// 由于一个sql条件只对应一个知识数据，所以只支持一个select元素
			Expression selectExpressionItem = ((SelectExpressionItem) selectitems.get(0)).getExpression();
			if (selectExpressionItem instanceof Column) { // 如果知识数据类型是field
				return SET;
			} else if (selectExpressionItem instanceof Function) { // 如果知识数据类型是count()、sum(field)
				return INC;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
