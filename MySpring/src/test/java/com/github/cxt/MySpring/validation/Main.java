package com.github.cxt.MySpring.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Test;


public class Main {

	@Test
	public void test1(){
		Abc demo = new Abc();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
		Validator validator = factory.getValidator(); 
		demo.setStr1("aaa");
		Set<ConstraintViolation<Abc>> violations = validator.validate(demo); 
		for (ConstraintViolation<Abc> constraintViolation : violations) {
			System.out.println(constraintViolation.getMessageTemplate());
            System.out.println(constraintViolation.getMessage());  
        }  
	}
	
	@Test
	public void test2(){
		Abc demo = new Abc();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
		Validator validator = factory.getValidator(); 
		demo.setStr1("aaa");
		Set<ConstraintViolation<Abc>> violations = validator.validate(demo, GroupA.class);
		for (ConstraintViolation<Abc> constraintViolation : violations) {
			System.out.println(constraintViolation.getMessageTemplate());
            System.out.println(constraintViolation.getMessage());  
        }  
	}
}
