package com.github.cxt.MySpring.regix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.el.ExpressionFactory;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import com.github.cxt.MySpring.transaction.mybatis.Table;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

public class Expression {

	@Test
	public void test1() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", 1);
		List<Table> tables = new ArrayList<Table>();
		Table table = new Table();
		table.setId(1L);
		tables.add(table);
		table = new Table();
		table.setId(2L);
		tables.add(table);
		map.put("tables", tables);
		System.out.println(eval_de(String.class, "aaa${aaa}a${tables[0].id}", new HashMap<>(map)));
	}
	
	@Test
	public void test2() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", 1);
		List<Table> tables = new ArrayList<Table>();
		Table table = new Table();
		table.setId(10001L);
		tables.add(table);
		table = new Table();
		table.setId(10002L);
		tables.add(table);
		map.put("tables", tables);
		System.out.println(eval_de(String.class, "aaa#{aaa}a#{tables[0].id}", new HashMap<>(map)));
	}

	
	private <T> T eval_de(Class<T> T, String expr, Map<String, Object> args) {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		for(Entry<String, Object> entry : args.entrySet()) {
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), Object.class));
		}
		return (T)factory.createValueExpression(context, expr, T).getValue(context);
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
