package com.netease.qa.log.web.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;

@Controller
public class WlogPMController {
	
	@Resource
	private LogSourceService logSourceService;
	
	//日志聚合分析，选择日志源内容
	@RequestMapping(value = "logsrc/pm_analyse", method = RequestMethod.GET)
	public String pm_analyse(@RequestParam(value = "proj", required = false) String projectid, Model model) {
		model.addAttribute("controller", "WlogPM" );		
		model.addAttribute("action", "pm_analyse" );
		ArrayList<LogSource> logsources = logSourceService.selectAllByProjectId(Integer.parseInt(projectid));
		ArrayList<String> logs = new ArrayList<String>();
		for (int i = 0; i < logsources.size(); i++) {
			logs.add(logsources.get(i).getLogSourceId() + "#" + logsources.get(i).getLogSourceName());
		}
		model.addAttribute("logs", logs);
		return "logsrc/pm_analyse";
	}
}
