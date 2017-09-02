package io.hyperion.managerPlatform.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyStatDTO;
import io.hyperion.managerPlatform.dto.StrategyAnalysisSummaryDTO;


public interface StrategyAnalysisSummaryDAO extends MongoRepository<StrategyAnalysisSummaryDTO, Long> {

	public List<StrategyAnalysisSummaryDTO> getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime);
	public StrategyAnalysisSummaryDTO getTheLastestSummary(String key, String triggerName);
}
