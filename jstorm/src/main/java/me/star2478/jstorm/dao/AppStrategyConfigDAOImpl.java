package me.star2478.jstorm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import me.star2478.jstorm.dto.AppStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO;

@Repository
public class AppStrategyConfigDAOImpl implements AppStrategyConfigDAO {
	private final static String APP_STRATEGY_CONFIG_COL = "appStrategyConfig";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@Override
	public AppStrategyConfigDTO getConfigByKey(String key) {

		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, AppStrategyConfigDTO.class);
	}
	
	@Override
	public List<AppStrategyConfigDTO> getAllSolutionsByStrategies(List<String> strategyNameList) {
		Criteria criteria = new Criteria().where("key").in(strategyNameList);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, AppStrategyConfigDTO.class);
	}
}
