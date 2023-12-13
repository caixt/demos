package com.example.dubbo.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.dubbo.entity.User;
import com.example.dubbo.service.UserService;


@RestController
public class UserController {

	@DubboReference
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getByUserId(@PathVariable("userId") Integer userId) {
        System.out.println("userId = " + userId);
        RpcContext.getContext().set("aaa", "bbb");
		RpcContext.getContext().setAttachment("aaa", "bbb");
        User user = userService.getByUserId(userId);
        System.out.println("user = " + user);
        return ResponseEntity.ok(user);
    }
    
}
