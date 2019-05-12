package com.github.cxt.Mybatis.dds;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Intercepts(
	{ 
		@Signature(type=Executor.class, method="update", args={MappedStatement.class, Object.class}),
		@Signature(type=Executor.class, method="query", args={MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}) 
	}
)
public class DynamicDataSourcePlugin implements Interceptor{
	
	private static Logger logger  = LoggerFactory.getLogger(DynamicDataSourcePlugin.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
		String routeKey = null;
		Object[] objects = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) objects[0];
		if(!transactionActive){
			if(mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
				if(mappedStatement.getId().equals(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
					routeKey = DynamicDataSourceHolder.DB_MASTER;
				}
				else{
					routeKey = DynamicDataSourceHolder.DB_SLAVE;
				}
			}
			else{
				routeKey = DynamicDataSourceHolder.DB_MASTER;
			}
		}else{
			//带事物的全部在主库执行
			routeKey = DynamicDataSourceHolder.DB_MASTER;
		}
		DynamicDataSourceHolder.setRouteKey(routeKey);
		logger.info("使用{}方法,执行了{}策略,执行的命令为{}", invocation.getMethod().getName(), 
				routeKey, mappedStatement.getSqlCommandType().name());
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
