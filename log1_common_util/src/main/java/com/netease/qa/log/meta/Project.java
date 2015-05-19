package com.netease.qa.log.meta;
import java.sql.Timestamp;

public class Project {
	
	private int projectId;
	private String projectName;
	private String projectEngName;
	private Timestamp createTime;
	private Timestamp modifyTime;
	private int timeAccuracy;
	
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
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getModifyTime() {
		return modifyTime;
	}
	
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public int getTimeAccuracy() {
		return timeAccuracy;
	}
	
	public void setTimeAccuracy(int timeAccuracy) {
		this.timeAccuracy = timeAccuracy;
	}

	
	public String toString(){
		return "{projectId:" + projectId + ", projectName:" + projectName + ", projectEngName:" + projectEngName + 
				", timeAccuracy:" + timeAccuracy +"}";
	}
	
	
}
