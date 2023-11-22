package com.github.cxt.springboot2.controller;

import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson2.JSONObject;
import com.github.cxt.springboot2.entities.Subscriber;


@Controller
public class FormController {
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String loadFormPage(Model m) {
		m.addAttribute("subscriber", new Subscriber());
		return "formPage";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	public String submitForm(@Valid Subscriber subscriber, BindingResult result, Model m) {
		if(result.hasErrors()) {
			return "formPage";
		}
		m.addAttribute("message", "Successfully saved person: " + subscriber.toString());
		return "formPage";
	}
	
	@RequestMapping(value="/valid", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject json(@RequestBody @Valid Subscriber subscriber) {
		JSONObject json = new JSONObject();
		System.out.println(subscriber);
	    return json;
	}
}
