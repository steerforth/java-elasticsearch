package com.steer.javaelasticsearch.client;

import com.steer.javaelasticsearch.util.ResourceFileUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class RestClientTest {
    private static Logger LOGGER = LoggerFactory.getLogger(RestClientTest.class);
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testInsertIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("hotel_index");

        request.source(ResourceFileUtil.getJson("index_create_hotel.json"), XContentType.JSON);

        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
    }

    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel_index");

        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);

        LOGGER.info("是否存在:"+response);
    }

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("hotel_index");

        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

        LOGGER.info(response.toString());
    }

    @Test
    public void test() throws IOException {
        SearchRequest request = new SearchRequest("user_index");
        //组织DSL参数
        request.source().query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
    }
}
