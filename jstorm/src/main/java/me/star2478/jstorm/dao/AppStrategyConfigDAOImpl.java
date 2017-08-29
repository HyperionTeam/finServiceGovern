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
	
//	@Override
//	public long getAppStrategyNumber() {
//		return this._count(null);
//	}
//	
//	@Override
//	public List<AppStrategyConfigDTO> getAppStrategyByPage(int skip ,int limit) {
//		Sort sort = new Sort(Sort.Direction.DESC, "opTime");
//		return this._list(null, skip, limit, sort);
//	}
//	
//	@Override
//	public void insertAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO) {
//		this._add(appStrategyConfigDTO);
//	}
//	
//	@Override
//	public boolean modifyAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO) {
//		String name = appStrategyConfigDTO.getName();
//		Update update = new Update();
//		update.set("description", appStrategyConfigDTO.getDescription());
//		update.set("command", appStrategyConfigDTO.getCommand());
//		update.set("opTime", appStrategyConfigDTO.getOpTime());
//		return this._update(Criteria.where("name").is(name), update);
//	}
//	
//	@Override
//	public List<AppStrategyConfigDTO> getAllAppStrategy() {
//		return this._list(null);
//	}
}
