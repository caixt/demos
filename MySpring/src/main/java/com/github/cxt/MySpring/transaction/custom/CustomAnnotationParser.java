package com.github.cxt.MySpring.transaction.custom;

import java.lang.reflect.AnnotatedElement;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;


public class CustomAnnotationParser {

	public AnnotationCustomAttributeSource parseAnnotation(AnnotatedElement ae) {
		AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, Custom.class);
		if (attributes != null) {
			return parseAnnotation(attributes);
		}
		else {
			return null;
		}
	}

	public AnnotationCustomAttributeSource parseAnnotation(Custom ann) {
		return parseAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
	}

	protected AnnotationCustomAttributeSource parseAnnotation(AnnotationAttributes attributes) {
		AnnotationCustomAttributeSource attr = new AnnotationCustomAttributeSource();
		attr.setName(attributes.getString("name"));
		return attr;
	}

}
