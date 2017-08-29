package io.hyperion.managerPlatform.vo;

import java.util.List;

public class StrategyStatVo {

	private String key;
	private List<FieldVo> fieldList;
	
	
	public List<FieldVo> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldVo> fieldList) {
		this.fieldList = fieldList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public static class FieldVo {
		private String field;
		private List<CountVo> countList;
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public List<CountVo> getCountList() {
			return countList;
		}
		public void setCountList(List<CountVo> countList) {
			this.countList = countList;
		}
		
	}
	
	public static class CountVo {
		private Object value;
		private String time;
		
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
	}
}
