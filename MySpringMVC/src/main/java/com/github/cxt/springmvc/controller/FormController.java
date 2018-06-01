package com.github.cxt.springmvc.controller;

import java.io.UnsupportedEncodingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.cxt.springmvc.form.Subscriber;
import com.github.cxt.springmvc.springconfig.ISessionBean;

@Controller
public class FormController {
	@Autowired
	private ISessionBean sessionBean;
	@Value("${xxx}")
	private String xx;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loadFormPage(Model m) {
		sessionBean.printId();
		
		m.addAttribute("subscriber", new Subscriber());
		System.out.println(xx);
		return "formPage";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String submitForm(@Valid Subscriber subscriber, BindingResult result, Model m) {
		if(result.hasErrors()) {
			return "formPage";
		}
		
		m.addAttribute("message", "Successfully saved person: " + subscriber.toString());
		return "formPage";
	}
	
	
	@RequestMapping(value="/json", method=RequestMethod.POST)
	@ResponseBody
	public Object test_test(@RequestBody @Valid Subscriber subscriber, BindingResult result) throws UnsupportedEncodingException {
		if(result.hasErrors()) {
			return "{\"result\":\"" + result.toString() + "\"}";
		}
		System.out.println(subscriber.toString());
	    return "{\"result\":\"" + subscriber.toString() + "\"}";

	}
}
