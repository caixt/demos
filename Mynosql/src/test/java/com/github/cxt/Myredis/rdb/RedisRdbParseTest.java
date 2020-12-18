package com.github.cxt.Myredis.rdb;

import java.io.File;

public class RedisRdbParseTest {
	public static void main(String[] args) {
		String path = "C:\\Users\\WIN\\Desktop\\all_dump.rdb";
//		String path = "D:\\其他\\redis_log\\redis_log\\dump.rdb";
//		String path = "D:\\git\\test\\java-redis-rdb-master\\src\\test\\java\\com\\pajk\\rdb\\dump2.6.rdb";
		RdbParser rdb = new RdbParser();
		rdb.rdbParse(new File(path), new CallBackHandler() {
			public void printlnHandler(long dbid, String type, String key,
					Object val, long expiretime) {
				System.out.println(dbid + "||" + type + "||" + "key=" + key
						+ "||" + "value=" + val + "||" + expiretime);
			}
		});
	}
}
