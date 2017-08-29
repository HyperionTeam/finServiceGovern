package io.hyperion.managerPlatform.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import io.hyperion.managerPlatform.dto.AppStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyBaseInfoDTO;
import io.hyperion.managerPlatform.utils.Const;

//@Component("KnowledgeStrategyBaseInfoDAO")
public class KnowledgeStrategyBaseInfoDAOImpl {
	
	private final static String KNOWLEDGE_STRATEGY_CONFIG_COL = "knowledgeStrategyBaseInfo";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	//@Override
	public void upsertBaseInfo(String key, String appId, String os, String appVersion) {
		if (appId == null && os == null && appVersion == null) {
			return;
		}
		Update update = new Update();
		if (appId != null) {
			update.addToSet(Const.BaseInfoEnum.APPID.toString(), appId);
		}
		if (os != null) {
			update.addToSet(Const.BaseInfoEnum.OS.toString(), os);
		}
		if (appVersion != null) {
			update.addToSet(Const.BaseInfoEnum.APPVERSION.toString(), appVersion);
		}
		Query q = new Query(Criteria.where("key").is(key));
		mongoTemplate.upsert(q, update, KnowledgeStrategyBaseInfoDTO.class);
	}
	
	//@Override
	public KnowledgeStrategyBaseInfoDTO getBaseInfo(String key) {
		Criteria criteria = new Criteria().where("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeStrategyBaseInfoDTO.class);
	}
	
	//@Override
	public List<KnowledgeStrategyBaseInfoDTO> getAllBaseInfo() {
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);
		return mongoTemplate.find(query, KnowledgeStrategyBaseInfoDTO.class);
	}
	
	//@Override
	public void batchUpsertBaseInfo(String key, List<String> appId, List<String> os, List<String> appVersion) {
		if (appId == null && os == null && appVersion == null) {
			return;
		}
		Update update = new Update();
		if (appId != null) {
			for (String string : appId) {
				update.set(Const.BaseInfoEnum.APPID.toString(), string);
			}
		}
		if (os != null) {
			for (String string : os) {
				update.set(Const.BaseInfoEnum.OS.toString(), string);
			}
		}
		if (appVersion != null) {
			for (String string : appVersion) {
				update.set(Const.BaseInfoEnum.APPVERSION.toString(), string);
			}
		}
		Query q = new Query(Criteria.where("key").is(key));
		mongoTemplate.upsert(q, update, KnowledgeStrategyBaseInfoDTO.class);
	}
	
	//@Override
	public void dropCol(String name) {
		mongoTemplate.dropCollection(name);
	}
}