package com.github.cxt.MySpring.validationI18n;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.ConstraintDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import com.github.cxt.MySpring.validation.Abc;
import com.github.cxt.MySpring.validation.B;

//SpringValidatorAdapter的参考实现
public class Main {
	
	private static final Set<String> internalAnnotationAttributes = new HashSet<String>(3);

	ResourceBundleMessageSource messageSource = null;
	
	static {
		internalAnnotationAttributes.add("message");
		internalAnnotationAttributes.add("groups");
		internalAnnotationAttributes.add("payload");
	}
	@Before
	public void before(){
		messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
	}

	@Test
	public void test(){
		Abc demo = new Abc();
		demo.setStr1("12345678");
		B b = new B();
		demo.setB(b);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
		Validator validator = factory.getValidator(); 
		Set<ConstraintViolation<Abc>> violations = validator.validate(demo); 
		
		Errors errors = new BeanPropertyBindingResult(demo, "test", true, 256);
		
		for (ConstraintViolation<?> violation : violations) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
					String errorCode = cd.getAnnotation().annotationType().getSimpleName();
					Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
					// Can do custom FieldError registration with invalid value from ConstraintViolation,
					// as necessary for Hibernate Validator compatibility (non-indexed set path in field)
					BindingResult bindingResult = (BindingResult) errors;
					String nestedField = bindingResult.getNestedPath() + field;
					if ("".equals(nestedField)) {
						String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
						bindingResult.addError(new ObjectError(
								errors.getObjectName(), errorCodes, errorArgs, violation.getMessage()));
					}
					else {
						Object invalidValue = violation.getInvalidValue();
						if (!"".equals(field) && (invalidValue == violation.getLeafBean() ||
								(field.contains(".") && !field.contains("[]")))) {
							// Possibly a bean constraint with property path: retrieve the actual property value.
							// However, explicitly avoid this for "address[]" style paths that we can't handle.
							invalidValue = bindingResult.getRawFieldValue(field);
						}
						String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
						bindingResult.addError(new FieldError(
								errors.getObjectName(), nestedField, invalidValue, false,
								errorCodes, errorArgs, violation.getMessage()));
					}
					
				}
				catch (NotReadablePropertyException ex) {
					throw new IllegalStateException("JSR-303 validated property '" + field +
							"' does not have a corresponding accessor for Spring data binding - " +
							"check your DataBinder's configuration (bean property versus direct field access)", ex);
				}
			}
		}
		
		System.out.println(errors);
		for(int i = 0; i < errors.getAllErrors().size(); i++){
			ObjectError error =  errors.getAllErrors().get(i);
			String str = messageSource.getMessage(error, Locale.CHINA);
			System.out.println(error.getCode() + ":" + str);
			
		}
		
	}
	
	protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
		List<Object> arguments = new LinkedList<Object>();
		String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
		arguments.add(new DefaultMessageSourceResolvable(codes, field));
		// Using a TreeMap for alphabetical ordering of attribute names
		Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
			String attributeName = entry.getKey();
			Object attributeValue = entry.getValue();
			if (!internalAnnotationAttributes.contains(attributeName)) {
				attributesToExpose.put(attributeName, attributeValue);
			}
		}
		arguments.addAll(attributesToExpose.values());
		return arguments.toArray(new Object[arguments.size()]);
	}
}
