
package com.example.dubbo.service;

import com.example.dubbo.entity.User;

public interface UserService {
    User getByUserId(Integer userId);
}