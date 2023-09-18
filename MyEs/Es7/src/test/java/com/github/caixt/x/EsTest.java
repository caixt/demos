package com.github.caixt.x;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import com.github.caixt.x.EsClient.Item;
import com.github.caixt.x.EsClient.Method;


public class EsTest {
	

	@Test
	public void test() throws IOException {
		EsClient esClient = new EsClient6();
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("admin", "Admin@123"));
		HttpHost[] httpHosts = new HttpHost[] {new HttpHost("10.1.56.59", 9200)};
		esClient.init(httpHosts, credentialsProvider);
		
		System.out.println(esClient.exists("tenantid_kb_attachment_data"));
		esClient.bulk(new Item("tenantid_kb_attachment_data", "1", "{\"attachmentName\":\"测试\"}", Method.add));
		
		SearchSourceBuilder sb = new SearchSourceBuilder();
		sb.query(QueryBuilders.matchAllQuery());
		System.out.println(sb.toString());
		SearchResponse response = esClient.search("e10adc3949ba59abbe56e057f20f88dd_kb_data", sb);
		System.out.println(response.toString());
	}
	
	
	@Test
	public void test2() throws IOException {
		EsClient esClient = new EsClient7();
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("admin", "Admin@123"));
		HttpHost[] httpHosts = new HttpHost[] {new HttpHost("10.1.5.230", 9200)};
		esClient.init(httpHosts, credentialsProvider);
		System.out.println(esClient.exists("tenantid_kb_attachment_data"));
		esClient.bulk(new Item("tenantid_kb_attachment_data", "1", "{\"attachmentName\":\"测试\"}", Method.add));
//		esClient.delete("tenantid_kb_attachment_data");
		SearchSourceBuilder sb = new SearchSourceBuilder();
		sb.query(QueryBuilders.matchAllQuery());
		System.out.println(sb.toString());
		esClient.search("tenantid_kb_attachment_data", sb);
	}
}
