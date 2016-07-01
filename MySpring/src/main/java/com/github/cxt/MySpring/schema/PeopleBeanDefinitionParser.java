package com.github.cxt.MySpring.schema;

import org.w3c.dom.Element;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.util.StringUtils;

public class PeopleBeanDefinitionParser extends AbstractBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext parserContext) {
		BeanRegistrator beanRegistrator = new BeanRegistrator(parserContext);
		beanRegistrator.CLASS(WorkManager.class).register();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(People.class);
		
		
		new ConfValue().NAME("age").DEFAULT(100).register(element, parserContext);
		
		String name = element.getAttribute("name");
		String age = element.getAttribute("age");
		String id = element.getAttribute("id");
		
		if (StringUtils.hasText(id)) {
			builder.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(name)) {
			builder.addPropertyValue("name", name);
		}
		if (StringUtils.hasText(age)) {
			builder.addPropertyValue("age", Integer.valueOf(age));
		}
		
		
		new XmlBeanDefinitionReader(parserContext.getRegistry())
			.loadBeanDefinitions("com/github/cxt/MySpring/schema/people-context.xml");
		
		return builder.getBeanDefinition();
	}

	@Override
	protected boolean shouldGenerateId() {
		return false;
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}
}