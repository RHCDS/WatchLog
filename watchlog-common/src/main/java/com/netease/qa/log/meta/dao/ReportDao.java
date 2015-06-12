package com.netease.qa.log.meta.dao;

import com.netease.qa.log.meta.Report;

/*
 * report has not update
 */
public interface ReportDao {

    public int insert(Report report);

	public int delete(int reportId);
	
	public Report findByReportId(int reportId);
	
	
}
