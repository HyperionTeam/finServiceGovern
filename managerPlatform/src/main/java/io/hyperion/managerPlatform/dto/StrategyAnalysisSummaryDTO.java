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
    @CompoundIndex(background=true,unique=true,name ="strategy_analysis_summary_kt_uidx", def ="{'key': -1, 'triggerName': -1, 'time': -1}")
})
public class StrategyAnalysisSummaryDTO {
	
	public static final String EVENT_DATA_ANALYSIS_START = "问题定位开始";
	public static final String EVENT_DATA_ANALYSIS_DOING = "问题定位中";
	public static final String EVENT_DATA_ANALYSIS_SUCCESS = "问题定位成功";
	public static final String EVENT_DATA_ANALYSIS_FAIL = "问题定位失败";
	public static final String EVENT_DATA_ANALYSIS_RESULT = "问题定位结果：";

	public static final String EVENT_GOV_START = "问题治理开始";
	public static final String EVENT_GOV_DOING = "问题治理中";
	public static final String EVENT_GOV_SUCCESS = "问题治理成功";
	public static final String EVENT_GOV_FAIL = "问题治理失败";
	public static final String EVENT_GOV_RESULT = "问题治理结果：";
	
	private String _id;
	
	private String key;	// 数据源key
	private String triggerName;  // 策略名
	private String sourceData;	// 源数据
	private String dataResult;	//策略执行命令，相当于回调
	private String time;	//最近操作时间
	private int dataAnalysisStatus;	// 定位状态：0-定位未触发，1-定位中，2-定位成功，3-定位失败
	private int governStatus;	// 治理状态：0-治理未触发，1-治理中，2-治理成功，3-治理失败
	private String governResult;	// 治理结果详情
	private List<EventInfo> evenList = new ArrayList<EventInfo>();	// 事件列表
	private List<EventInfo> eventList = new ArrayList<EventInfo>();	// 事件列表/////////////////////////
	
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
	
	public int getDataAnalysisStatus() {
		return dataAnalysisStatus;
	}
	public void setDataAnalysisStatus(int dataAnalysisStatus) {
		this.dataAnalysisStatus = dataAnalysisStatus;
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
	
	public List<EventInfo> getEvenList() {
		return evenList;
	}
	public void setEvenList(List<EventInfo> evenList) {
		this.evenList = evenList;
	}
	

	public List<EventInfo> getEventList() {
		return eventList;
	}
	public void setEventList(List<EventInfo> eventList) {
		this.eventList = eventList;
	}


	public static class EventInfo {
		private String event;	// 事件
		private String time;	// 事件发生时间
		private int status;	// 状态：0-未触发，1-进行中，2-成功，3-失败
		public String getEvent() {
			return event;
		}
		public void setEvent(String event) {
			this.event = event;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}

	}

}
