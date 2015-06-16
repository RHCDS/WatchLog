package com.netease.qa.log.web.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.util.MathUtil;

@Controller
public class WlogRTController {

	@Resource
	private LogSourceService logSourceService;
	@Resource
	private ReadService readService;

	@RequestMapping(value = "logsrc/rt_analyse", method = RequestMethod.GET)
	// 右边小侧栏的日志源详细信息
	public String rt_analyse(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "log_id", required = false, defaultValue = "0") String logsrcid, Model model) {
		model.addAttribute("controller", "WlogRT");
		model.addAttribute("action", "rt_analyse");
		ArrayList<String> logs = new ArrayList<String>();
		ArrayList<LogSource> logsources = new ArrayList<LogSource>();
		if (projectid != null) {
			logsources = logSourceService.selectAllByProjectId(Integer.parseInt(projectid));
			for (int i = 0; i < logsources.size(); i++) {
				logs.add(logsources.get(i).getLogSourceId() + "#" + logsources.get(i).getLogSourceName());
			}
		}
		model.addAttribute("logs", logs);
		if (logsources.size() == 0)
			return "";
		// 没有传logsrcid,默认第一个
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String time = MathUtil.parse2Str(currentTime);
		long current = 0;
		try {
			current = MathUtil.parse2Long(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject result = new JSONObject(); 
		if (Integer.parseInt(logsrcid) == 0) {
			LogSource logSource = logsources.get(0);
			model.addAttribute("logsrc_name", logSource.getLogSourceName());
			model.addAttribute("host_name", logSource.getHostname());
			model.addAttribute("logsrc_path", logSource.getPath());
			model.addAttribute("logsrc_file", logSource.getFilePattern());
			model.addAttribute("start_regex", logSource.getLineStartRegex());
			model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
			model.addAttribute("reg_regex", logSource.getLineTypeRegex());
			result = readService.queryLatestTimeRecords(logSource.getLogSourceId(), current);
		} else {
			LogSource logSource = logSourceService.getByLogSourceId(Integer.parseInt(logsrcid));
			model.addAttribute("logsrc_name", logSource.getLogSourceName());
			model.addAttribute("host_name", logSource.getHostname());
			model.addAttribute("logsrc_path", logSource.getPath());
			model.addAttribute("logsrc_file", logSource.getFilePattern());
			model.addAttribute("start_regex", logSource.getLineStartRegex());
			model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
			model.addAttribute("reg_regex", logSource.getLineTypeRegex());
			result = readService.queryLatestTimeRecords(logSource.getLogSourceId(), current);
		}
		model.addAttribute("rt_table", result.getJSONArray("record"));
		System.out.println("record:" + result.getJSONArray("record"));
		return "logsrc/rt_analyse";
	}

}
