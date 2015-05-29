package com.netease.qa.log.web.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WlogPMController {
	@RequestMapping(value = "logsrc/pm_analyse", method = RequestMethod.GET)
	public String pm_analyse(Locale locale, Model model) {
		model.addAttribute("controller", "WlogPM" );		
		model.addAttribute("action", "pm_analyse" );				
		return "logsrc/pm_analyse";
	}
}
