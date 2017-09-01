package io.hyperion.managerPlatform.vo;

import java.util.List;

import io.hyperion.managerPlatform.dto.StrategyAnalysisSummaryDTO;

public class StrategyAnalysisSummaryVo {

	private String key;
	private List<Field4SASummaryVo> fieldList;

	public List<Field4SASummaryVo> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field4SASummaryVo> fieldList) {
		this.fieldList = fieldList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public static class Field4SASummaryVo {
		private String field;
		private List<StrategyAnalysisSummaryDTO> countList;
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public List<StrategyAnalysisSummaryDTO> getCountList() {
			return countList;
		}
		public void setCountList(List<StrategyAnalysisSummaryDTO> countList) {
			this.countList = countList;
		}
		
	}
	
}
