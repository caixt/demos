package com.github.cxt.MySpring.base;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionIfAbsent implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		if(context.getBeanFactory().getBeanNamesForType(DemoBean.class).length == 0){
			return true;
		}
		return false;
	}

}
