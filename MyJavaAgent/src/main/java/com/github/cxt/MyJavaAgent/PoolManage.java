package com.github.cxt.MyJavaAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;


public class PoolManage {
	
	private static final AtomicInteger ID = new AtomicInteger();
	
	private static ClassLoader EXTCLASSLOAD;
	
	private static ClassLoader APPCLASSLOAD;
	
	private static ClassPool ROOT;
	
	private static Map<ClassLoader, NamedClassPool> CLASSPOOLMAP;
	
	static{
		APPCLASSLOAD = ClassLoader.getSystemClassLoader();
		EXTCLASSLOAD = APPCLASSLOAD.getParent();
		CLASSPOOLMAP = new HashMap<>();
		
		ROOT = new NamedClassPool("root");
		ROOT.appendSystemPath();
	}
	
	
	private static synchronized ClassPool createGetPool(ClassLoader classLoader){
		if(CLASSPOOLMAP.containsKey(classLoader)){
			return CLASSPOOLMAP.get(classLoader);
		}
		
		String classLoaderName = classLoader.toString();
        NamedClassPool newClassPool = new NamedClassPool(ROOT, classLoaderName + "-" + getNextId());
        newClassPool.childFirstLookup = true;

        final ClassPath classPath = new LoaderClassPath(classLoader);
        newClassPool.appendClassPath(classPath);
        
        CLASSPOOLMAP.put(classLoader, newClassPool);
        return newClassPool;
	}
	
	public static ClassPool getPool(ClassLoader classLoader){
		if(classLoader == null || classLoader == EXTCLASSLOAD || classLoader == APPCLASSLOAD){
			return ROOT;
		}
		return createGetPool(classLoader);
	}

	
    private static int getNextId() {
        return ID.getAndIncrement();
    }
}
