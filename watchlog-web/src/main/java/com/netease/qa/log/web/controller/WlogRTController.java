package com.netease.qa.log.web.controller;

import java.sql.Timestamp;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Report;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.service.ReportService;
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
	@Resource
	private ReportService reportService;

	@RequestMapping(value = "logsrc/rt_analyse", method = RequestMethod.GET)
	//右边小侧栏的日志源详细信息
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

	//历史报告表格内容
	@RequestMapping(value = "logsrc/pm_analyse/pmtable", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> getSortedReports(
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "sort", required = false, defaultValue = "create_time") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset) {
		String message = "";
		int recordsTotal = 0;
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			message = ConstCN.NULL_PARAM;
			result.put("message", message);
			result.put("total", recordsTotal);
			result.put("rows", data);
			return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		}
		if (!MathUtil.isInteger(projectid)) {
			message = ConstCN.ID_MUST_BE_NUM;
			result.put("message", message);
			result.put("total", recordsTotal);
			result.put("rows", data);
			return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			message = ConstCN.LIMIT_AND_OFFSET_MUST_BE_NUM;
			result.put("message", message);
			result.put("total", recordsTotal);
			result.put("rows", data);
			return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			message = ConstCN.PROJECT_NOT_EXSIT;
			result.put("message", message);
			result.put("total", recordsTotal);
			result.put("rows", data);
			return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		}
		String field = MathUtil.getReportField(sort);
		recordsTotal = reportService.getTotalCountByProjectId(Integer.parseInt(projectid));
		message = ConstCN.RESPONSE_SUCCESSFUL;
		data = reportService.getReportListSortedByProjectid(Integer.parseInt(projectid), field, order,
				Integer.parseInt(limit), Integer.parseInt(offset));
		if (data == null)
			message = ConstCN.RESPONSE_NOTSUCCESSFUL;
		result.put("message", message);
		result.put("total", recordsTotal);
		result.put("rows", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}

	//删除历史报告 
	@RequestMapping(value = "/logsrc/pm_analyse/destroy", method = RequestMethod.POST)
	public String deleteReport(@RequestParam(value = "proj") String projectid,
			@RequestParam(value = "report_id") String reportid, RedirectAttributes model) {
		// 成功和失败的重定向url
		String ret = "/logsrc/pm_analyse?proj=" + projectid;

		if (MathUtil.isEmpty(reportid)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret;
		}
		// 选中删除，所以日志必存在，不需要进行日志检查
		int result = reportService.deleteReport(Integer.parseInt(reportid));
		if (result == 0) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.INNER_ERROR);
			return ret;
		}
		model.addFlashAttribute("status", 0);
		model.addFlashAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);
		return ret;
	}

	@RequestMapping(value = "/logsrc/***", method = RequestMethod.POST)
	public String addReport(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "logsrc_name", required = false) String logsrcName,
			@RequestParam(value = "start", required = false) String startTime,
			@RequestParam(value = "end", required = false) String endTime,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "comment", required = false) String comment, RedirectAttributes model) {
		String ret = "redirect:/logsrc/***?proj=" + projectid;
		if (MathUtil.isEmpty(projectid, logsrcName, startTime, endTime, title, comment)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret;
		}
		if (!MathUtil.isInteger(projectid)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.ID_MUST_BE_NUM);
			return ret;
		}
		// 是否需要判断report插入，重复
		Timestamp start = MathUtil.parse2Time(startTime);
		Timestamp end = MathUtil.parse2Time(endTime);
		int logsourceId = logSourceService.getByLogSourceName(logsrcName).getLogSourceId();
		if (reportService.checkReportByTime(logsourceId, start, end)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.REPORT_EXSIT);
			return ret;
		}
		Report report = new Report();
		report.setProjectId(Integer.parseInt(projectid));
		report.setLogSourceId(logsourceId);
		report.setStartTime(start);
		report.setEndTime(end);
		report.setCreatorId(1);
		report.setTitle("default");
		report.setComment("default");
		int result = reportService.createReport(report);
		if (result == 0) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.INNER_ERROR);
			return ret;
		} else {
			model.addFlashAttribute("status", 0);
			model.addFlashAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);
			return ret;
		}

	}
}
