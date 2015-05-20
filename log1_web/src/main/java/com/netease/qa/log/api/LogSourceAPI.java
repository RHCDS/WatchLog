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
import com.netease.qa.log.exception.ConflictRequestException;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.user.service.LogsourceService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Controller
@RequestMapping("/api/logsource")
public class LogSourceAPI {
	
	@Resource
	private LogsourceService logsourceService;
	
	@Resource
	private ApiExceptionHandler apiException;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addLogsource(@RequestParam("logsourcename") String logsourceName,
			@RequestParam("projectid") String projectid, @RequestParam("hostname") String hostname,
			@RequestParam("path") String path, @RequestParam("filepattern") String filepattern,
			@RequestParam("linestart") String linestart, @RequestParam("filterkeyword") String filterkeyword,
			@RequestParam("typeregex") String typeregex, @RequestParam("logsourcecreatorname") String creatorname, Model model) {
		if (!MathUtil.isInteger(projectid)) {
			// projectid 不是num
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		// 获取post提交的数据
		int result = logsourceService.addLogsource(logsourceName, Integer.parseInt(projectid), hostname, path, filepattern, linestart,
				filterkeyword, typeregex, creatorname);
		// 409错误，有冲突 
		if (result == -2) {
			ConflictRequestException cr = new ConflictRequestException(Const.LOG_ALREADY_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		} 
		else if (result == -1) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} 
		// 内部错误，500,基本很少出现这种错误
		else if (result == 0) { 
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		else {
			JSONObject json = new JSONObject();
			json.put("logsourceid", result);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateLogsource(@PathVariable String logsourceid,
			@RequestParam("logsourcename") String logsourcename, @RequestParam("hostname") String hostname,
			@RequestParam("path") String path, @RequestParam("filepattern") String filepattern,
			@RequestParam("linestart") String linestart, @RequestParam("filterkeyword") String filterkeyword,
			@RequestParam("typeregex") String typeregex, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			// logsourceid 不是数字
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		
		int result = logsourceService.updateLogsource(Integer.parseInt(logsourceid), logsourcename, hostname, path, filepattern,
				linestart, filterkeyword, typeregex);
		if (result == -1) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} 
		else if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSource(@PathVariable String logsourceid, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		
		JSONObject logSource = logsourceService.findLogsource(Integer.parseInt(logsourceid));
		if (logSource == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<JSONObject>(logSource, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> deleteLogSource(@PathVariable String logsourceid, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		
		int result = logsourceService.deleteLogsource(Integer.parseInt(logsourceid));
		if (result == -1) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} 
		else if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
	

	@RequestMapping(value = "/changestatus/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> changeStatus(@PathVariable String logsourceid,
			@RequestParam("status") String status, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(status)) {
			InvalidRequestException ex = new InvalidRequestException(Const.STATUS_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		
		int result = logsourceService.changeLogsourceStatus(Integer.parseInt(logsourceid), Integer.parseInt(status));
		if (result == -1) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
	
}
