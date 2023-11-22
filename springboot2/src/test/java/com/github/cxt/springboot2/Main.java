package com.github.cxt.springboot2;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cxt.springboot2.entities.User;


public class Main {

	//https://blog.csdn.net/weixin_37657245/article/details/108348078
	@Test
	public void test() throws Exception {
//		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("1990-05-19");
		
		Locale loc = Locale.CHINA;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", loc);
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
//        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        df.setTimeZone(tz);
        
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", loc);
        
        System.out.println(df.format(date));
        
        User demo = new User();
        demo.setBirthday(date);
        StringWriter sw = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(sw, demo);
        System.out.println(sw);
	}
}
