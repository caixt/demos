package com.github.caixt.Es7;

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
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexTemplatesRequest;
import org.elasticsearch.client.indices.GetIndexTemplatesResponse;
import org.elasticsearch.client.indices.IndexTemplateMetadata;
import org.elasticsearch.client.indices.IndexTemplatesExistRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.junit.Before;
import org.junit.Test;


public class EsTest {
	
	private String host = "10.1.5.230";
	private Integer port = 9200;
	private String user = "admin";
	private String password = "Admin@123";
	
	private static String INDEX = "caixt_data_2023";
	private static String ALIAS = "caixt_data";
	private RestHighLevelClient client;
	protected static final RequestOptions COMMON_OPTIONS;
	
	static {
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

		// 默认缓冲限制为100MB，此处修改为30MB。
		builder.setHttpAsyncResponseConsumerFactory(
				new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
		COMMON_OPTIONS = builder.build();
	}

	//_nodes/stats/indices/search
	//http://127.0.0.1:9100/?auth_user=xxx&auth_password=xxx
	//http://127.0.0.1:9200/caixt_data
	//http://127.0.0.1:9200/caixt_data/_settings
	//http://127.0.0.1:9200/caixt_data/_mapping/
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
	
	//PUT /_template/caixt_templete
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
		putIndexTemplateRequest.mapping(xMapping);
		putIndexTemplateRequest.alias(new Alias(ALIAS));
		
		AcknowledgedResponse response = client.indices().putTemplate(putIndexTemplateRequest, COMMON_OPTIONS);
		System.out.println(response.isAcknowledged());
	}
	
	//GET /_template/caixt_templete
	@Test
	public void getTemplete() throws IOException {
		GetIndexTemplatesRequest request = new IndexTemplatesExistRequest("caixt_templete");
		GetIndexTemplatesResponse response = client.indices().getIndexTemplate(request, COMMON_OPTIONS);
		System.out.println(response.getIndexTemplates());
		if(response.getIndexTemplates().size() > 0) {
			IndexTemplateMetadata data = response.getIndexTemplates().get(0);
			System.out.println(data.mappings().getSourceAsMap());
		}
	}
	
	//DELETE /_template/caixt_templete
	@Test
	public void deleteTemplete() throws IOException {
		DeleteIndexTemplateRequest request = new DeleteIndexTemplateRequest("caixt_templete");
		AcknowledgedResponse response = client.indices().deleteTemplate(request, COMMON_OPTIONS);
		System.out.println(response.isAcknowledged());
	}
	
	/*
	@Test
	public void createIndex() throws IOException {
		boolean exist = client.indices().exists(new GetIndexRequest(INDEX), COMMON_OPTIONS);
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
			request.mapping(xMapping);
			CreateIndexResponse createIndexResponse = client.indices().create(request, COMMON_OPTIONS);
			
			System.out.println(createIndexResponse.isAcknowledged());
			System.out.println(createIndexResponse.isShardsAcknowledged());
		}
		else{
	        System.out.println("exist!!!");
	    }
	} */
	
