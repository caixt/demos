package com.github.cxt.springboot2.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistLock {
	
	public static String PATH = "'/lock/' + #_class + '.' + #_method";

    String value() default PATH;

    long waitMillis() default -1;
}