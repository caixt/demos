package com.github.cxt.Mybatis.main;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.github.cxt.Mybatis.JSONArrayHandler;
import com.github.cxt.Mybatis.JSONObjectHandler;
import com.github.cxt.Mybatis.STRING;
import com.github.cxt.Mybatis.UUIDTypeHandler;
import com.github.pagehelper.PageInterceptor;

@EnableTransactionManagement
public class Configurator {

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
    	Interceptor[] plugins = new Interceptor[1];
    	sqlSessionFactoryBean.setPlugins(plugins);
    	Interceptor plugin = new PageInterceptor();
    	plugins[0] = plugin;
    	Properties props = new Properties();
    	props.setProperty("helperDialect", "mysql");
    	props.setProperty("reasonable", "true");
    	plugin.setProperties(props);
    	return sqlSessionFactoryBean;
    }
    
    @Bean
    MapperScannerConfigurer mapperScannerConfigurer(){
    	MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
    	scannerConfigurer.setBasePackage("com.github.cxt.Mybatis.dao");
    	return scannerConfigurer;
    }
}
