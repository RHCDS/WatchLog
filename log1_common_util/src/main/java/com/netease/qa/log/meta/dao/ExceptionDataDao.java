package com.netease.qa.log.meta.dao;

import java.util.List;

import com.netease.qa.log.meta.ExceptionData;

public interface ExceptionDataDao {
	
	public int insert(ExceptionData exceptionData); 
	
	public int delete(int exceptionDataId);
    
    public ExceptionData findByExceptionDataId(int exceptionDataId);
    //在某个时间段内，同一个日志源出现的所有记录
    public List<ExceptionData> findByLogSourceIdAndTime(int logSourceId, long startTime, long endTime, String orderBy, int limit, int offset);
    
    //根据exceptionId，取出的数据不止一条？
    public ExceptionData findByExceptionId(int exceptionId);


}
