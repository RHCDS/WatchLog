package com.netease.qa.log.web.api;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.ConflictRequestException;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Controller
@RequestMapping(value = "api/project/")
public class ProjectAPI {

	@Resource
	private ProjectService projectService;
	@Resource
	private ApiExceptionHandler apiException;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JSONObject> createProject(@RequestParam("name") String name, @RequestParam("name_eng") String name_eng,
			@RequestParam("accuracy") String accuracy, Model model) {
		if (!MathUtil.isInteger(accuracy)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ACCURACY_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(projectService.checkProjectExsit(name_eng)){
			ConflictRequestException cr = new ConflictRequestException(Const.PROJECT_ALREADY_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		}
		Project project = new Project();
		project.setProjectName(name);
		project.setProjectEngName(name_eng);
		project.setTimeAccuracy(Integer.parseInt(accuracy));
		int result = projectService.createProject(project);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		else {
			JSONObject json = new JSONObject();
			json.put("projectid", result);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "{projectid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateProject(@PathVariable String projectid, @RequestParam("name") String name,
			@RequestParam("name_eng") String name_eng, @RequestParam("accuracy") String accuracy, Model model) {
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(accuracy)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ACCURACY_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(!projectService.checkProjectExsit(Integer.parseInt(projectid))){
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		
		Project project = new Project();
		project.setProjectId(Integer.parseInt(projectid)); 
		project.setProjectName(name);
		project.setProjectEngName(name_eng);
		project.setTimeAccuracy(Integer.parseInt(accuracy));
		int result = projectService.updateProject(project);
	    if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
	

	@RequestMapping(value = "{projectid}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findProject(@PathVariable String projectid, Model model) {
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(!projectService.checkProjectExsit(Integer.parseInt(projectid))){
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		
		JSONObject result = projectService.getDetailByProjectId(Integer.parseInt(projectid));
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}
	
}

