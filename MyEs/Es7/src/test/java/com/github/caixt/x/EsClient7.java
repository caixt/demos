package com.github.caixt.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;


public class EsClient7 implements EsClient {
	
	private RestHighLevelClient client;
	
	public void init(HttpHost[] httpHosts, CredentialsProvider credentialsProvider) {
		RestClientBuilder restClientBuilder = RestClient.builder(httpHosts)
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
		            @Override
		            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
		                return httpClientBuilder
		                        .setDefaultCredentialsProvider(credentialsProvider)
		                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
		            }
        });
        client = new RestHighLevelClient(restClientBuilder);
	}
	

	public boolean  delete(String... indices) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(indices);
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}
	
	public boolean exists(String... indices) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indices);
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public SearchResponse search(String index, SearchSourceBuilder searchSourceBuilder) throws IOException {
		SearchRequest searchRequest = new SearchRequest(index);
		searchRequest.source(searchSourceBuilder).indicesOptions(IndicesOptions.lenientExpandOpen());
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	@Override
	public Result bulk(Item... items) throws IOException {
		BulkRequest request = new BulkRequest();
		for(Item item : items) {
			if(item.method == Method.add) {
				request.add( new IndexRequest(item.index).id(item.id).source(item.source, XContentType.JSON));
			}
			else {
				request.add(new DeleteRequest(item.index, item.id));
			}
		}
		BulkResponse result = client.bulk(request, RequestOptions.DEFAULT);
		if(result.hasFailures()) {
			return new Result(true, result.buildFailureMessage());
		}
		return new Result(true, null);
	}
	
	
	@Override
	public List<String> mget(Item... items) throws IOException {
		MultiGetRequest request = new MultiGetRequest();
		for(Item item : items) {
			request.add(item.index, item.id);
		}
		MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
		List<String> list = new ArrayList<>();
		for (MultiGetItemResponse item : response.getResponses()) {
			list.add(item.getResponse().getSourceAsString());
		}
		return list;
	}
}
