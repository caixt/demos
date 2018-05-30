package com.github.cxt.springmvc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.cxt.springmvc.form.Subscriber;


@RestController
@RequestMapping(value="/rest")
public class RestDemoController {
	
    @GetMapping
    @JsonView(Subscriber.TestView.class)
    public List<Subscriber> query() {
    	List<Subscriber> subs = new ArrayList<>();
    	Subscriber sub = null;
    	sub = new Subscriber();
    	sub.setId("1");
    	sub.setName("name1");
    	sub.setEmail("email1");
    	sub.setAge(29);
    	subs.add(sub);
    	sub = new Subscriber();
    	sub.setId("2");
    	sub.setName("name2");
    	sub.setEmail("email2");
    	sub.setAge(29);
    	subs.add(sub);
    	return subs;
    }
	
    @GetMapping(value = "/{id}")
    public Subscriber get(@PathVariable("id") String id, @RequestParam(name="apikey", required=false) String apikey) {
    	Subscriber sub = new Subscriber();
    	sub.setId(id);
    	sub.setBirthday(new Date());
    	sub.setEmail("email");
    	sub.setAge(29);
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
