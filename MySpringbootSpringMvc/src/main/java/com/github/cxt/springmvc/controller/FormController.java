package com.github.cxt.springmvc.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.cxt.springmvc.form.Subscriber;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
public class FormController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loadFormPage(Model m) {
		m.addAttribute("subscriber", new Subscriber());
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
	public Object json(@RequestBody @Valid Subscriber subscriber, BindingResult result) throws UnsupportedEncodingException {
		if(result.hasErrors()) {
			return "{\"result\":\"" + result.toString() + "\"}";
		}
		System.out.println(subscriber.toString());
	    return "{\"result\":\"" + subscriber.toString() + "\"}";

	}
	
	@RequestMapping(value="/err", method=RequestMethod.GET)
	@ResponseBody
	public Object err() throws UnsupportedEncodingException {	
		double n = 100 / 0;
		return n;
	}
	
	
	@ApiOperation(value="根据名称获取实体", httpMethod="GET", notes="get user by id", response= Subscriber.class)  
	@RequestMapping(value="/swagger", method=RequestMethod.GET)
	@ResponseBody
	public Subscriber swagger(@ApiParam(required=true,value="用户名称",name="name")@RequestParam(value="name")String name) {
		Subscriber subscriber = new Subscriber();
		subscriber.setAge(1);
		subscriber.setBirthday(new Date());
		subscriber.setEmail("aaaa@email");
		subscriber.setName(name);
		subscriber.setReceiveNewsletter(true);
		return subscriber;
	}
}
