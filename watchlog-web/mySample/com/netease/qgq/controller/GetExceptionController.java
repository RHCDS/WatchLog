package com.netease.qgq.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netease.qgq.service.GetException;
import com.netease.qa.log.meta.Exception;

@Controller
@RequestMapping("/exception")
public class GetExceptionController {

	@Resource
	private GetException getException;
	@RequestMapping(value = "/getException/{id}")
	public String getException(@PathVariable String id, Model model){
		//获取url传入的id
		//int exceptionId = Integer.parseInt(request.getParameter("id"));
		int exceptionId = Integer.parseInt(id);
		System.out.println("exceptionId:"+exceptionId);
		Exception exception =  this.getException.getException(exceptionId);
		model.addAttribute("exception", exception);
		return "showException";
		
	}
}
