package com.github.caixt.x;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public interface EsClient {
	
	abstract void init(HttpHost[] httpHosts, CredentialsProvider credentialsProvider);

	public boolean delete(String...indices) throws IOException;

	public boolean exists(String... indices) throws IOException;
	
	public Result bulk(Item...item) throws IOException;
	
	public List<String> mget(Item... items) throws IOException;
	
	public SearchResponse search(String index, SearchSourceBuilder searchSourceBuilder) throws IOException;

	
	public static class Item {
		String index;
		String id;
		String source;
		Method method;
		
		public Item(String index, String id, String source, Method method) {
			super();
			this.index = index;
			this.id = id;
			this.source = source;
			this.method = method;
		}
		
		public Item(String index, String id, Method method) {
			super();
			this.index = index;
			this.id = id;
			this.method = method;
		}
	}
	
	public enum Method {
		add,delete
	}
	
	public static class Result {
		boolean hasFailure;
		String failureMessage;
		
		public Result(boolean hasFailure, String failureMessage) {
			super();
			this.hasFailure = hasFailure;
			this.failureMessage = failureMessage;
		}
	}
}
