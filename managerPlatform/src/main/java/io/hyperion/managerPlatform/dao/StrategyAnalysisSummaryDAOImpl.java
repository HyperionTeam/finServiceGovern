package io.hyperion.managerPlatform.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyStatDTO;
import io.hyperion.managerPlatform.dto.StrategyAnalysisSummaryDTO;

//@Component("KnowledgeStrategyStatDAO")
public class StrategyAnalysisSummaryDAOImpl {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	private final static String STRATEGY_ANALYSIS_SUMMARY_COL = "strategyAnalysisSummary";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
//	public KnowledgeStrategyStatDTO getKnowledgeStrategyStat(String key,String field,String time) {
//		Query query = new Query(Criteria.where("key").is(key).and("field").is(field).and("time").is(time));
//		return mongoTemplate.findOne(query, KnowledgeStrategyStatDTO.class);
//	}
	
	//@Override
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
	
	public StrategyAnalysisSummaryDTO getTheLastestSummary(String key, String triggerName) {
		Criteria criteria = Criteria.where("key").is(key).and("triggerName").is(triggerName);

		Direction sortDirection = Sort.Direction.DESC;
		String sortField = "time";
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.sort(sortDirection, sortField),
        		Aggregation.skip(0),
        		Aggregation.limit(1)
        );
          
        AggregationResults<StrategyAnalysisSummaryDTO> results = mongoTemplate.aggregate(aggreResult, 
        		STRATEGY_ANALYSIS_SUMMARY_COL,
        		StrategyAnalysisSummaryDTO.class);
        if (results.getMappedResults() != null && results.getMappedResults().size() > 0) {
        	return results.getMappedResults().get(0);
        } else {
        	return null;
        }
	}
	
}
