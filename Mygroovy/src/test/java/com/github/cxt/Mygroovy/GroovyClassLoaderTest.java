package com.github.cxt.Mygroovy;


import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class GroovyClassLoaderTest {

	@Test
	public void testGroovyCode() throws InstantiationException, IllegalAccessException {
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
		try {
			Class<?> groovyClass = groovyClassLoader.parseClass(
					"package com.github.cxt.Mygroovy; class SayImpl implements Say {public String sayHello(String name) {return name + \",hello\";}}");
			Say say = (Say) groovyClass.newInstance();
			System.out.println(say.sayHello("world1"));
		} finally {
			if (groovyClassLoader != null) {
				try {
					groovyClassLoader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testGroovyFile() throws URISyntaxException, IOException, InstantiationException, IllegalAccessException {
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
		try {
			File file = new File(GroovyClassLoaderTest.class.getResource("SayImpl.groovy").toURI());
			//接口
			Class<?> groovyClass = groovyClassLoader.parseClass(file);
			Say say = (Say) groovyClass.newInstance();
			System.out.println(say.sayHello("world2"));
		}  finally {
			if (groovyClassLoader != null) {
				try {
					groovyClassLoader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testGroovyJarFile() throws URISyntaxException, IOException, InstantiationException, IllegalAccessException {
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
		try {
			groovyClassLoader.addClasspath("jar/myGroovy.jar");
			File file = new File(GroovyClassLoaderTest.class.getResource("jar/SayImpl.groovy").toURI());
			//接口
			Class<?> groovyClass = groovyClassLoader.parseClass(file);
			GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
			String ret = (String) groovyObject.invokeMethod("sayHello", "world3");
			System.out.println(ret);
		}  finally {
			if (groovyClassLoader != null) {
				try {
					groovyClassLoader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
