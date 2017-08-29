package io.hyperion.managerPlatform.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.hyperion.managerPlatform.dto.AppStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;

public interface AppStrategyConfigDAO extends MongoRepository<AppStrategyConfigDTO, Long> {
	
	public AppStrategyConfigDTO getConfigByKey(String key);
	
	public long getAppStrategyNumber();
	
	public List<AppStrategyConfigDTO> getAppStrategyByPage(int skip ,int limit);
	
	public void insertAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO);
	
	public boolean modifyAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO);
	
	public List<AppStrategyConfigDTO> getAllAppStrategy();
}
