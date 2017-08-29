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

//@Component("KnowledgeStrategyStatDAO")
public class KnowledgeStrategyStatDAOImpl {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	private final static String KNOWLEDGE_STRATEGY_STAT_COL = "knowledgeStrategyStat";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
	public KnowledgeStrategyStatDTO getKnowledgeStrategyStat(String key,String field,String time) {
		Query query = new Query(Criteria.where("key").is(key).and("field").is(field).and("time").is(time));
		return mongoTemplate.findOne(query, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public List<KnowledgeStrategyStatDTO> getKnowledgeStrategyStat(int skip ,int limit)  {
		Criteria criteria = new Criteria();
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.skip(skip),
        		Aggregation.limit(limit)
        );
          
        AggregationResults<KnowledgeStrategyStatDTO> results = mongoTemplate.aggregate(aggreResult, 
        		KNOWLEDGE_STRATEGY_STAT_COL,
        		KnowledgeStrategyStatDTO.class);
		return results.getMappedResults();
	}
	
	//@Override
	public void addKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO) {
		mongoTemplate.insert(knowledgeStrategyStatDTO);
	}
	
	//@Override
	public void modifyKnowledgeStrategyStatVal(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO) {
		Update update = new Update();
		update.set("value", knowledgeStrategyStatDTO.getValue());
	    Criteria criteria = Criteria.where("key").is(knowledgeStrategyStatDTO.getKey()).and("triggerName").is(knowledgeStrategyStatDTO.getTriggerName())
	    		.and("time").is(knowledgeStrategyStatDTO.getTime());
	    Query query = new Query(criteria);
	    mongoTemplate.updateMulti(query, update, KNOWLEDGE_STRATEGY_STAT_COL);
	}
	
	//@Override
	public List<KnowledgeStrategyStatDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime) {
		Criteria criteria = Criteria.where("key").is(key).and("appId").is(appID).
				and("os").is(os).and("appVersion").is(appVersion).and("triggerName").is(triggerName).
				and("time").gte(beginTime).lte(endTime);
//		return this._list(criteria);
		Query query = new Query();
		query.addCriteria(Criteria.where("key").is(key));
		if(appID != null) {
			query.addCriteria(Criteria.where("appId").is(appID));
		}
		if(os != null) {
			query.addCriteria(Criteria.where("os").is(os));
		}
		if(appVersion != null) {
			query.addCriteria(Criteria.where("appVersion").is(appVersion));
		}
		if(triggerName != null) {
			query.addCriteria(Criteria.where("triggerName").is(triggerName));
		}
		query.addCriteria(Criteria.where("time").gte(beginTime).lte(endTime));
		return mongoTemplate.find(query, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field,int skip,int limitSize) {
		
		Criteria criteria = Criteria.where("key").is(key).and("field").is(field);
		Direction sortDirection = Sort.Direction.DESC;
		String sortField = "time";
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.sort(sortDirection, sortField),
        		Aggregation.skip(skip),
        		Aggregation.limit(limitSize)
        );
          
        AggregationResults<KnowledgeStrategyStatDTO> results = mongoTemplate.aggregate(aggreResult, 
        		KNOWLEDGE_STRATEGY_STAT_COL,
        		KnowledgeStrategyStatDTO.class);
		return results.getMappedResults();
	}

	//@Override
	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field, String beginTime, String endTime) {
		Criteria criteria = Criteria.where("key").is(key).and("field").is(field).and("time").gte(beginTime).lte(endTime);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public List<KnowledgeStrategyStatDTO> getStatByKey(String key, String beginTime, String endTime) {
		Criteria criteria = Criteria.where("key").is(key).and("time").gte(beginTime).lte(endTime);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public List<KnowledgeStrategyStatDTO> getAllFieldByKey(String key) {
		Criteria criteria = Criteria.where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public void upsertIncKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value) {
		try {
			Update update = new Update();
			update.inc("value", Float.valueOf(value.toString()));
			Query q = new Query(Criteria.where("appId").is(knowledgeStrategyStatDTO.getAppId()).and("os")
					.is(knowledgeStrategyStatDTO.getOs()).and("appVersion").is(knowledgeStrategyStatDTO.getAppVersion())
					.and("key").is(knowledgeStrategyStatDTO.getKey()).and("triggerName")
					.is(knowledgeStrategyStatDTO.getTriggerName()).and("time").is(knowledgeStrategyStatDTO.getTime()));
			mongoTemplate.upsert(q, update, KnowledgeStrategyStatDTO.class);

		} catch (UncategorizedMongoDbException e) {	// 如果mongodb现在数据不是数字，就会捕获异常，改用set方式
			logger.fatal("inc mongoDB fail, because new value="+value+" is numeric type but old value of db is non-numeric type");
			upsertSetKnowledgeStrategyStat(knowledgeStrategyStatDTO, value);
		}
	}
	
	//@Override
	public void upsertSetKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value) {
		Update update = new Update();
		update.set("value", value);
		Query q = new Query(Criteria.where("appId").is(knowledgeStrategyStatDTO.getAppId()).
				and("os").is(knowledgeStrategyStatDTO.getOs()).
				and("appVersion").is(knowledgeStrategyStatDTO.getAppVersion()).
				and("key").is(knowledgeStrategyStatDTO.getKey()).
				and("triggerName").is(knowledgeStrategyStatDTO.getTriggerName()).
				and("time").is(knowledgeStrategyStatDTO.getTime()));
		mongoTemplate.upsert(q, update, KnowledgeStrategyStatDTO.class);
	}
	
	//@Override
	public void deleteKnowledgeStrategyStatByOpTime(String time) {
		Criteria criteria = new Criteria().where("time").is(time);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, KNOWLEDGE_STRATEGY_STAT_COL);
	}
}
