package com.github.cxt.springboot2.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.cxt.springboot2.entities.User;


@RestController
@RequestMapping(value="/rest")
public class RestDemoController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @GetMapping(value = "/{id}")
    public User get(@PathVariable("id") String id) throws ParseException {
    	User user = new User();
    	user.setId(id);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("1990-05-19");
		user.setBirthday(date);
    	return user;
    }
    
    //{"name":"aaa","type":{"id":1},"info":{},"classes":["aaa"]}
    @PostMapping
    @JsonView(User.IdView.class)
    public User post(@Valid @RequestBody User user) {
    	user.setId(UUID.randomUUID().toString());
    	logger.info("user:{}", user);
        return user;
    }
    
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") String id) {
    	logger.info("id:{}", id);
    }
    
    
    @PutMapping(value = "/{id}")
    public void put(@PathVariable("id") String id,@Valid @RequestBody User user) {
    	user.setId(id);
    	logger.info("user:{}", user);
    }
}
