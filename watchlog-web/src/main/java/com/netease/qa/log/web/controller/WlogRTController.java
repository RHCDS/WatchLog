package com.netease.qa.log.web.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WlogRTController {

	@RequestMapping(value = "logsrc/rt_analyse", method = RequestMethod.GET)
	public String rt_analyse(Locale locale, Model model) {
		model.addAttribute("controller", "WlogRT" );		
		model.addAttribute("action", "rt_analyse" );				
		return "logsrc/rt_analyse";
	}
}
