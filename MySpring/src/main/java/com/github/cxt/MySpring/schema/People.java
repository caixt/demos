package com.github.cxt.MySpring.schema;



public class People {
	
	private String test;
	
    private String id;  
    private String name;  
    private Integer age;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	@Override
	public String toString() {
		return "People [test=" + test + ", id=" + id + ", name=" + name + ", age=" + age + "]";
	}  
    
    
}  
