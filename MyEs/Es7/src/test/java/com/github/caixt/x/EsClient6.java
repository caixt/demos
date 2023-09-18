package com.github.caixt.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.v6elasticsearch.action.get.MultiGetItemResponse;
import org.v6elasticsearch.action.get.MultiGetRequest;
import org.v6elasticsearch.action.get.MultiGetResponse;
import org.v6elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.v6elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.v6elasticsearch.action.index.IndexRequest;
import org.v6elasticsearch.client.RestClient;
import org.v6elasticsearch.client.RestClientBuilder;
import org.v6elasticsearch.client.RestHighLevelClient;
import org.v6elasticsearch.common.settings.Settings;
import org.v6elasticsearch.common.xcontent.LoggingDeprecationHandler;
import org.v6elasticsearch.common.xcontent.NamedXContentRegistry;
import org.v6elasticsearch.common.xcontent.XContentFactory;
import org.v6elasticsearch.common.xcontent.XContentParser;
import org.v6elasticsearch.common.xcontent.XContentType;
import org.v6elasticsearch.search.SearchModule;
import org.v6elasticsearch.action.bulk.BulkResponse;
import org.v6elasticsearch.action.search.SearchRequest;
import org.v6elasticsearch.action.support.IndicesOptions;
import org.v6elasticsearch.action.bulk.BulkRequest;
import org.v6elasticsearch.action.delete.DeleteRequest;
import org.v6elasticsearch.action.support.master.AcknowledgedResponse;


public class EsClient6 implements EsClient {
	
	private static final String TYPE = "doc";
	
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

	@Override
	public boolean delete(String...indices) throws IOException {
		DeleteIndexRequest temp = new DeleteIndexRequest(indices);
		AcknowledgedResponse response = client.indices().delete(temp);
		return response.isAcknowledged();
	}
	
	@Override
	public boolean exists(String... indices) throws IOException {
		GetIndexRequest request = new GetIndexRequest().indices(indices);
		return client.indices().exists(request);
	}

	@Override
	public SearchResponse search(String index, SearchSourceBuilder searchSourceBuilder) throws IOException {
		SearchModule sm = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser(new NamedXContentRegistry(sm.getNamedXContents()),
                              LoggingDeprecationHandler.INSTANCE,
                              searchSourceBuilder.toString());
        org.v6elasticsearch.search.builder.SearchSourceBuilder temp = new org.v6elasticsearch.search.builder.SearchSourceBuilder();
        temp.parseXContent(parser);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(temp).indicesOptions(IndicesOptions.lenientExpandOpen());
        org.v6elasticsearch.action.search.SearchResponse searchResponse =  client.search(searchRequest);
        
        
        org.elasticsearch.search.SearchModule sm2 = new org.elasticsearch.search.SearchModule(org.elasticsearch.common.settings.Settings.EMPTY, false, Collections.emptyList());
        org.elasticsearch.xcontent.XContentParser parser2 = org.elasticsearch.xcontent.XContentFactory.xContent(org.elasticsearch.xcontent.XContentType.JSON)
                .createParser(new org.elasticsearch.xcontent.NamedXContentRegistry(sm2.getNamedXContents()),
                              org.elasticsearch.common.xcontent.LoggingDeprecationHandler.INSTANCE,
                              searchResponse.toString());
        return SearchResponse.fromXContent(parser2);
	}

	@Override
	public Result bulk(Item...items) throws IOException {
		BulkRequest request = new BulkRequest();
		for(Item item : items) {
			if(item.method == Method.add) {
				request.add(new IndexRequest(item.index, TYPE, item.id).source(item.source, XContentType.JSON));
			}
			else {
				request.add(new DeleteRequest(item.index, TYPE, item.id));
			}
		}
		BulkResponse result = client.bulk(request);
		if(result.hasFailures()) {
			return new Result(true, result.buildFailureMessage());
		}
		return new Result(true, null);
	}
	
	@Override
	public List<String> mget(Item... items) throws IOException {
		MultiGetRequest request = new MultiGetRequest();
		for(Item item : items) {
			request.add(item.index, TYPE, item.id);
		}
		MultiGetResponse response = client.multiGet(request);
		List<String> list = new ArrayList<>();
		for (MultiGetItemResponse item : response.getResponses()) {
			list.add(item.getResponse().getSourceAsString());
		}
		return list;
	}
}
