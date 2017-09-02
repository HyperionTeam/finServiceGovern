package me.star2478.jstorm.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;
import me.star2478.jstorm.dto.StrategyAnalysisSummaryDTO;


@Repository
//@Component("KnowledgeStrategyStatDAO")
public class StrategyAnalysisSummaryDAOImpl implements StrategyAnalysisSummaryDAO{
	
	private final Logger logger = Logger.getLogger(getClass());
	
	private final static String STRATEGY_ANALYSIS_SUMMARY_COL = "strategyAnalysisSummary";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
//	public KnowledgeStrategyStatDTO getKnowledgeStrategyStat(String key,String field,String time) {
//		Query query = new Query(Criteria.where("key").is(key).and("field").is(field).and("time").is(time));
//		return mongoTemplate.findOne(query, KnowledgeStrategyStatDTO.class);
//	}
	
	@Override
	public List<StrategyAnalysisSummaryDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime) {
		Criteria criteria = Criteria.where("key").is(key).and("time").gte(beginTime).lte(endTime);
		if(appID != null) {
			Criteria criteria4AppID = Criteria.where("appId").is(appID);
			criteria.andOperator(criteria4AppID);
		}
		if(os != null) {
			Criteria criteria4OS = Criteria.where("os").is(os);
			criteria.andOperator(criteria4OS);
		}
		if(appVersion != null) {
			Criteria criteria4AppVersion = Criteria.where("appVersion").is(appVersion);
			criteria.andOperator(criteria4AppVersion);
		}
		if(triggerName != null) {
			Criteria criteria4TriggerName = Criteria.where("triggerName").is(triggerName);
			criteria.andOperator(criteria4TriggerName);
		}

		Direction sortDirection = Sort.Direction.DESC;
		String sortField = "time";
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.sort(sortDirection, sortField)
        );
          
        AggregationResults<StrategyAnalysisSummaryDTO> results = mongoTemplate.aggregate(aggreResult, 
        		STRATEGY_ANALYSIS_SUMMARY_COL,
        		StrategyAnalysisSummaryDTO.class);
		return results.getMappedResults();
	}
	
	@Override
	public void incStrategySummary(StrategyAnalysisSummaryDTO dto) {
		mongoTemplate.insert(dto, STRATEGY_ANALYSIS_SUMMARY_COL);
	}

	@Override
	public void updStrategySummary(StrategyAnalysisSummaryDTO dto) {
		Update update = new Update();
		update.set("sourceData", dto.getSourceData());
		update.set("dataResult", dto.getDataResult());
		update.set("dataAnalysisStatus", dto.getDataAnalysisStatus());
		update.set("governResult", dto.getGovernResult());
		update.set("governStatus", dto.getGovernStatus());
		update.set("evenList", dto.getEvenList());
	    Criteria criteria = Criteria.where("key").is(dto.getKey()).and("triggerName").is(dto.getTriggerName())
	    		.and("time").is(dto.getTime());
	    Query query = new Query(criteria);
	    mongoTemplate.updateMulti(query, update, STRATEGY_ANALYSIS_SUMMARY_COL);
	}
}
