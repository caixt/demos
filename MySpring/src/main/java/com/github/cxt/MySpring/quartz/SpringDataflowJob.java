package com.github.cxt.MySpring.quartz;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpringDataflowJob implements DataflowJob<Integer> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static List<Integer> DATA = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    
    @Override
    public List<Integer> fetchData(final ShardingContext shardingContext) {
    	logger.info(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW FETCH"));
    	int num = shardingContext.getShardingTotalCount();
    	List<Integer> list = new ArrayList<>();
    	for(Integer n : DATA){
    		if(n.hashCode() % num == shardingContext.getShardingItem()){
    			list.add(n);
    		}
    	}
        return list;
    }
    
    @Override
    public void processData(final ShardingContext shardingContext, final List<Integer> data) {
    	logger.info(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW PROCESS"));
    	logger.info("data size:{}", data.size());
    }
}
