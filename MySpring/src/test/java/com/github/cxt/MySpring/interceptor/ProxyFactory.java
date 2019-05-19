package com.github.cxt.MySpring.interceptor;


public class ProxyFactory<T> {
	private final Class<T> customInterface;
	
	public ProxyFactory(Class<T> customInterface) {
		this.customInterface = customInterface;
	}

	  @SuppressWarnings("unchecked")
	  protected T newInstance(Proxy<T> proxy) {
	    return (T) java.lang.reflect.Proxy.newProxyInstance(customInterface.getClassLoader(), new Class[] { customInterface }, proxy);
	  }

	  public T newInstance() {
	    Proxy<T> proxy = new Proxy<T>();
	    return newInstance(proxy);
	  }
}
