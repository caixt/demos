package com.github.cxt.MySpring.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.type.TypeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.github.cxt.MySpring.transaction.mybatis.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main.Context.class })
//@Transactional
//@TransactionConfiguration(defaultRollback = true)
public class Main {
	
	@Autowired
	private Table1Server table1Server;
	
	/**
	 * 一个插入,结束后模拟异常
	 */
	@Test
	public void test1(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save1(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务(默认)
	 * 1,2失败
	 */
	@Test
	public void test2(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save2(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
	 * 即1失败,2成功
	 */
	@Test
	public void test3(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save3(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。
	 * 异常不是2里面的异常
	 * 1,2失败
	 */
	@Test
	public void test4(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save4(table);
	}
	
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起
	 * 把后一个抛出的异常捕获
	 * 1成功,2失败
	 */
	@Test
	public void test5(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save5(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。
	 * 1,2失败
	 */
	@Test
	public void test6(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save6(table);
	}

	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
	 * 第一个不加事务,即两个成功
	 */
	@Test
	public void test7(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save7(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
	 * 第一个加事务,即两个失败
	 */
	@Test
	public void test8(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save8(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性NESTED：支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务
	 * 在第个方法捕获异常,即第一个成功,第二个失败,如果该用默认事务抛出  "Transaction rolled back because it has been marked as rollback-only"
	 */
	@Test
	public void test9(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save9(table);
	}
	
	
	@EnableTransactionManagement
	public static class Context{
		
		@Bean
	    DataSource dataSource(){
			BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&characterEncoding=UTF-8");
	        ds.setUsername("root");
	        ds.setPassword("12345678");
	        return ds;
	    }
		
	    @Bean(name="transactionManager")
	    PlatformTransactionManager txManager(DataSource dataSource) {
	        return new DataSourceTransactionManager(dataSource);
	    }
	    
	    @Bean
	    SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException{
	    	List<TypeHandler<?>> customHandler = new ArrayList<>();
	    	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	    	sqlSessionFactoryBean.setDataSource(dataSource);
	    	
	    	sqlSessionFactoryBean.setTypeHandlers(customHandler.toArray(new TypeHandler[]{}));
	    	PathMatchingResourcePatternResolver p  = new PathMatchingResourcePatternResolver();
	    	sqlSessionFactoryBean.setMapperLocations(p.getResources("classpath*:com/github/cxt/MySpring/transaction/mybatis/*.xml"));
	    	return sqlSessionFactoryBean;
	    }
	    
	    @Bean
	    @DependsOn(value="sqlSessionFactoryBean")
	    MapperScannerConfigurer mapperScannerConfigurer(){
	    	MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
	    	scannerConfigurer.setBasePackage("com.github.cxt.MySpring.transaction.mybatis");
	    	return scannerConfigurer;
	    }
		
		@Bean
		Table1Server table1Server(){
			return new Table1ServerImpl();
		}
		
		@Bean
		Table2Server table2Server(){
			return new Table2ServerImpl();
		}
		
		@Bean
		TestEventListener testEventListener(){
			return new TestEventListener();
		}
		
	}

}
