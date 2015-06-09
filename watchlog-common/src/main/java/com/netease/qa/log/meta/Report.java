package com.netease.qa.log.meta;

import java.sql.Timestamp;

public class Report {

	private int reportId;
	private int logSourceId;
	private int projectId;
	private Timestamp startTime;
	private Timestamp endTime;
	private Timestamp createTime;
	private String creator;
	private String reportComment;

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getCreatTime() {
		return createTime;
	}

	public void setCreatTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getReportComment() {
		return reportComment;
	}

	public void setReportComment(String reportComment) {
		this.reportComment = reportComment;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("reportId:").append(reportId).append(";logSourceId:").append(logSourceId).append(";projectId:")
				.append(projectId).append(";startTime:").append(startTime).append(";endTime:").append(endTime)
				.append(";creator:").append(creator).append(";creatTime:").append(createTime).append(";reportComment:")
				.append(reportComment);
		return sb.toString();
	}
}
