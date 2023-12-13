package com.example.dubbo.impl;

import java.util.Arrays;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import com.example.dubbo.entity.User;
import com.example.dubbo.service.UserService;

@DubboService
public class UserServiceImpl implements UserService {

    // 模拟数据
    private static final List<User> USERS = Arrays.asList(
            new User(1, "张三"),
            new User(2, "李四")
    );

    @Override
    public User getByUserId(Integer userId) {
        System.out.println(RpcContext.getContext().get("aaa"));
        System.out.println(RpcContext.getContext().getAttachment("aaa"));
        for (User user : USERS) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        throw new RuntimeException(userId + "");
    }
}
