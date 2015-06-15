package com.netease.qa.log.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.service.ReportService;
import com.netease.qa.log.util.MathUtil;

@Controller
public class WlogRTController {

	@Resource
	private LogSourceService logSourceService;
	@Resource
	private ReadService readService;
	

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
			JSONObject result = readService.queryTimeRecords(Integer.parseInt(logsrcid), startTime, endTime, "sample_time", "desc", 15, 0);
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

	
}
