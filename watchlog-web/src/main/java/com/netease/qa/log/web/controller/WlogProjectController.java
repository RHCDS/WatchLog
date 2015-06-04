package com.netease.qa.log.web.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.util.Const;

@Controller
@RequestMapping(value = "/")
public class WlogProjectController {

	@Resource
	private ApiExceptionHandler apiException;
	@Resource
	private ProjectService projectService;
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllProjects() {
	
		String message = Const.RESPONSE_SUCCESSFUL;
		JSONArray data = projectService.getAllProjects();
		if(data == null){
			message = Const.RESPONSE_NOTSUCCESSFUL;
		}
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("data", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}
}
