package io.hyperion.managerPlatform.dao;

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

import io.hyperion.managerPlatform.dto.AppStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;


//@Component("AppStrategyConfigDAO")
public class AppStrategyConfigDAOImpl {
	private final static String APP_STRATEGY_CONFIG_COL = "appStrategyConfig";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
	public AppStrategyConfigDTO getConfigByKey(String key) {
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, AppStrategyConfigDTO.class);
	}
	
	//@Override
	public long getAppStrategyNumber() {
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);
		return mongoTemplate.count(query, APP_STRATEGY_CONFIG_COL);
	}
	
	//@Override
	public List<AppStrategyConfigDTO> getAppStrategyByPage(int skip ,int limit) {
		Criteria criteria = new Criteria();
		Direction sortDirection = Sort.Direction.DESC;
		String sortField = "opTime";
		Aggregation aggreResult = Aggregation.newAggregation(
				Aggregation.match(criteria),
        		Aggregation.sort(sortDirection, sortField),
        		Aggregation.skip(skip),
        		Aggregation.limit(limit)
        );
          
        AggregationResults<AppStrategyConfigDTO> results = mongoTemplate.aggregate(aggreResult, 
        		APP_STRATEGY_CONFIG_COL,
        		AppStrategyConfigDTO.class);
		return results.getMappedResults();
	}
	
	//@Override
	public void insertAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO) {
		mongoTemplate.insert(appStrategyConfigDTO);
	}
	
	//@Override
	public boolean modifyAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO) {
		String key = appStrategyConfigDTO.getKey();
		Update update = new Update();
		update.set("description", appStrategyConfigDTO.getDescription());
		update.set("command", appStrategyConfigDTO.getCommand());
		update.set("opTime", appStrategyConfigDTO.getOpTime());
		Query query = new Query(Criteria.where("key").is(key));
		mongoTemplate.updateMulti(query, update, APP_STRATEGY_CONFIG_COL);
		return true;
	}
	
	//@Override
	public List<AppStrategyConfigDTO> getAllAppStrategy() {
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);
		return mongoTemplate.find(query, AppStrategyConfigDTO.class);
	}
}
