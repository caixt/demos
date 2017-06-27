package com.github.cxt.MySpring.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import java.util.concurrent.atomic.AtomicInteger;

//http://www.2cto.com/kf/201604/503411.html
public class HelloCommand extends HystrixCommand<Object> {
	
	static AtomicInteger num = new AtomicInteger(0);

    public HelloCommand(String group, String key) {
    	super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group))
			    .andCommandKey(HystrixCommandKey.Factory.asKey(key))
			    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
			    		.withMetricsRollingStatisticalWindowInMilliseconds(10000) //统计滚动的时间窗口 default 10000 ten seconds
			    		.withCircuitBreakerRequestVolumeThreshold(2)		// 10秒钟内至少5次请求失败，熔断器才发挥起作用
						.withCircuitBreakerSleepWindowInMilliseconds(30000)	// 熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
						.withCircuitBreakerErrorThresholdPercentage(50)		// 错误率达到50开启熔断保护
						.withExecutionTimeoutEnabled(false))			// 禁用这里的超时
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10))
						);
    }

    @Override
    protected Object run() throws Exception {
    	int n = num.incrementAndGet();
    	System.out.println("+++++++++++++++++++++++++++++++++" + n);
    	Thread.sleep(5000);
    	if(n > 1 && n < 5){
    		throw new Exception("test" + n);
    	}
        return String.format("Hello %d", n);
    }
}