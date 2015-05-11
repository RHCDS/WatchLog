package com.netease.qa.log.meta;


public class LogSource {
	
	private int logSourceId;
	private int projectId;
	private Long createTime;
	private Long modifyTime;
	private String hostname;
	private String path;
	private String filePattern;
	private String lineStartRegex;
	private String lineFilterKeyword;
	private String lineTypeRegex;
	
	public int getLogSourceId() {
		return logSourceId;
	}
	
	public void setLogSourceId(int logSourceId) {
		this.logSourceId = logSourceId;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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
	
	public String toString(){
		return "{logSourceId:" + logSourceId + ", projectId:" + projectId + ", hostname:" + hostname + 
				", path:" + path + ", file:" + filePattern + ", lineStartRegex:" + lineStartRegex + 
				", FilterKeyword:" + lineFilterKeyword + ", TypeRegex:" + lineTypeRegex + "}";
	}
}
