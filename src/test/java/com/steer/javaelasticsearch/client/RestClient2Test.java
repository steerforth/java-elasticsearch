package com.steer.javaelasticsearch.client;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
public class RestClient2Test {
    private static Logger LOGGER = LoggerFactory.getLogger(RestClient2Test.class);

    private RestHighLevelClient client;

    @BeforeEach
    public void init(){
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.96.144:9200")));
    }

    @AfterEach
    public void close() throws IOException {
        this.client.close();
    }

    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel_index");

        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);

        LOGGER.info("是否存在:"+response);
    }
}
