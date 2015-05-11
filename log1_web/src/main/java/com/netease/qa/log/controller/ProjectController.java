package com.netease.qa.log.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netease.qa.log.user.service.ProjectService;

@Controller
@RequestMapping(value = "api/project")
public class ProjectController {

	@Resource
	private ProjectService projectService;
	
}
