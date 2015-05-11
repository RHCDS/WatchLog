package com.netease.qa.log.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.user.service.LogsourceService;


@Controller
@RequestMapping("/api/logsource")
public class LogSourceController {

	@Resource
	private LogsourceService logsourceService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addLogsource(@RequestParam("projectid") int projectid,@RequestParam("host") String hostname,
			@RequestParam("path") String path, @RequestParam("filePattern") String filePattern,
			@RequestParam("start") String linestart, @RequestParam("keyword") String filterkeyword,
			@RequestParam("regex") String typeregex, Model model)
	      {
		//获取post提交的数据
	    int logsourceId = logsourceService.addLogsource(projectid, hostname, path, filePattern, linestart, filterkeyword, typeregex);
		model.addAttribute("addLogSourceId", logsourceId);
		return "addLogsource";
	    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateLogsource(@RequestParam("logsourceid") int logsourceid,@RequestParam("host") String hostname,
			@RequestParam("path") String path, @RequestParam("filePattern") String filePattern,
			@RequestParam("start") String linestart, @RequestParam("keyword") String filterkeyword,
			@RequestParam("regex") String typeregex, Model model){
		int update = logsourceService.updateLogsource(logsourceid, hostname, path, filePattern, linestart, filterkeyword, typeregex);
		model.addAttribute("updateLogsource", update);
		return "addLogsource";
	}
	
	@RequestMapping(value = "/find/{logsourceid}", method = RequestMethod.GET)
	public String findLogSource(@PathVariable int logsourceid, Model model){
		LogSource logSource = logsourceService.findLogsource(logsourceid);
		model.addAttribute("logsource", logSource);
		return "logSource";	
	}
	
	@RequestMapping(value = "/delete/{logsourceid}", method = RequestMethod.POST)
	public String deleteLogSource(@PathVariable int logsourceid,Model model){
		int delete = logsourceService.deleteLogsource(logsourceid);
		model.addAttribute("delete", delete);
		return "addLogsource";
		
	}
}
