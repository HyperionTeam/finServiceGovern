package me.star2478.jstorm.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import me.star2478.jstorm.dto.StrategyAnalysisSummaryDTO;




public interface StrategyAnalysisSummaryDAO {//extends MongoRepository<StrategyAnalysisSummaryDTO, Long> {

	public List<StrategyAnalysisSummaryDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime);
	public void incStrategySummary(StrategyAnalysisSummaryDTO dto);
	public void updStrategySummary(StrategyAnalysisSummaryDTO dto);
}
