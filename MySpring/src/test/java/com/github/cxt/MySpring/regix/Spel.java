package com.github.cxt.MySpring.regix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.cxt.MySpring.transaction.mybatis.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/regix/spring-el.xml")
public class Spel {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void test1(){
		ExpressionParser parser = new SpelExpressionParser();
		//literal expressions 
		Expression exp = parser.parseExpression("'Hello World'");
		String msg1 = exp.getValue(String.class);
		System.out.println(msg1);
		
		//method invocation
		Expression exp2 = parser.parseExpression("'Hello World'.length()");  
		int msg2 = (Integer) exp2.getValue();
		System.out.println(msg2);
		
		//Mathematical operators
		Expression exp3 = parser.parseExpression("100 * 2");  
		int msg3 = (Integer) exp3.getValue();
		System.out.println(msg3);
		
		//create an item object
		Table table = new Table();
		table.setColumn1("abcd");
		//test EL with item object
		StandardEvaluationContext itemContext = new StandardEvaluationContext(table);
		
		//display the value of item.name property
		Expression exp4 = parser.parseExpression("column1");
		String msg4 = exp4.getValue(itemContext, String.class);
		System.out.println(msg4);
		
		//test if item.name == 'yiibai'
		Expression exp5 = parser.parseExpression("column1 == 'abcd'");
		boolean msg5 = exp5.getValue(itemContext, Boolean.class);
		System.out.println(msg5);
	}
	
	@Test
	public void test2(){
		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		System.out.println(factory.resolveEmbeddedValue("${a2}"));
	}
	
	@Test
	public void test3(){
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", 1);
		List<Table> tables = new ArrayList<Table>();
		Table table = new Table();
		table.setId(1L);
		tables.add(table);
		table = new Table();
		table.setId(2L);
		tables.add(table);
		map.put("$tables", tables);
		System.out.println(eval_spring(String.class, "#$tables[0].id + #aaa", new HashMap<>(map)));
		
	}
	
	@Test
	public void test4(){
		ExpressionParser parser = new SpelExpressionParser();
		ParserContext parserContext = new ParserContext() {  
	        @Override  
	         public boolean isTemplate() {  
	            return true;  
	        }  
	        @Override  
	        public String getExpressionPrefix() {  
	            return "#{";  
	        }  
	        @Override  
	        public String getExpressionSuffix() {  
	            return "}";  
	        }  
	    };
	    String str = "#{'aaaa'.toUpperCase()}";
	    String msg = parser.parseExpression(str, parserContext).getValue(String.class);
		System.out.println(msg);
	}
	
	
	public <T> T eval_spring(Class<T> T, String expr, Map<String, Object> args) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		for(Entry<String, Object> entry : args.entrySet()) {
			context.setVariable(entry.getKey(), entry.getValue());
		}
		return parser.parseExpression(expr).getValue(context, T);
	}
}
