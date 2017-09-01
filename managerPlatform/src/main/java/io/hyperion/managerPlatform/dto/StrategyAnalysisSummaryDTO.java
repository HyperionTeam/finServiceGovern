package io.hyperion.managerPlatform.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document(collection = "strategyAnalysisSummary")
@Component("StrategyAnalysisSummaryDTO")
@CompoundIndexes({
    @CompoundIndex(background=true,unique=true,name ="strategy_analysis_summary_kt_uidx", def ="{'key': -1, 'time': -1}")
})
public class StrategyAnalysisSummaryDTO {
	
	private String _id;
	
	private String key;	// 数据源key
	private String triggerName;  // 策略名
	private String sourceData;	// 源数据
	private String dataResult;	//策略执行命令，相当于回调
	private String time;	//最近操作时间
	private int governStatus;	// 治理状态：0-治理未触发，1-治理中，2-治理成功，3-治理失败
	private String governResult;	// 治理结果详情
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getSourceData() {
		return sourceData;
	}
	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}
	public String getDataResult() {
		return dataResult;
	}
	public void setDataResult(String dataResult) {
		this.dataResult = dataResult;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getGovernStatus() {
		return governStatus;
	}
	public void setGovernStatus(int governStatus) {
		this.governStatus = governStatus;
	}
	public String getGovernResult() {
		return governResult;
	}
	public void setGovernResult(String governResult) {
		this.governResult = governResult;
	}
	


}
