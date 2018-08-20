package com.github.cxt.MyJavaAgent.test;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import org.junit.BeforeClass;


public class Test {

	
//	static class ProxyURLStreamHandlerFactory implements URLStreamHandlerFactory {
//
//		@Override
//		public URLStreamHandler createURLStreamHandler(String protocol) {
//			System.out.println("---------------protocol:" + protocol);
//			return null;
//		}
//	}
//	
//	@BeforeClass
//	public static void before(){
//		URL.setURLStreamHandlerFactory(new ProxyURLStreamHandlerFactory());
//	}
		
	
	@org.junit.Test
	public void test() throws Exception{
//		WeakHashMap<A, B> map = new WeakHashMap<>();
//		A a = new A(1);
//		B b = new B(11);
//		map.put(a, b);
//		a = new A(1);
//		System.out.println(map.get(a));
//		 System.gc();  
//		 System.out.println(map.get(a));
//		System.out.println(map.get(a));
//		map.put(new A(2), new B(22));
//		System.out.println(map);
//	      System.gc();  
//	        System.runFinalization();  
//	        //再次输出w
//	        System.out.println(map); 
//	        a = null;
//	        System.gc();  
//	        System.runFinalization();  
//	        System.out.println(map); 
				
		
		
//		for(int i = 0; i < 2; i++){
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					local.set(1);
//					local.set(1);
//					System.out.println("!");
//					
//				}
//			}).start();
//		}
//			Thread.sleep(50000 *10);
//		System.out.println(String.format("%2$s %1$s", "a", "b"));
//		
		MyClassLoad loader = new MyClassLoad();
        Thread.currentThread().setContextClassLoader(loader);
		
        Class<?> c = loader.loadClass("com.github.cxt.MyJavaAgent.test.Main");
        
        
        Method m = c.getMethod("main", java.lang.String[].class);
        m.invoke(null, (Object)new String[]{});
	}
}


class A {
	
	private Integer n;

	public A(Integer n) {
		super();
		this.n = n;
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("aaaaaaaaa:" + n);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		A other = (A) obj;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		return true;
	}
	
}


class  B {
	private Integer n;

	public B(Integer n) {
		super();
		this.n = n;
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("bbbbbbbb:" + n);
	}
}