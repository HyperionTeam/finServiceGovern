package io.hyperion.managerPlatform.vo;

import java.util.List;

public class AllAppStrategyVo {

	private List<AllAppStrategy> strategy;
	private Long totalPageNumber; 
	private Integer currentPageNumber;
	
	
	
	public List<AllAppStrategy> getStrategy() {
		return strategy;
	}



	public void setStrategy(List<AllAppStrategy> strategy) {
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




	public static class AllAppStrategy {
		private String key;
		private String opTime;
//		public String getName() {
//			return name;
//		}
//		public void setName(String name) {
//			this.name = name;
//		}
		
		public String getOpTime() {
			return opTime;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public void setOpTime(String opTime) {
			this.opTime = opTime;
		}
	}
}
