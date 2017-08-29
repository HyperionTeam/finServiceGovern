package io.hyperion.managerPlatform.vo;

import java.util.List;

public class AllKnowledgeStrategyVo {

	private List<AllKnowledgeStrategy> strategy;
	private Long totalPageNumber;
	private Integer currentPageNumber;

	public List<AllKnowledgeStrategy> getStrategy() {
		return strategy;
	}

	public void setStrategy(List<AllKnowledgeStrategy> strategy) {
		this.strategy = strategy;
	}

	public Long getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(Long totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public Integer getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(Integer currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}



	public static class AllKnowledgeStrategy {
//		private String name;
		private String key;
		private Integer status;
		private String opTime;

//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
		

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getOpTime() {
			return opTime;
		}

		public void setOpTime(String opTime) {
			this.opTime = opTime;
		}
	}
}
