package com.github.cxt.MySpring.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.beans.factory.FactoryBean;
import io.netty.util.internal.TypeParameterMatcher;


public class DemoFactoryBean implements FactoryBean<DemoFactory>{

	@Override
	public DemoFactory getObject() throws Exception {
		return new DemoFactory();
	}

	@Override
	public Class<?> getObjectType() {
		return DemoFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		Type[] types = DemoFactoryBean.class.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        System.out.println(type);
        
        TypeParameterMatcher matcher = null;
        
        C c = new C();
        matcher = TypeParameterMatcher.find(c, A.class, "I");
        System.out.println(getClass(matcher));
        
        
        A<String> a = new A<String>() {};
		matcher = TypeParameterMatcher.find(a, A.class, "I");
        System.out.println(getClass(matcher));
	}
	
	private static Class getClass(TypeParameterMatcher matcher) throws Exception {
		Field field= matcher.getClass().getDeclaredField("type");
        //我们这里是操作私有属性，所以是用setAccessible方法跳过私有检测
        field.setAccessible(true);
        return(Class) field.get(matcher);
	}
}

abstract class A<I> {
	
}
abstract class B<I> extends A<I>{
	
}
class C extends B<String>{
	
}