package me.star2478.jstorm.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;
import me.star2478.jstorm.service.StrategyServiceString;

@Repository
//public class KnowledgeStrategyStatDAOImpl extends BaseMongoDAO<KnowledgeStrategyStatDTO> implements KnowledgeStrategyStatDAO{
public class KnowledgeStrategyStatDAOImpl implements KnowledgeStrategyStatDAO{

	private static Logger logger = Logger.getLogger(KnowledgeStrategyStatDAOImpl.class);
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
//	@Override
//	public KnowledgeStrategyStatDTO getKnowledgeStrategyStat(String key,String field,String time) {
//		return this._get(Criteria.where("key").is(key).and("field").is(field).and("time").is(time));
//	}
//	
//	@Override
//	public List<KnowledgeStrategyStatDTO> getKnowledgeStrategyStat(int skip ,int limit)  {
//		return this._list(null, skip, limit);
//	}
//	
//	@Override
//	public void addKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO) {
//		this._add(knowledgeStrategyStatDTO);
//	}
//	
//	@Override
//	public void modifyKnowledgeStrategyStatVal(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO) {
//		Update update = new Update();
//		update.set("value", knowledgeStrategyStatDTO.getValue());
//	    Criteria criteria = Criteria.where("key").is(knowledgeStrategyStatDTO.getKey()).and("triggerName").is(knowledgeStrategyStatDTO.getTriggerName())
//	    .and("time").is(knowledgeStrategyStatDTO.getTime());
//		this._update(criteria, update);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime) {
//		Criteria criteria = Criteria.where("key").is(key).and("appId").is(appID).
//				and("os").is(os).and("appVersion").is(appVersion).and("triggerName").is(triggerName).
//				and("time").gte(beginTime).lte(endTime);
////		return this._list(criteria);
//		MongoTemplate mongoTemplate = this.getMongoTemplate();
//		Query query = new Query();
//		query.addCriteria(Criteria.where("key").is(key));
//		if(appID != null) {
//			query.addCriteria(Criteria.where("appId").is(appID));
//		}
//		if(os != null) {
//			query.addCriteria(Criteria.where("os").is(os));
//		}
//		if(appVersion != null) {
//			query.addCriteria(Criteria.where("appVersion").is(appVersion));
//		}
//		if(triggerName != null) {
//			query.addCriteria(Criteria.where("triggerName").is(triggerName));
//		}
//		query.addCriteria(Criteria.where("time").gte(beginTime).lte(endTime));
//		return mongoTemplate.find(query, KnowledgeStrategyStatDTO.class);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field,int skip,int limitSize) {
//		Criteria criteria = Criteria.where("key").is(key).and("field").is(field);
//		Sort sort = new Sort(Sort.Direction.DESC, "time");
//		return this._list(criteria, skip, limitSize, sort);
//	}
//
//	@Override
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field, String beginTime, String endTime) {
//		Criteria criteria = Criteria.where("key").is(key).and("field").is(field).and("time").gte(beginTime).lte(endTime);
//		return this._list(criteria);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyStatDTO> getStatByKey(String key, String beginTime, String endTime) {
//		Criteria criteria = Criteria.where("key").is(key).and("time").gte(beginTime).lte(endTime);
//		return this._list(criteria);
//	}
//	
//	@Override
//	public List<KnowledgeStrategyStatDTO> getAllFieldByKey(String key) {
//		Criteria criteria = Criteria.where("key").is(key);
//		return this._list(criteria);
//	}
	
	@Override
	public void upsertIncKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value) {
		try {
			Update update = new Update();
			update.inc("value", Float.valueOf(value.toString()));
			Query q = new Query(Criteria.where("appId").is(knowledgeStrategyStatDTO.getAppId()).and("os")
					.is(knowledgeStrategyStatDTO.getOs()).and("appVersion").is(knowledgeStrategyStatDTO.getAppVersion())
					.and("key").is(knowledgeStrategyStatDTO.getKey()).and("triggerName")
					.is(knowledgeStrategyStatDTO.getTriggerName()).and("time").is(knowledgeStrategyStatDTO.getTime()));
			mongoTemplate.upsert(q, update, KnowledgeStrategyStatDTO.class);

		} catch (UncategorizedMongoDbException | DataIntegrityViolationException e) {	// 如果mongodb现在数据不是数字，就会捕获异常，改用set方式
			logger.fatal("inc mongoDB fail, because new value="+value+" is numeric type but old value of db is non-numeric type");
			upsertSetKnowledgeStrategyStat(knowledgeStrategyStatDTO, value);
//		} catch (DataIntegrityViolationException e) {
//			upsertIncKnowledgeStrategyStatExceptionHandler(knowledgeStrategyStatDTO, value);
		}
	}
	
	@Override
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
	
//	@Override
//	public void deleteKnowledgeStrategyStatByOpTime(String time) {
//		int returnCode =  this._remove(Criteria.where("time").is(time));
//	}
	
//	private void upsertIncKnowledgeStrategyStatExceptionHandler(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value) {
//		logger.fatal("inc mongoDB fail, because new value="+value+" is numeric type but old value of db is non-numeric type");
//		upsertSetKnowledgeStrategyStat(knowledgeStrategyStatDTO, value);
//	}
}
