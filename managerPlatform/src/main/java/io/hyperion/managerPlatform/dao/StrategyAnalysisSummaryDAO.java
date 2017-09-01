package io.hyperion.managerPlatform.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyStatDTO;
import io.hyperion.managerPlatform.dto.StrategyAnalysisSummaryDTO;


public interface StrategyAnalysisSummaryDAO extends MongoRepository<StrategyAnalysisSummaryDTO, Long> {

	public List<StrategyAnalysisSummaryDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime);
//	public StrategyAnalysisSummaryDTO getKnowledgeStrategyStat(String key,String field,String time);
//	public List<KnowledgeStrategyStatDTO> getKnowledgeStrategyStat(int skip ,int limit);
//	public void addKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO);
//	public void modifyKnowledgeStrategyStatVal(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO);
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field,int skip,int limitSize);
//	public List<KnowledgeStrategyStatDTO> getStatByKeyAndField(String key, String field, String beginTime, String endTime);
//	public List<KnowledgeStrategyStatDTO> getStatByKey(String key, String beginTime, String endTime);
//	public List<KnowledgeStrategyStatDTO> getAllFieldByKey(String key);
//	public void upsertIncKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value);
//	public void upsertSetKnowledgeStrategyStat(KnowledgeStrategyStatDTO knowledgeStrategyStatDTO, Object value);
//	public void deleteKnowledgeStrategyStatByOpTime(String time);
}
