package com.github.cxt.Mybatis.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.github.cxt.Mybatis.JSONArrayHandler;
import com.github.cxt.Mybatis.JSONObjectHandler;
import com.github.cxt.Mybatis.STRING;
import com.github.cxt.Mybatis.UUIDTypeHandler;
import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.dds.DynamicDataSource;
import com.github.cxt.Mybatis.dds.DynamicDataSourcePlugin;
import com.github.cxt.Mybatis.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DynamicDataSourcePluginTest.Configurator.class, DynamicDataSourcePluginTest.Configurator2.class})
//@Transactional
//@TransactionConfiguration(defaultRollback = true)
public class DynamicDataSourcePluginTest {
	
	@Autowired
	private UserDao userDao;
	
	@Before
	public void before(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();  
		SLF4JBridgeHandler.install();
	}
	
	
	@Test
	public void test1(){
		User user = new User();
		user.setName("aaa");
		user.setUserType(1);
		user.setClasses(new String[]{"a","b"});
		
		System.out.println(userDao.insert(user));
	}
	
	@Test
	public void test2(){
		System.out.println(userDao.selectAll());
	}
	
	@EnableTransactionManagement
	@Configuration
	public static class Configurator {
		
		@Bean
	    DataSource master(){
			BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8");
	        ds.setUsername("root");
	        ds.setPassword("xiantong");
	        return ds;
	    }
		
		@Bean
	    DataSource slave(){
			BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis2?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8");
	        ds.setUsername("root");
	        ds.setPassword("xiantong");
	        return ds;
	    }
		
		@Bean
		DynamicDataSource dynamicDataSource(){
			DynamicDataSource dds = new DynamicDataSource();
			Map<Object, Object> targetDataSources = new HashMap<>();
			targetDataSources.put("master", master());
			targetDataSources.put("slave", slave());
			dds.setTargetDataSources(targetDataSources);
			return dds;
		}
		
		@Bean
		LazyConnectionDataSourceProxy dataSource(){
			LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy = new LazyConnectionDataSourceProxy();
			lazyConnectionDataSourceProxy.setTargetDataSource(dynamicDataSource());
			return lazyConnectionDataSourceProxy;
			
		}
		
	    @Bean(name="transactionManager")
	    PlatformTransactionManager txManager() {
	        return new DataSourceTransactionManager(dataSource());
	    }
	    
	    @Bean
	    SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException{
	    	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	    	sqlSessionFactoryBean.setDataSource(dataSource());
	    	TypeHandler<?>[] typeHandlers = new TypeHandler[]{new JSONArrayHandler(), new JSONObjectHandler(), new UUIDTypeHandler(), new STRING()};
	    	sqlSessionFactoryBean.setTypeHandlers(typeHandlers);
	    	PathMatchingResourcePatternResolver p  = new PathMatchingResourcePatternResolver();
	    	sqlSessionFactoryBean.setMapperLocations(p.getResources("classpath*:com/github/cxt/Mybatis/mapper/*.xml"));
	    	Interceptor[] plugins = new Interceptor[]{new DynamicDataSourcePlugin()};
	    	sqlSessionFactoryBean.setPlugins(plugins);
	    	return sqlSessionFactoryBean;
	    }
	    
	}
	
	//单独写,不然上面的ref就不生效了.不知道为什么。
	public static class Configurator2 {
		@Bean
	    MapperScannerConfigurer mapperScannerConfigurer(){
	    	MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
	    	scannerConfigurer.setBasePackage("com.github.cxt.Mybatis.dao");
	    	return scannerConfigurer;
	    }
	}
}
