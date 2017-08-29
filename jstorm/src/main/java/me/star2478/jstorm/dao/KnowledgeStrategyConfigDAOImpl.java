package me.star2478.jstorm.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import me.star2478.jstorm.JstormMain;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO;

//@Component("KnowledgeStrategyConfigDAO")

//public class KnowledgeStrategyConfigDAOImpl extends BaseMongoDAO<KnowledgeStrategyConfigDTO> implements KnowledgeStrategyConfigDAO {
//public class KnowledgeStrategyConfigDAOImpl implements KnowledgeStrategyConfigDAO,MongoRepository<KnowledgeStrategyConfigDTO, Long> {
//@Component("KnowledgeStrategyConfigDAOImpl")
@Repository
public class KnowledgeStrategyConfigDAOImpl implements KnowledgeStrategyConfigDAO {
	private final static String KNOWLEDGE_STRATEGY_CONFIG_COL = "knowledgeStrategyConfig";	//mongodb表名
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@Override
	public KnowledgeStrategyConfigDTO getConfigByKey(String key) {
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeStrategyConfigDTO.class);
	}
	
//	@Override
//	public void insertKnowledgeStrategy(KnowledgeStrategyConfigDTO KnowledgeStrategyConfigDTO){
//		this._add(KnowledgeStrategyConfigDTO);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyConfigDTO> getKnowledgeStrategyByPage(int skip ,int limit) {
//		Sort sort = new Sort(Sort.Direction.DESC, "opTime");
//		return this._list(null, skip, limit, sort);
//	}
//	
//	@Override
//	public boolean deleteKnowledgeStrategyByName(String key) {
//		int returnCode =  this._remove(Criteria.where("key").is(key));
//		if(returnCode == 1){
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
//	@Override
//	public boolean deleteKnowledgeStrategyByOpTime(String opTime) {
//		int returnCode =  this._remove(Criteria.where("opTime").is(opTime));
//		if(returnCode == 1){
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
//	@Override
//	public boolean modifyKnowledgeStrategyByName(KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO){
//		String key = knowledgeStrategyConfigDTO.getKey();
//		Update update = new Update();
//		update.set("description", knowledgeStrategyConfigDTO.getDescription());
//		update.set("status", knowledgeStrategyConfigDTO.getStatus());
//		update.set("expire", knowledgeStrategyConfigDTO.getExpire());
//		update.set("opTime", knowledgeStrategyConfigDTO.getOpTime());
//		update.set("appStrategyTriggers", knowledgeStrategyConfigDTO.getAppStrategyTriggers());
//		return this._update(Criteria.where("key").is(key), update);
//	}
//	
//	@Override
//	public long getKnowledgeStrategyNumber() {
//		return this._count(null);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyConfigDTO> getAllPersistentedStrategy(int status, int persistent, int expire) {
//		Criteria criteria = Criteria.where("status").is(status).and("persistent").is(persistent).and("expire").gte(expire);
//		return this._list(criteria);
//	}
}


