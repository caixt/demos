package com.github.cxt.MySpring.regix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.el.ExpressionFactory;
import org.junit.Test;
import com.github.cxt.MySpring.transaction.mybatis.Table;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

//http://blog.chinaunix.net/uid-631975-id-1641857.html
public class Juel {

	@Test
	public void test1() throws NoSuchMethodException, SecurityException {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", "1");
		List<Table> tables = new ArrayList<Table>();
		Table table = new Table();
		table.setId(1L);
		tables.add(table);
		table = new Table();
		table.setId(2L);
		tables.add(table);
		map.put("$tables", tables);
		
		System.out.println(eval_de(String.class, "aaa${aaa}aaa${$tables[0].id}", new HashMap<>(map)));
	}

	
	@SuppressWarnings("unchecked")
	private <T> T eval_de(Class<T> T, String expr, Map<String, Object> args) throws NoSuchMethodException, SecurityException {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		//context.setFunction("math", "max", Math.class.getMethod("max", int.class, int.class)); 
		context.setFunction("", "max", Math.class.getMethod("max", int.class, int.class));
		for(Entry<String, Object> entry : args.entrySet()) {
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), Object.class));
		}
		return (T)factory.createValueExpression(context, expr, T).getValue(context);
	}
}
