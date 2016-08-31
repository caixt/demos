package com.github.cxt.Mybatis.main;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
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
import com.github.cxt.Mybatis.UUIDTypeHandler;

@EnableTransactionManagement
public class Configurator {

	@Bean
    DataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&amp;characterEncoding=UTF-8");
        ds.setUsername("root");
        ds.setPassword("xiantong");
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
    	TypeHandler<?>[] typeHandlers = new TypeHandler[]{new JSONArrayHandler(), new JSONObjectHandler(), new UUIDTypeHandler()};
    	sqlSessionFactoryBean.setTypeHandlers(typeHandlers);
    	PathMatchingResourcePatternResolver p  = new PathMatchingResourcePatternResolver();
    	sqlSessionFactoryBean.setMapperLocations(p.getResources("classpath*:com/github/cxt/Mybatis/mapper/*.xml"));
    	return sqlSessionFactoryBean;
    }
    
    @Bean
    MapperScannerConfigurer mapperScannerConfigurer(){
    	MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
    	scannerConfigurer.setBasePackage("com.github.cxt.Mybatis.dao");
    	return scannerConfigurer;
    }
}
