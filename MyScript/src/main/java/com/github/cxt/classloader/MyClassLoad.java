package com.github.cxt.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class MyClassLoad extends URLClassLoader {

	
	public MyClassLoad(URL[] urls, ClassLoader parent){
		super(urls, parent);
		
	}
}
