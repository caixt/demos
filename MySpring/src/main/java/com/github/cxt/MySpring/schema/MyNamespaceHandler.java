package com.github.cxt.MySpring.schema;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class MyNamespaceHandler extends NamespaceHandlerSupport {
	
	public static void main(String[] args) {
		System.out.println(PeopleBeanDefinitionParser.class.getName());
	}
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<String, Class<? extends BeanDefinitionParser>> parsers = new HashMap<String, Class<? extends BeanDefinitionParser>>(){

		private static final long serialVersionUID = 1L;

	{
		put("people", PeopleBeanDefinitionParser.class);
	}};
	
    
    
    @Override
	public void init() {
		if (logger.isInfoEnabled()) logger.info("Registering custom namespace handler");
		for (Map.Entry<String, Class<? extends BeanDefinitionParser>> entry : parsers.entrySet()) {
			try {
				//noinspection unchecked
				registerBeanDefinitionParser(entry.getKey(), entry.getValue().newInstance());
				if (logger.isDebugEnabled()) logger.debug("registered element <" + entry.getKey() + "> with parser " + entry.getValue());
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException("Failed to register element <" + entry.getKey() + ">", ex);
			}
		}
	}
}  