	//DELETE /caixt_data_2023
	@Test
	public void deleteIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(INDEX);
		AcknowledgedResponse response = client.indices().delete(request, COMMON_OPTIONS);
		System.out.println(response.isAcknowledged());
	}
	
	
	//POST /caixt_data_2023/_doc/1
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
		
		IndexRequest request = new IndexRequest(INDEX).id("1").source(employeeMap);
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		System.out.println(indexResponse.getShardInfo().getFailed() == 0);
	}
	
	//GET /caixt_data_2023/_doc/1
	@Test
	public void testGet() throws IOException{
		GetResponse response = client.get(new GetRequest(INDEX, "1"), RequestOptions.DEFAULT);
		System.out.println(response.getSource() + "!" + response.isExists());
		System.out.println(response.getSourceAsString() + response.getVersion());
	}
	
	@Test
	public void testBulkAdd() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap = new HashMap<>();
		employeeMap.put("id", 1);
		employeeMap.put("createDate", "2019-10-15");
		employeeMap.put("age", 1);
		employeeMap.put("category", "国内");
		Map<String, Object> info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "国内消息");
		info.put("content", "公安部：中国各地校车将享最高路权,中国");
		bulkRequest.add(new IndexRequest(INDEX).id("-1").source(employeeMap));

		employeeMap = new HashMap<>();
		employeeMap.put("id", 2);
		employeeMap.put("createDate", "2019-10-15");
		employeeMap.put("age", 3);
		employeeMap.put("category", "国际");
		info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "国际消息");
		info.put("content", "美国留给伊拉克的是个烂摊子吗");
		bulkRequest.add(new IndexRequest(INDEX).id("-2").source(employeeMap));
		
		employeeMap = new HashMap<>();
		employeeMap.put("id", 3);
		employeeMap.put("createDate", "2019-10-15");
		employeeMap.put("age", 3);
		employeeMap.put("category", "国际");
		info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "国际消息");
		info.put("content", "中韩渔警冲突调查：韩警平均每天扣1艘中国渔船");
		bulkRequest.add(new IndexRequest(INDEX).id("-3").source(employeeMap));
		
		employeeMap = new HashMap<>();
		employeeMap.put("id", 4);
		employeeMap.put("createDate", "2019-10-15");
		employeeMap.put("age", 4);
		employeeMap.put("category", "国际");
		info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "国际消息");
		info.put("content", "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首");
		bulkRequest.add(new IndexRequest(INDEX).id("-4").source(employeeMap));
		
		long now = System.currentTimeMillis();
		BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.out.println(System.currentTimeMillis() - now);
		System.out.println(!responses.hasFailures());
		
		for(BulkItemResponse item: responses.getItems()){
			if(item.isFailed()){
				item.getFailure().getCause().printStackTrace();
			}
		}
	}
	
	@Test
	public void testMget() throws IOException {
		MultiGetRequest request = new MultiGetRequest();
		//new GetRequest(INDEX, "1")
		request.add(INDEX, "-1").add(INDEX, "-2");
		MultiGetResponse response = client.mget(request, COMMON_OPTIONS);
		for(MultiGetItemResponse item : response.getResponses()) {
			System.out.println(item.getIndex() + "!" + item.getId());
			System.out.println(item.getResponse().getSource());
			
		}
	}
	
	//POST /caixt_data_2023/_update/1     {"doc":{"category":"e1-editx"}}
	@Test
	public void testUpdate() throws IOException {
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("category", "e1-editx");
		Map<String, Object> info = new HashMap<>();
		employeeMap.put("info", info);
		info.put("title", "测试标题...");
		UpdateRequest request = new UpdateRequest(INDEX, "1").doc(employeeMap);
		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
		System.out.println(response.getShardInfo().getFailed() == 0);
	}
	
	//DELETE /caixt_data_2023/_doc/1
	@Test
	public void testDelete() throws IOException {
		DeleteResponse response = client.delete(new DeleteRequest(INDEX, "1"), RequestOptions.DEFAULT);
		System.out.println(response.status() + "!" + response.getShardInfo().status());
	}
	
	
	@Test
	public void testQueryScroll() throws IOException {
		long time = 1000 * 60 * 1;
		SearchRequest searchRequest = new SearchRequest(ALIAS);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder
		.from(0).size(1)
		.fetchSource(new String[]{"id"}, null)
		;
	    searchRequest.source(searchSourceBuilder)
	    	.scroll(new TimeValue(time));
	    
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		int  num = 0;
		while(true){
			num ++;
			System.out.println("failedShards:" + response.getFailedShards());
			SearchHits hits = response.getHits();
			System.out.println("total:" + hits.getTotalHits());
			for(SearchHit hit : hits.getHits()){
				System.out.println(hit.getSourceAsString() + "!" + hit.getId() + "!" + hit.getIndex());
			}
			System.out.println("---------------"  + response.getScrollId());
			response = client.scroll(new SearchScrollRequest(response.getScrollId()).scroll(new TimeValue(time)), RequestOptions.DEFAULT);
			if (response.getHits().getHits().length == 0) {
                break;
            }
		}
		ClearScrollRequest request = new ClearScrollRequest(); 
		request.addScrollId(response.getScrollId()); 
		client.clearScroll(request, RequestOptions.DEFAULT);
		System.out.println(num);
	}
	
	//GET caixt_data/_search/
	@Test
	public void testQuery() throws IOException {
		String key = "测试";
		SearchRequest searchRequest = new SearchRequest(ALIAS);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(
				QueryBuilders.boolQuery().must(QueryBuilders.boolQuery()
						.should(QueryBuilders.multiMatchQuery(key, "info.title", "info.content").type(Type.CROSS_FIELDS).operator(Operator.OR))
						.should(QueryBuilders.multiMatchQuery(key, "info.title", "info.content").type(Type.PHRASE_PREFIX).operator(Operator.OR))
						)
				.filter(QueryBuilders.rangeQuery("age").gte(0))
				)
//		 		.sort("age", SortOrder.ASC)
				.highlighter(new HighlightBuilder().field("info.title").field("info.content").preTags("<font color='red'>").postTags("</font>"))
				.from(0).size(10);
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println("failedShards:" + response.getFailedShards());
		SearchHits hits = response.getHits();
		System.out.println("total:" + hits.getTotalHits());
		for(SearchHit hit : hits.getHits()){
			System.out.println(hit.getSourceAsString() + "!" + hit.getScore());
			System.out.println(hit.getHighlightFields());
		}
	}
	
}
