package me.star2478.jstorm.dao;

import java.util.List;

import me.star2478.jstorm.dto.AppStrategyConfigDTO;

public interface AppStrategyConfigDAO {
	
	public AppStrategyConfigDTO getConfigByKey(String key);
	
	public List<AppStrategyConfigDTO> getAllSolutionsByStrategies(List<String> strategyNameList);
	
//	public long getAppStrategyNumber();
//	
//	public List<AppStrategyConfigDTO> getAppStrategyByPage(int skip ,int limit);
//	
//	public void insertAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO);
//	
//	public boolean modifyAppStrategy(AppStrategyConfigDTO appStrategyConfigDTO);
//	
//	public List<AppStrategyConfigDTO> getAllAppStrategy();
}
