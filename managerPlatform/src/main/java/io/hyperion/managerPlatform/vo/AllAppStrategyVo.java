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
		private int type;	// 1-数据分析方案，0-治理方案
		private String description;
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
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}
}
