package com.github.cxt.MyJavaAgent.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.cxt.MyJavaAgent.ClassAssemble;
import com.github.cxt.MyJavaAgent.LogTraceConfig;

public class MyClassLoad extends ClassLoader {
	
	private ClassAssemble classAssemble = new ClassAssemble(new LogTraceConfig(null));
	
	private String NAMESTART = MyClassLoad.class.getPackage().getName();
	
	public MyClassLoad(){
		super();
	}
	
	public MyClassLoad(ClassLoader parent){
		super(parent);
	}
	
	
	
	@Override	 
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try{
			byte[] b = getClassBytes(name);
			if(b == null){
				throw new ClassNotFoundException(name);
			}
//			FileUtils.writeByteArrayToFile(new File(name.substring(name.lastIndexOf(".") + 1) + ".class"), b);
			return defineClass(name, b, 0, b.length );
		} catch(IOException e){
			throw new ClassNotFoundException(name);
		}
    }
 
    private byte[] getClassBytes(String className) throws IOException {
    	if(!className.startsWith(NAMESTART)){
    		return null;
    	}
        String path = "target" + File.separator + "test-classes" + File.separator + className.replace('.', File.separatorChar) + ".class";
        FileInputStream inputStream = new FileInputStream(new File(path));
        try{
        	return classAssemble.assembleClass(className, inputStream);
        }finally{
        	try{
        		inputStream.close();
        	}catch(IOException ignore){}
        }
    }

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if(!name.startsWith(NAMESTART)){
			return super.loadClass(name, resolve);
		}
		Class<?> c = findClass(name);
		if(resolve){
			resolveClass(c);
		}
		return c;
	}
}
