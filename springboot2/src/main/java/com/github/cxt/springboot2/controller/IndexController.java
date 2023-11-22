package com.github.cxt.springboot2.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cxt.springboot2.config.ApiJsonObject;
import com.github.cxt.springboot2.config.ApiJsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Api(tags = "测试接口")
@RestController
@RequestMapping("test")
public class IndexController {
	
	
	@GetMapping("/hello")
	@ApiOperation(value = "hello")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "key", value = "关键字", required = false, dataType = "String")})
	public String hello(String key) {
		return "hello:" + key;
	}
	
	@PostMapping("/div")
	public Integer div(
			@ApiJsonObject(name = "map_model", value = {
					@ApiJsonProperty(key = "a", example = "1", description = "dividend"),//, type = "int"),
					@ApiJsonProperty(key = "b", example = "2", description = "divisor")//, type = "int")
			}) @RequestBody Map<String, Integer> map) {
		int a = map.get("a");
		int b = map.get("b");
		return a / b;
	}

}
