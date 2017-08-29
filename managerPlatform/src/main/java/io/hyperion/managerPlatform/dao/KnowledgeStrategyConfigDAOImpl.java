package io.hyperion.managerPlatform.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;


//@Component("KnowledgeStrategyConfigDAO")
public class KnowledgeStrategyConfigDAOImpl {
	private final static String KNOWLEDGE_STRATEGY_CONFIG_COL = "knowledgeStrategyConfig";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
	public KnowledgeStrategyConfigDTO getConfigByKey(String key) {
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeStrategyConfigDTO.class);
	}
	
	//@Override
	public KnowledgeStrategyConfigDTO getConfigByName(String name) {
		Criteria criteria = new Criteria().where("name").is(name);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeStrategyConfigDTO.class);
	}
	
	//@Override
	public void insertKnowledgeStrategy(KnowledgeStrategyConfigDTO KnowledgeStrategyConfigDTO){
		mongoTemplate.insert(KnowledgeStrategyConfigDTO);
//		this._add(KnowledgeStrategyConfigDTO);
	}
	
	//@Override
	public List<KnowledgeStrategyConfigDTO> getKnowledgeStrategyByPage(int skip ,int limit) {
		Criteria criteria = new Criteria();
		Direction sortDirection = Sort.Direction.DESC;
		String sortField = "opTime";
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.sort(sortDirection, sortField),
        		Aggregation.skip(skip),
        		Aggregation.limit(limit)
        );
          
        AggregationResults<KnowledgeStrategyConfigDTO> results = mongoTemplate.aggregate(aggreResult, 
        		KNOWLEDGE_STRATEGY_CONFIG_COL,
        		KnowledgeStrategyConfigDTO.class);
		return results.getMappedResults();
	}
	
	//@Override
	public boolean deleteKnowledgeStrategyByName(String key) {
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, KNOWLEDGE_STRATEGY_CONFIG_COL);
		return true;
//		int returnCode =  this._remove(Criteria.where("key").is(key));
//		if(returnCode == 1){
//			return true;
//		}else {
//			return false;
//		}
	}
	
	//@Override
	public boolean deleteKnowledgeStrategyByOpTime(String opTime) {
		Criteria criteria = new Criteria().where("opTime").is(opTime);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, KNOWLEDGE_STRATEGY_CONFIG_COL);
		return true;
//		int returnCode =  this._remove(Criteria.where("opTime").is(opTime));
//		if(returnCode == 1){
//			return true;
//		}else {
//			return false;
//		}
	}
	
	//@Override
	public boolean modifyKnowledgeStrategyByName(KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO){
		String key = knowledgeStrategyConfigDTO.getKey();
		Update update = new Update();
		update.set("description", knowledgeStrategyConfigDTO.getDescription());
		update.set("status", knowledgeStrategyConfigDTO.getStatus());
		update.set("expire", knowledgeStrategyConfigDTO.getExpire());
		update.set("opTime", knowledgeStrategyConfigDTO.getOpTime());
		update.set("appStrategyTriggers", knowledgeStrategyConfigDTO.getAppStrategyTriggers());
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		mongoTemplate.updateMulti(query, update, KnowledgeStrategyConfigDTO.class);
		return true;
//		return this._update(Criteria.where("key").is(key), update);
	}
	
	//@Override
	public long getKnowledgeStrategyNumber() {
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);
		return mongoTemplate.count(query, KNOWLEDGE_STRATEGY_CONFIG_COL);
	}
	
	//@Override
	public List<KnowledgeStrategyConfigDTO> getAllPersistentedStrategy(int status, int persistent, int expire) {
		Criteria criteria = Criteria.where("status").is(status).and("persistent").is(persistent).and("expire").gte(expire);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, KnowledgeStrategyConfigDTO.class);
	}
}


