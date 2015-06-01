package com.netease.qa.log.web.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Controller
@RequestMapping(value = "/logsrc")
public class WlogSrcController {

	@Resource
	private ApiExceptionHandler apiException;
	@Resource
	private ProjectService projectService;
	@Resource
	private LogSourceService logSourceService;

	@RequestMapping(value = "/manage/tabledata", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSourceByProjectid(
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "length", required = false) String limit,
			@RequestParam(value = "start", required = false) String offset) {
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(
					apiException.handleNullParamException(ne),
					HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(
					Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(
					apiException.handleInvalidRequestError(ex),
					HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(
					Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(
					apiException.handleInvalidRequestError(ex),
					HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(
					Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(
					apiException.handleNotFoundRequestException(nr),
					HttpStatus.NOT_FOUND);
		}
		int recordsTotal = logSourceService.getTotalCountByProjectId(Integer
				.parseInt(projectid));
		String message = "select successful";
		JSONArray data = logSourceService.getLogSourceByProjectid(
				Integer.parseInt(projectid), Integer.parseInt(limit),
				Integer.parseInt(offset));
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("recordsTotal", recordsTotal);
		result.put("data", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}
}
