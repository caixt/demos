package com.github.cxt.MySpring.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;



@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { Name.NameChecker.class })
@Documented
public @interface Name {
	String message() default "名称不存在";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

	public static class NameChecker implements ConstraintValidator<Name, String> {

		@Override
		public void initialize(Name constraintAnnotation) {
			System.out.println("!");
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if ("111111".equals(value)) {
				return true;
			}
			return false;
		}

	}
}