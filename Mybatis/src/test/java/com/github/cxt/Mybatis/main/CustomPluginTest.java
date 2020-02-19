package com.github.cxt.Mybatis.main;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.alibaba.fastjson.JSON;
import com.github.cxt.Mybatis.JSONArrayHandler;
import com.github.cxt.Mybatis.JSONObjectHandler;
import com.github.cxt.Mybatis.STRING;
import com.github.cxt.Mybatis.UUIDTypeHandler;
import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;
import com.github.cxt.Mybatis.plugin.MyFirstPlugin;
import com.github.cxt.Mybatis.plugin.MySecondPlugin;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CustomPluginTest.Configurator.class)
public class CustomPluginTest {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void test(){
		User user = userDao.selectByUserId(1l);
		System.out.println(JSON.toJSONString(user));
	}

	
	@EnableTransactionManagement
	public static class Configurator {

		@Bean
	    DataSource dataSource(){
			BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8");
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
	    	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	    	sqlSessionFactoryBean.setDataSource(dataSource);
	    	TypeHandler<?>[] typeHandlers = new TypeHandler[]{new JSONArrayHandler(), new JSONObjectHandler(), new UUIDTypeHandler(), new STRING()};
	    	sqlSessionFactoryBean.setTypeHandlers(typeHandlers);
	    	PathMatchingResourcePatternResolver p  = new PathMatchingResourcePatternResolver();
	    	sqlSessionFactoryBean.setMapperLocations(p.getResources("classpath*:com/github/cxt/Mybatis/mapper/*.xml"));
	    	Interceptor[] plugins = new Interceptor[2];
	    	sqlSessionFactoryBean.setPlugins(plugins);
	    	plugins[0] = new MyFirstPlugin();
	    	plugins[1] = new MySecondPlugin();
	    	return sqlSessionFactoryBean;
	    }
	    
	    @Bean
	    MapperScannerConfigurer mapperScannerConfigurer(){
	    	MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
	    	scannerConfigurer.setBasePackage("com.github.cxt.Mybatis.dao");
	    	return scannerConfigurer;
	    }
	}
}
