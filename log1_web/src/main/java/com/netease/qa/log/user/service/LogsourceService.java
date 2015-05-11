package com.netease.qa.log.user.service;

import com.netease.qa.log.meta.LogSource;

//定义配置日资源的接口
public interface LogsourceService {

	//创建日志源
	public int addLogsource(int projectId,String hostname,String path,String filepattern,
			String linestart,String filterkeyword,String typeregex);
	//修改日志源
	public int updateLogsource(int logsourceid,String hostname,String path,String filepattern,
			String linestart,String filterkeyword,String typeregex);
	//查询日志源
	public LogSource findLogsource(int logsourceid);
	//删除日志源
	public int deleteLogsource(int logsourceid);
}
