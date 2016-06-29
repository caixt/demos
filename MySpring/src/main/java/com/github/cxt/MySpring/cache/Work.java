package com.github.cxt.MySpring.cache;

public interface Work {
	
	public String readByCache(String code);
	
	public boolean update(String code);
	
	public String save(String code);
	
	public boolean cleanAll();

}
