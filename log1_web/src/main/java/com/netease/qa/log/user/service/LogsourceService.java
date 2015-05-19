package com.netease.qa.log.user.service;

import com.alibaba.fastjson.JSONObject;

//定义配置日资源的接口
public interface LogsourceService {

	//创建日志源
	public int addLogsource(String logsourcename, int projectId, String hostname, String path, String filepattern,
			String linestart, String filterkeyword, String typeregex, String creatorname);
	//修改日志源
	public int updateLogsource(int logsourceid, String logsourcename, String hostname, String path, String filepattern,
			String linestart, String filterkeyword, String typeregex);
	//查询日志源
	public JSONObject findLogsource(int logsourceid);
	//删除日志源
	public int deleteLogsource(int logsourceid);
	//更改日志源
	public int changeLogsourceStatus(int logsourceid, int status);
	
}
