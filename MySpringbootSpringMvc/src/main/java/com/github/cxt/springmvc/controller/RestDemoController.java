package com.github.cxt.springmvc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.cxt.springmvc.form.Subscriber;


@RestController
@RequestMapping(value="/rest")
public class RestDemoController {
	
    @GetMapping(value = "/{id}")
    public Subscriber get(@PathVariable("id") String id, @RequestParam(name="apikey", required=false) String apikey) {
    	Subscriber sub = new Subscriber();
    	sub.setId(id);
    	return sub;
    }
    
    //@Valid
    @PostMapping
    public Subscriber post(@RequestBody Subscriber subscriber) {
    	subscriber.setId("newId");
        System.out.println(subscriber);
        return subscriber;
    }
    
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") String id) {
    	System.out.println("delete:" + id);
    }
    
    
    @PutMapping(value = "/{id}")
    public void put(@PathVariable("id") String id, @RequestBody Subscriber subscriber) {
    	subscriber.setId(id);
    	System.out.println(subscriber);
    }

}
