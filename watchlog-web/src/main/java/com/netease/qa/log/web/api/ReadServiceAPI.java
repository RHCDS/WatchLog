package com.netease.qa.log.web.api;

import java.text.ParseException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Controller
@RequestMapping("/api/report")
public class ReadServiceAPI {

	private static final Logger logger = LoggerFactory.getLogger(ReadServiceAPI.class);

	@Resource
	private ReadService readService;

	@Resource
	private LogSourceService logsourceService;
	
	@Resource
	private ApiExceptionHandler apiException;

	/**
	 * 按时间聚合
	 */
	@RequestMapping(value = "/time/{id}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> readByTime(@PathVariable String id,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset, Model model) {
		if (MathUtil.isEmpty(start, end, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(id)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(!logsourceService.checkLogSourceExist(Integer.valueOf(id))){ 
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		Long startTime = null;
		Long endTime = null;
		try {
			startTime = MathUtil.parse2Long(start);
			endTime = MathUtil.parse2Long(end);
		} catch (ParseException e) {
			logger.error("error", e);
			InvalidRequestException ex = new InvalidRequestException(Const.INVALID_TIME_FORMAT);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}

		JSONObject jsonObject = readService.queryTimeRecords(Integer.parseInt(id), startTime, endTime,
				Integer.parseInt(limit), Integer.parseInt(offset));
		if (jsonObject == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
	}

	/**
	 * 按异常类型聚合
	 */
	@RequestMapping(value = "/error/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JSONObject> readByError(@PathVariable String id, 
			@RequestParam(value = "start",required = false) String start,
			@RequestParam(value = "end", required = false) String end, 
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset, Model model) {
		if (MathUtil.isEmpty(start, end, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(id)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(!logsourceService.checkLogSourceExist(Integer.valueOf(id))){
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		Long startTime = null;
		Long endTime = null;
		try {
			startTime = MathUtil.parse2Long(start);
			endTime = MathUtil.parse2Long(end);
		} catch (ParseException e) {
			logger.error("error", e); 
			InvalidRequestException ex = new InvalidRequestException(Const.INVALID_TIME_FORMAT);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}

		JSONObject jsonObject = readService.queryErrorRecords(Integer.parseInt(id), startTime, endTime,
				Integer.parseInt(limit), Integer.parseInt(offset));
		if (jsonObject == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
	}

	
	/**
	 * 获取unknown类型异常
	 */
	@RequestMapping(value = "/unknown/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JSONObject> readByUnknow(@PathVariable String id, 
			@RequestParam(value = "start",required = false) String start,
			@RequestParam(value = "end", required = false) String end, 
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset, Model model) {
		if (MathUtil.isEmpty(start, end, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(id)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if(!logsourceService.checkLogSourceExist(Integer.valueOf(id))){
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		Long startTime = null;
		Long endTime = null;
		try {
			startTime = MathUtil.parse2Long(start);
			endTime = MathUtil.parse2Long(end);
		} catch (ParseException e) {
			logger.error("error", e);
			InvalidRequestException ex = new InvalidRequestException(Const.INVALID_TIME_FORMAT);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}

		JSONObject jsonObject = readService.queryUnknownExceptions(Integer.parseInt(id), startTime, endTime,
				Integer.parseInt(limit), Integer.parseInt(offset));
		if (jsonObject == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
	}

}
