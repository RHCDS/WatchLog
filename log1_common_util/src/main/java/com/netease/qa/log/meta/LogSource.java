package com.netease.qa.log.meta;

import java.sql.Timestamp;


public class LogSource {
	
	private int logSourceId;
	private String logSourceName;
	private int projectId;
	private Timestamp  createTime;
	private Timestamp  modifyTime;
	private String hostname;
	private String path;
	private String filePattern;
	private String lineStartRegex;
	private String lineFilterKeyword;
	private String lineTypeRegex;
	private int logSourceCreatorId;
	private String logSourceCreatorName;
	private int logSourceStatus;
	
	
	public int getLogSourceId() {
		return logSourceId;
	}
	
	public void setLogSourceId(int logSourceId) {
		this.logSourceId = logSourceId;
	}
	
	public String getLogSourceName() {
		return logSourceName;
	}

	public void setLogSourceName(String logSourceName) {
		this.logSourceName = logSourceName;
	}
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFilePattern() {
		return filePattern;
	}
	
	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}
	
	public String getLineStartRegex() {
		return lineStartRegex;
	}
	
	public void setLineStartRegex(String lineStartRegex) {
		this.lineStartRegex = lineStartRegex;
	}
	
	public String getLineFilterKeyword() {
		return lineFilterKeyword;
	}
	
	public void setLineFilterKeyword(String lineFilterKeyword) {
		this.lineFilterKeyword = lineFilterKeyword;
	}
	
	public String getLineTypeRegex() {
		return lineTypeRegex;
	}
	
	public void setLineTypeRegex(String lineTypeRegex) {
		this.lineTypeRegex = lineTypeRegex;
	}
	
	public int getLogSourceStatus() {
		return logSourceStatus;
	}

	public void setLogSourceStatus(int logSourceStatus) {
		this.logSourceStatus = logSourceStatus;
	}


	public int getLogSourceCreatorId() {
		return logSourceCreatorId;
	}

	public void setLogSourceCreatorId(int logSourceCreatorId) {
		this.logSourceCreatorId = logSourceCreatorId;
	}

	public String getLogSourceCreatorName() {
		return logSourceCreatorName;
	}

	public void setLogSourceCreatorName(String logSourceCreatorName) {
		this.logSourceCreatorName = logSourceCreatorName;
	}

	public String toString(){
		return "{logSourceId:" + logSourceId + ",logSourceName:" + logSourceName + ", projectId:" + projectId + ", hostname:" + hostname + 
				", path:" + path + ", file:" + filePattern + ", lineStartRegex:" + lineStartRegex + 
				", FilterKeyword:" + lineFilterKeyword + ", TypeRegex:" + lineTypeRegex + ",LogSourceCreatorId:" + logSourceCreatorId +
				",logSourceCreatorName:" + logSourceCreatorName + ",logSourceStatus:" + logSourceStatus + "}";
	}
}
