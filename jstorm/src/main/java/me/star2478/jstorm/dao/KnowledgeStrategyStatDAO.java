package me.star2478.jstorm.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;

//public interface KnowledgeStrategyStatDAO extends MongoRepository<KnowledgeStrategyStatDTO, Long> {
public interface KnowledgeStrategyStatDAO {
//	KnowledgeStrategyStatDTO getStatByKey(String key);

//	public KnowledgeStrategyStatDTO getKnowledgeStrategyStat(String key,String field,String time);
//	public List<KnowledgeStrategyStatDTO> getKnowledgeStrategyStat(int skip ,int limit);
//	public void addKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO);
//	public void modifyKnowledgeStrategyStatVal(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO);
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime);
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field,int skip,int limitSize);
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field, String beginTime, String endTime);
//	public List<KnowledgeStrategyStatDTO> getStatByKey(String key, String beginTime, String endTime);
//	public List<KnowledgeStrategyStatDTO> getAllFieldByKey(String key);
	public void upsertIncKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value);
	public void upsertSetKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value);
//	public void deleteKnowledgeStrategyStatByOpTime(String time);
}