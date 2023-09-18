package com.github.caixt.Es6;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesRequest;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Before;
import org.junit.Test;


public class EsTest {
	
	private String host = "10.1.56.59";
	private Integer port = 9200;
	private String user = "admin";
	private String password = "Admin@123";
	
	private static String INDEX = "caixt_data_2023";
	private static String ALIAS = "caixt_data";
	private RestHighLevelClient client;
	private String TYPE = "doc";
	
	
	//_nodes/stats/indices/search
	//http://127.0.0.1:9100/?auth_user=xxx&auth_password=xxx
	//http://127.0.0.1:9200/caixt_test/_settings
	//http://127.0.0.1:9200/caixt_test/_mapping/
	//curl -XPOST http://127.0.0.1:9200/_analyze -H 'Content-Type:application/json' -d'{"text":"我爱北京天安门","analyzer":"ik_smart"}'
	//curl -XPOST http://127.0.0.1:9200/_analyze -H 'Content-Type:application/json' -d'{"text":"我爱北京天安门","analyzer":"ik_max_word"}'
	//curl -XPOST http://127.0.0.1:9200/_analyze -H 'Content-Type:application/json' -d'{"text":"我爱北京天安门","analyzer":"standard"}'
	@Before
	public void before() {
		//RestHighLevelClient客户端初始化
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(user, password));
		client = new RestHighLevelClient(
		        RestClient.builder(new HttpHost(host, port))
		                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
		                    @Override
		                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
		                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		                    }
		                }));
	}
	
	@Test
	public void addTemplete() throws IOException {
		PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest("caixt_templete");
		putIndexTemplateRequest.settings(Settings.builder()
				.put("index.number_of_shards", 1)
				.put("index.number_of_replicas", 1));
		putIndexTemplateRequest.patterns(Arrays.asList("caixt_data_*"));
		XContentBuilder xMapping =  XContentFactory.jsonBuilder()
				.startObject()
                .startObject("properties")
                
                .startObject("id")
                .field("type","keyword")
                .endObject()
                
                .startObject("createDate")
                .field("type","date")
                .endObject()
                
                .startObject("category")
                .field("type","keyword")
                .endObject()
                
                .startObject("info")
                .startObject("properties")
                .startObject("title")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()
                .startObject("content")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()
                .endObject()
                .endObject()
                
                .endObject()
                .endObject();
		putIndexTemplateRequest.mapping(TYPE, xMapping);
		putIndexTemplateRequest.alias(new Alias(ALIAS));
		
		AcknowledgedResponse response = client.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT);
		System.out.println(response.isAcknowledged());
	}
	
	@Test
	public void getTemplete() throws IOException {
		GetIndexTemplatesRequest request = new GetIndexTemplatesRequest("caixt_templete");
		GetIndexTemplatesResponse response = client.indices().getTemplate(request, RequestOptions.DEFAULT);
		System.out.println(response.getIndexTemplates());
		if(response.getIndexTemplates().size() > 0) {
			IndexTemplateMetaData data = response.getIndexTemplates().get(0);
			System.out.println(data.mappings().toString());
		}
	}
	
	/**
	@Test
	public void createIndex() throws IOException {
		boolean exist = client.indices().exists(new GetIndexRequest().indices(INDEX));
		System.out.println(exist);
		if(!exist){
			CreateIndexRequest request = new CreateIndexRequest(INDEX);
			// Settings for this index
			request.settings(
					Settings.builder().put("index.number_of_shards", 1)
							.put("index.number_of_replicas", 1));
			
			XContentBuilder xMapping =  XContentFactory.jsonBuilder()
					.startObject()
                    .startObject("properties")
                    
                    .startObject("id")
                    .field("type","keyword")
                    .endObject()
                    
                    .startObject("createDate")
                    .field("type","date")
                    .endObject()
                    
                    .startObject("category")
                    .field("type","keyword")
                    .endObject()
                    
                    .startObject("info")
                    .startObject("properties")
                    .startObject("title")
                    .field("type","text")
                    .field("analyzer","keyword")
                    .field("search_analyzer","keyword")
                    .endObject()
                    .startObject("content")
                    .field("type","text")
                    .field("analyzer","keyword")
                    .field("search_analyzer","keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    
                    .endObject()
                    .endObject();
			request.mapping(TYPE, xMapping);
			CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
			
			System.out.println(createIndexResponse.isAcknowledged());
			System.out.println(createIndexResponse.isShardsAcknowledged());
		}
		else{
	        System.out.println("exist!!!");
	    }
	}*/
	
	@Test
	public void deleteIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(INDEX);
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		System.out.println(response.isAcknowledged());
	}
	
	@Test
	public void testAdd() throws IOException {
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap = new HashMap<>();
		employeeMap.put("id", 0);
		employeeMap.put("createDate", "2019-10-15");
		employeeMap.put("age", 0);
		employeeMap.put("category", "测试");
		Map<String, Object> info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "测试标题");
		info.put("content", "使用情况测试内容乌云产品真好用啊192.168.1.1使用情况");
		IndexRequest request = new IndexRequest(INDEX).type(TYPE).id("1").source(info);
		
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		System.out.println(indexResponse.getShardInfo().getFailed() == 0);
	}
	
	@Test
	public void testUpdate() throws IOException {
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("category", "e1-editx");
		Map<String, Object> info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "测试标题...");
		UpdateRequest request = new UpdateRequest(INDEX, TYPE, "1").doc(employeeMap);
		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
		System.out.println(response.getShardInfo().getFailed() == 0);
	}
	
	@Test
	public void testGet() throws IOException{
		GetResponse response = client.get(new GetRequest(INDEX, TYPE, "1"), RequestOptions.DEFAULT);
		System.out.println(response.getSource() + "!" + response.isExists());
		System.out.println(response.getSourceAsString() + response.getVersion());
	}

	@Test
	public void testDelete() throws IOException {
		DeleteResponse response = client.delete(new DeleteRequest(INDEX, TYPE, "1"), RequestOptions.DEFAULT);
		System.out.println(response.status() + "!" + response.getShardInfo().status());
	}
	
}
