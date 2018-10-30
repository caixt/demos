package com.github.cxt.springmvc.controller;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.github.cxt.springmvc.entity.User;
import com.github.cxt.springmvc.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


@RestController
@RequestMapping(value="/rest")
public class RestDemoController {
	
	@Autowired
	private UserMapper userMapper;
	
    @GetMapping
    public List<User> query( @RequestParam(name="pageNum", defaultValue="1") int pageNum,  @RequestParam(name="pageSize", defaultValue="2") int pageSize) {
    	PageHelper.startPage(pageNum, pageSize);
    	Page<User> users = userMapper.selectPage();
    	return users;
    }
	
    @GetMapping(value = "/{id}")
    public User get(@PathVariable("id") String id, @RequestParam(name="apikey", required=false) String apikey) {
    	return userMapper.selectByUserPrimaryKey(id);
    }
    
    //{"name":"aaa","type":{"id":1},"info":{},"classes":["aaa"]}
    @PostMapping
    @JsonView(User.IdView.class)
    public User post(@Valid @RequestBody User user) {
    	user.setId(UUID.randomUUID().toString());
    	userMapper.insert(user);
        return user;
    }
    
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") String id) {
    	userMapper.deleteByPrimaryKey(id);
    }
    
    
    @PutMapping(value = "/{id}")
    public void put(@PathVariable("id") String id,@Valid @RequestBody User user) {
    	user.setId(id);
    	userMapper.updateByPrimaryKey(user);
    }
}
