package com.netease.qa.log.web.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

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
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Report;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.service.ReportService;
import com.netease.qa.log.util.ConstCN;
import com.netease.qa.log.util.MathUtil;

@Controller
public class WlogPMController {

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

	// 日志聚合分析，选择日志源内容
	@RequestMapping(value = "logsrc/pm_analyse", method = RequestMethod.GET)
	public String pm_analyse(@RequestParam(value = "proj", required = false) String projectid, Model model) {
		model.addAttribute("controller", "WlogPM");
		model.addAttribute("action", "pm_analyse");
		ArrayList<String> logs = new ArrayList<String>();
		if (projectid != null) {
			ArrayList<LogSource> logsources = logSourceService.selectAllByProjectId(Integer.parseInt(projectid));
			for (int i = 0; i < logsources.size(); i++) {
				logs.add(logsources.get(i).getLogSourceId() + "#" + logsources.get(i).getLogSourceName());
			}
		}
		model.addAttribute("logs", logs);
		return "logsrc/pm_analyse";
	}

	// 历史报告表格内容
	@RequestMapping(value = "logsrc/pm_analyse/pmtable", method = RequestMethod.GET)
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

	// 删除历史报告
	@RequestMapping(value = "/logsrc/pm_analyse/destroy", method = RequestMethod.POST)
	public String deleteReport(@RequestParam(value = "proj") String projectid,
			@RequestParam(value = "report_id") String reportid, RedirectAttributes model) {
		// 成功和失败的重定向url
		String ret = "redirect:/logsrc/pm_analyse?proj=" + projectid;

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

	@RequestMapping(value = "/logsrc/pm_analyse/****", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addReport(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "log_id", required = false) String logsrcId,
			@RequestParam(value = "start_time", required = false) String startTime,
			@RequestParam(value = "end_time", required = false) String endTime, RedirectAttributes model) {
		
		
		String ret = "redirect:/logsrc/pm_analyse_unsave?log_id=" + logsrcId + "&proj=" + projectid;
		JSONObject json = new JSONObject();
		if (MathUtil.isEmpty(projectid, logsrcId, startTime, endTime)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			json.put("message", ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
		if (!MathUtil.isInteger(projectid)) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.ID_MUST_BE_NUM);
			json.put("message", ConstCN.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
		// 是否需要判断report插入，重复
		Timestamp start = MathUtil.parse2Time(startTime);
		Timestamp end = MathUtil.parse2Time(endTime);
		Report report = new Report();
		report.setProjectId(Integer.parseInt(projectid));
		report.setLogSourceId(Integer.parseInt(logsrcId));
		report.setStartTime(start);
		report.setEndTime(end);
		report.setCreatorId(1);
		report.setTitle("default");
		report.setComment("default");
		int result = reportService.createReport(report);
		if (result == 0) {
			model.addFlashAttribute("status", -1);
			model.addFlashAttribute("message", ConstCN.INNER_ERROR);
			json.put("message", ConstCN.INNER_ERROR);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		} else {
			model.addFlashAttribute("status", 0);
			model.addFlashAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);
			json.put("message", ConstCN.RESPONSE_SUCCESSFUL);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/logsrc/pm_analyse_unsave", method = RequestMethod.GET)
	public String viewReport(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "log_id", required = false) String logsrcId,
			@RequestParam(value = "start_time", required = false) String startTime,
			@RequestParam(value = "end_time", required = false) String endTime, Model model) {
		model.addAttribute("controller", "WlogPM");
		model.addAttribute("action", "pm_analyse_unsave");
		if (MathUtil.isEmpty(projectid, logsrcId, startTime, endTime)) {
			model.addAttribute("status", -1);
			model.addAttribute("message", ConstCN.NULL_PARAM);		
			return "logsrc/pm_analyse_unsave";
		}
		model.addAttribute("log_id", Integer.parseInt(logsrcId));
		model.addAttribute("start_time", startTime);
		model.addAttribute("end_time", endTime);
		if (!MathUtil.isInteger(projectid)) {
			model.addAttribute("status", -1);
			model.addAttribute("message", ConstCN.ID_MUST_BE_NUM);					
			return "logsrc/pm_analyse_unsave";
		}
		LogSource logSource = logSourceService.getByLogSourceId(Integer.parseInt(logsrcId));
		model.addAttribute("logsrc_name", logSource.getLogSourceName());
		model.addAttribute("host_name", logSource.getHostname());
		model.addAttribute("logsrc_path", logSource.getPath());
		model.addAttribute("logsrc_file", logSource.getFilePattern());
		model.addAttribute("start_regex", logSource.getLineStartRegex());
		model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
		model.addAttribute("reg_regex", logSource.getLineTypeRegex());
		Long start = null;
		Long end = null;
		try {
			start = MathUtil.parse2Long(startTime);
			end = MathUtil.parse2Long(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject resultByTime = readService.queryTimeRecords(Integer.parseInt(logsrcId), start, end, "sample_time",
				"desc", 10, 0);
		model.addAttribute("pm_error_dist_table", resultByTime.getJSONArray("record"));
		System.out.println("pm_error_dist_table:" + resultByTime.getJSONArray("record"));
		JSONObject resultByError = readService.queryErrorRecordsWithTimeDetail(Integer.parseInt(logsrcId), start, end,
				"sample_time", "desc", 5, 0);
		model.addAttribute("pm_error_type_table", resultByError.getJSONArray("error"));
		System.out.println("pm_error_type_table:" + resultByError.getJSONArray("error").toString());
		model.addAttribute("status", 0);
		model.addAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);		
		return "logsrc/pm_analyse_unsave";
	}
	
	@RequestMapping(value = "/logsrc/pm_analyse_saved", method = RequestMethod.GET)
	public String getReport(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "report_id", required = false) String reportid,Model model){
		Report report = reportService.getReportById(Integer.parseInt(reportid));
		model.addAttribute("controller", "WlogPM");
		model.addAttribute("action", "pm_analyse_unsave");
		model.addAttribute("report_id", Integer.parseInt(reportid));
		String startTime = MathUtil.parse2Str(report.getStartTime());
		String endTime = MathUtil.parse2Str(report.getEndTime());
		model.addAttribute("start_time", startTime);
		model.addAttribute("end_time", endTime);
		LogSource logSource = logSourceService.getByLogSourceId(report.getLogSourceId());
		model.addAttribute("logsrc_name", logSource.getLogSourceName());
		model.addAttribute("host_name", logSource.getHostname());
		model.addAttribute("logsrc_path", logSource.getPath());
		model.addAttribute("logsrc_file", logSource.getFilePattern());
		model.addAttribute("start_regex", logSource.getLineStartRegex());
		model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
		model.addAttribute("reg_regex", logSource.getLineTypeRegex());
		Long start = null;
		Long end = null;
		try {
			start = MathUtil.parse2Long(startTime);
			end = MathUtil.parse2Long(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject resultByTime = readService.queryTimeRecords(report.getLogSourceId(), start, end, "sample_time",
				"desc", 10, 0);
		model.addAttribute("pm_error_dist_table", resultByTime.getJSONArray("record"));
		System.out.println("pm_error_dist_table:" + resultByTime.getJSONArray("record"));
		JSONObject resultByError = readService.queryErrorRecordsWithTimeDetail(report.getLogSourceId(), start, end,
				"sample_time", "desc", 5, 0);
		model.addAttribute("pm_error_type_table", resultByError.getJSONArray("error"));
		System.out.println("pm_error_type_table:" + resultByError.getJSONArray("error").toString());
		return "logsrc/pm_analyse_saved";
	}

}
