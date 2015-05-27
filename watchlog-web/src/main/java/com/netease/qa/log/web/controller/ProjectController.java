package com.netease.qa.log.web.controller;
//package com.netease.qa.log.controller;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSONObject;
//import com.netease.qa.log.user.service.ProjectService;
//
//@Controller
//@RequestMapping(value = "api/project/")
//public class ProjectController {
//
//	@Resource
//	private ProjectService projectService;
//	
//	@RequestMapping(method = RequestMethod.POST)
//	public String addProject(@RequestParam String name, @RequestParam String name_eng, 
//			@RequestParam int accuracy, Model model){
//		int projectid = projectService.addProject(name, name_eng, accuracy);
//		model.addAttribute("newprojectid", projectid);
//		
//		return "addProject";
//	}
//	
//	@RequestMapping(value = "{projectid}", method = RequestMethod.POST)
//	public String updateProject(@PathVariable int projectid, @RequestParam String name, @RequestParam String name_eng,
//			@RequestParam int accuracy, Model model){
//		int updateOk = projectService.updateProject(projectid, name, name_eng, accuracy);
//		model.addAttribute("updateOk", updateOk);
//		return "addProject";
//	}
//	
//	@RequestMapping(value = "changestatus/{projectid}", method = RequestMethod.POST)
//	public String changeStatus(@PathVariable int projectid,@RequestParam int status
//			, Model model){
//		int changeOk = projectService.updateProjectStatus(projectid, status);
//		model.addAttribute("changeStatus", changeOk);
//		return "addProject";
//	}
//	
//	@RequestMapping(value = "{projectid}", method = RequestMethod.GET,
//			produces = {"application/json;charset=utf-8"})
//	public @ResponseBody JSONObject findProject(@PathVariable int projectid, Model model){
//		JSONObject project = projectService.findProject(projectid);
//		return project;
//	}
//}
//
//
