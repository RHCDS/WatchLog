package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONArray;
import com.netease.qbs.meta.User;

public interface ProjectQbsService {

	public JSONArray getAllProjectsByQbs(User user);
}
