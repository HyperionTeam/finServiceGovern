package io.hyperion.managerPlatform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.hyperion.managerPlatform.dto.AppStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyBaseInfoDTO;

public interface KnowledgeStrategyBaseInfoDAO extends MongoRepository<KnowledgeStrategyBaseInfoDTO, Long> {

	public void upsertBaseInfo(String key, String appId, String os, String appVersion);
	
	public KnowledgeStrategyBaseInfoDTO getBaseInfo(String key);
	
	public List<KnowledgeStrategyBaseInfoDTO> getAllBaseInfo();
	
	public void batchUpsertBaseInfo(String key, List<String> appId, List<String> os, List<String> appVersion);
	
	public void dropCol(String name);
}
