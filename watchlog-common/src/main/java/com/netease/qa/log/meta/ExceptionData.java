package com.netease.qa.log.meta;


public class ExceptionData {
	
	private int exceptionDataId;
	private int logSourceId;
	private Long sampleTime;
	private int exceptionId;
	private String exceptionType;
	private int exceptionCount;
	
	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	public int getExceptionDataId() {
		return exceptionDataId;
	}
	
	public void setExceptionDataId(int exceptionDataId) {
		this.exceptionDataId = exceptionDataId;
	}
	
	public int getLogSourceId() {
		return logSourceId;
	}
	
	public void setLogSourceId(int logSourceId) {
		this.logSourceId = logSourceId;
	}
	
	public Long getSampleTime() {
		return sampleTime;
	}
	
	public void setSampleTime(Long sampleTime) {
		this.sampleTime = sampleTime;
	}
	
	public int getExceptionId() {
		return exceptionId;
	}
	
	public void setExceptionId(int exceptionId) {
		this.exceptionId = exceptionId;
	}
	
	public int getExceptionCount() {
		return exceptionCount;
	}
	
	public void setExceptionCount(int exceptionCount) {
		this.exceptionCount = exceptionCount;
	}
	

	public String toString(){
		return "sampleTime: " + sampleTime + ", logSourceId: " + logSourceId + ", exceptionId: " + exceptionId + ", exceptionCount: " + exceptionCount;
	}
	
}
