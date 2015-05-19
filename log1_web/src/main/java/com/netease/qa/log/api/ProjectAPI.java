package com.netease.qa.log.api;

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
import com.netease.qa.log.invaildrequest.ConflictRequestException;
import com.netease.qa.log.invaildrequest.InvalidRequestException;
import com.netease.qa.log.invaildrequest.NotFoundRequestException;
import com.netease.qa.log.user.service.ProjectService;
import com.netease.qa.log.util.Match;

@Controller
@RequestMapping(value = "api/project/")
public class ProjectAPI {

	@Resource
	private ProjectService projectService;
	@Resource
	private ApiExceptionHandler apiException;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addProject(@RequestParam("name") String name, @RequestParam("name_eng") String name_eng,
			@RequestParam("accuracy") String accuracy, Model model) {
		boolean isNum = Match.isInteger(accuracy);
		if (!isNum) {
			// logsourceid 不是数字
			InvalidRequestException ex = new InvalidRequestException("accuracy  must is a number");
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int addResult = projectService.addProject(name, name_eng, Integer.parseInt(accuracy));
		if (addResult == -2) {
			ConflictRequestException cr = new ConflictRequestException("The project is already existence");
			// 409错误，有冲突
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		} else if (addResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("internal server error");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			JSONObject json = new JSONObject();
			json.put("projectid", addResult);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "{projectid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateProject(@PathVariable String projectid, @RequestParam("name") String name,
			@RequestParam("name_eng") String name_eng, @RequestParam("accuracy") String accuracy, Model model) {
		boolean isNum1 = Match.isInteger(projectid);
		boolean isNum2 = Match.isInteger(accuracy);
		if (!isNum1 || !isNum2) {
			// projectid 不是数字
			InvalidRequestException ex = new InvalidRequestException("projectid or accuracy  must is a number");
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int updateResult = projectService.updateProject(Integer.parseInt(projectid), name, name_eng, Integer.parseInt(accuracy));
		if (updateResult == -1) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (updateResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("internal server error");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "{projectid}", method = RequestMethod.GET, produces = { "application/json;charset=utf-8" })
	public ResponseEntity<JSONObject> findProject(@PathVariable String projectid, Model model) {
		boolean isNum = Match.isInteger(projectid);
		if (!isNum) {
			// projectid 不是数字
			InvalidRequestException ex = new InvalidRequestException("projectid  must is a number");
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		JSONObject project = projectService.findProject(Integer.parseInt(projectid));
		if (project == null) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<JSONObject>(project, HttpStatus.OK);
	}
}
