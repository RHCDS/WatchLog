package com.netease.qa.log.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.util.ConstCN;
import com.netease.qa.log.util.MathUtil;

@Controller
public class WlogRTController {

	@Resource
	private LogSourceService logSourceService;
	@Resource
	private ReadService readService;
	@Resource
	private ApiExceptionHandler apiException;
	@Resource
	private ProjectService projectService;

	@RequestMapping(value = "logsrc/rt_analyse", method = RequestMethod.GET)
	public String rt_analyse(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "log_id", required = false, defaultValue = "0") String logsrcid, Model model) {
		model.addAttribute("controller", "WlogRT");
		model.addAttribute("action", "rt_analyse");
		ArrayList<LogSource> logsources = logSourceService.selectAllByProjectId(Integer.parseInt(projectid));
		ArrayList<String> logs = new ArrayList<String>();
		for (int i = 0; i < logsources.size(); i++) {
			logs.add(logsources.get(i).getLogSourceId() + "#" + logsources.get(i).getLogSourceName());
		}
		model.addAttribute("logs", logs);

		if (logsources.size() != 0) {
			// 没有传logsrcid,默认第一个
			if (Integer.parseInt(logsrcid) == 0) {
				LogSource logSource = logsources.get(0);
				model.addAttribute("logsrc_name", logSource.getLogSourceName());
				model.addAttribute("host_name", logSource.getHostname());
				model.addAttribute("logsrc_path", logSource.getPath());
				model.addAttribute("logsrc_file", logSource.getFilePattern());
				model.addAttribute("start_regex", logSource.getLineStartRegex());
				model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
				model.addAttribute("reg_regex", logSource.getLineTypeRegex());
			} else {
				LogSource logSource = logSourceService.getByLogSourceId(Integer.parseInt(logsrcid));
				model.addAttribute("logsrc_name", logSource.getLogSourceName());
				model.addAttribute("host_name", logSource.getHostname());
				model.addAttribute("logsrc_path", logSource.getPath());
				model.addAttribute("logsrc_file", logSource.getFilePattern());
				model.addAttribute("start_regex", logSource.getLineStartRegex());
				model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
				model.addAttribute("reg_regex", logSource.getLineTypeRegex());
			}
			Calendar end = Calendar.getInstance();
			Calendar start = end;
			start.add(Calendar.MONTH, -1);
			Long startTime = null;
			Long endTime = null;
			try {
				startTime = MathUtil.parse2Long(MathUtil.parse2Str(start.getTime()));
				endTime = MathUtil.parse2Long(MathUtil.parse2Str(end.getTime()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			JSONObject result = readService.queryTimeRecords(Integer.parseInt(logsrcid), startTime, endTime, 15, 0);
			JSONArray records = result.getJSONArray("record");
			JSONObject record = new JSONObject();
			JSONObject error = new JSONObject();
			JSONArray errors = new JSONArray();
			JSONObject detail = new JSONObject();
			JSONArray details = new JSONArray();
			for (int i = 0; i < records.size(); i++) {
				record = records.getJSONObject(i);
				detail.put("datetime", record.get("time"));
				detail.put("totalcount", record.get("totalcount"));
				details = record.getJSONArray("detail");
				for (int j = 0; j < details.size(); j++) {
					JSONObject temp = details.getJSONObject(j);
					error.put("type", temp.get("exceptionType"));
					error.put("count", temp.get("count"));
					errors.add(error);
				}
				detail.put("logtc", errors);
				details.add(detail);
			}
			model.addAttribute("rt_table", details);
		}
		return "logsrc/rt_analyse";
	}

	public ResponseEntity<JSONObject> getSortedReports(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "sort", required = false, defaultValue = "create_time") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset) {
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			NullParamException ne = new NullParamException(ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(ConstCN.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(ConstCN.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(ConstCN.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		return null;

	}
}
