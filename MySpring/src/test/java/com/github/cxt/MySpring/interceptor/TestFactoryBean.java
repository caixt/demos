package com.github.cxt.MySpring.interceptor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


public class TestFactoryBean<T> implements InitializingBean, FactoryBean<T> {
	private Holder holder;

	private Class<T> customInterface;

	public TestFactoryBean(Class<T> customInterface) {
		this.customInterface = customInterface;
	}

	private boolean addToConfig;

	public boolean isAddToConfig() {
		return addToConfig;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(holder == null){
			throw new IllegalArgumentException("not find holder");
		}
	}

	@Override
	public T getObject() throws Exception {
		return holder.getTarget(customInterface);
	}

	@Override
	public Class<?> getObjectType() {
		return this.customInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setHolder(Holder holder) {
		this.holder = holder;
	}

}
