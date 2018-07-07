package com.github.cxt.MySpring.quartz;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringSimpleJob implements SimpleJob {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    @Override
    public void execute(final ShardingContext shardingContext) {
    	logger.info(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));
    }
}
