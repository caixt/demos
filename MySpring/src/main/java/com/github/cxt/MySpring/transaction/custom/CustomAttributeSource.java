package com.github.cxt.MySpring.transaction.custom;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;

public class CustomAttributeSource {

	private final static AnnotationCustomAttributeSource NULL_TRANSACTION_ATTRIBUTE = new AnnotationCustomAttributeSource();

	private final Map<Object, AnnotationCustomAttributeSource> attributeCache =
			new ConcurrentHashMap<Object, AnnotationCustomAttributeSource>(1024);

	public AnnotationCustomAttributeSource getAttribute(Method method, Class<?> targetClass) {
		if (method.getDeclaringClass() == Object.class) {
			return null;
		}

		// First, see if we have a cached value.
		Object cacheKey = getCacheKey(method, targetClass);
		Object cached = this.attributeCache.get(cacheKey);
		if (cached != null) {
			// Value will either be canonical value indicating there is no transaction attribute,
			// or an actual transaction attribute.
			if (cached == NULL_TRANSACTION_ATTRIBUTE) {
				return null;
			}
			else {
				return (AnnotationCustomAttributeSource) cached;
			}
		}
		else {
			// We need to work it out.
			AnnotationCustomAttributeSource txAttr = computeAttribute(method, targetClass);
			// Put it in the cache.
			if (txAttr == null) {
				this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
			}
			else {
				this.attributeCache.put(cacheKey, txAttr);
			}
			return txAttr;
		}
	}

	protected Object getCacheKey(Method method, Class<?> targetClass) {
		return new MethodClassKey(method, targetClass);
	}

	protected AnnotationCustomAttributeSource computeAttribute(Method method, Class<?> targetClass) {
		// Don't allow no-public methods as required.
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return null;
		}

		// Ignore CGLIB subclasses - introspect the actual user class.
		Class<?> userClass = ClassUtils.getUserClass(targetClass);
		// The method may be on an interface, but we need attributes from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
		// If we are dealing with method with generic parameters, find the original method.
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

		// First try is the method in the target class.
		AnnotationCustomAttributeSource txAttr = findAttribute(specificMethod);
		if (txAttr != null) {
			return txAttr;
		}

		// Second try is the transaction attribute on the target class.
		txAttr = findAttribute(specificMethod.getDeclaringClass());
		if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
			return txAttr;
		}

		if (specificMethod != method) {
			// Fallback is to look at the original method.
			txAttr = findAttribute(method);
			if (txAttr != null) {
				return txAttr;
			}
			// Last fallback is the class of the original method.
			txAttr = findAttribute(method.getDeclaringClass());
			if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
				return txAttr;
			}
		}
		return null;
	}

	protected AnnotationCustomAttributeSource findAttribute(Method method){
		return determineAttribute(method);
	}

	protected AnnotationCustomAttributeSource findAttribute(Class<?> clazz){
		return determineAttribute(clazz);
	}


	protected boolean allowPublicMethodsOnly() {
		return false;
	}
	
	private static CustomAnnotationParser parser = new CustomAnnotationParser();
	
	protected AnnotationCustomAttributeSource determineAttribute(AnnotatedElement ae) {
		if (ae.getAnnotations().length > 0) {
			AnnotationCustomAttributeSource attr = parser.parseAnnotation(ae);
			if (attr != null) {
				return attr;
			}
		}
		return null;
	}

}
