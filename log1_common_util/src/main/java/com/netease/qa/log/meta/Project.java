package com.netease.qa.log.meta;


public class Project {
	
	private int projectId;
	private String projectName;
	private String projectEngName;
	private Long createTime;
	private Long modifyTime;
	private int timeAccuracy;
	private int projectStatus;
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectEngName() {
		return projectEngName;
	}
	
	public void setProjectEngName(String projectEngName) {
		this.projectEngName = projectEngName;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Long getModifyTime() {
		return modifyTime;
	}
	
	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public int getTimeAccuracy() {
		return timeAccuracy;
	}
	
	public void setTimeAccuracy(int timeAccuracy) {
		this.timeAccuracy = timeAccuracy;
	}

	
	public int getProjectStatus() {
		return projectStatus;
	}

	
	public void setProjectStatus(int projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	public String toString(){
		return "{projectId:" + projectId + ", projectName:" + projectName + ", projectEngName:" + projectEngName + 
				", timeAccuracy:" + timeAccuracy + ", status:" + projectStatus  + "}";
	}
	
	
}
