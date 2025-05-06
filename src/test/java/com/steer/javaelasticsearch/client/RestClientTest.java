package com.steer.javaelasticsearch.client;

import com.steer.javaelasticsearch.config.Constants;
import com.steer.javaelasticsearch.util.ResourceFileUtil;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class RestClientTest {
    private static Logger LOGGER = LoggerFactory.getLogger(RestClientTest.class);
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testInsertIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(Constants.HOTEL_INDEX);

        request.source(ResourceFileUtil.getJson("index_create_hotel.json"), XContentType.JSON);

        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
    }

    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(Constants.HOTEL_INDEX);

        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);

        LOGGER.info("是否存在:"+response);
    }

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(Constants.HOTEL_INDEX);

        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

        LOGGER.info(response.toString());
    }

    @Test
    public void testInsertDocument() throws IOException {
        IndexRequest request = new IndexRequest(Constants.HOTEL_INDEX).id("1");
        //组织DSL参数
        request.source(ResourceFileUtil.getJson("doc_create_hotel.json"), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
    }

    @Test
    public void testBatchInsertDoc() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(Constants.HOTEL_INDEX).id("2").source("{\n" +
                "  \"id\": \"2\",\n" +
                "  \"name\":\"杭州如家连锁酒店\",\n" +
                "  \"city\":\"杭州\",\n" +
                "  \"price\":\"200\"\n" +
                "}",XContentType.JSON));
        request.add(new IndexRequest(Constants.HOTEL_INDEX).id("3").source("{\n" +
                "  \"id\": \"3\",\n" +
                "  \"name\":\"杭州1号城市酒店\",\n" +
                "  \"city\":\"杭州\",\n" +
                "  \"price\":\"1200\"\n" +
                "}",XContentType.JSON));

        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
    }

    @Test
    public void testGetDocumentById() throws IOException {
        GetRequest request = new GetRequest(Constants.HOTEL_INDEX,"1");
        //组织DSL参数
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        LOGGER.info(response.toString());
        LOGGER.info(response.getSourceAsString());
    }

    @Test
    public void testUpdateDocumentById() throws IOException {
        UpdateRequest request = new UpdateRequest(Constants.HOTEL_INDEX,"1");
        request.doc("price",656,"starName","四钻");//语法特殊
        //组织DSL参数
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
    }

    @Test
    public void testDeleteDocumentById() throws IOException {
        DeleteRequest request = new DeleteRequest(Constants.HOTEL_INDEX,"1");
        //组织DSL参数
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
    }

    @Test
    public void testMatchAll() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        request.source().query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());
        }
    }

    @Test
    public void testMatch() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        request.source().query(QueryBuilders.matchQuery("name","如家"));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());
        }
    }

    @Test
    public void testMultiMatch() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        request.source().query(QueryBuilders.multiMatchQuery("如家","name","branch"));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());
        }
    }

    @Test
    public void testTermAndRangeQuery() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        //整理条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.termQuery("city","杭州"));
        builder.filter(QueryBuilders.rangeQuery("price").gte(400).lte(1300));

//        request.source().query(QueryBuilders.termQuery("city","杭州"));
//        request.source().query(QueryBuilders.rangeQuery("price").gte(400).lte(1000));
        request.source().query(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());
        }
    }

    @Test
    public void testPageAndSort() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        request.source().query(QueryBuilders.matchAllQuery());
        request.source().sort("price", SortOrder.DESC);//先排序，后分页
        request.source().from(0).size(2);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);//这里指的是查询的总数，而非分页后取的size
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());
        }
    }

    @Test
    public void testHighlight() throws IOException {
        SearchRequest request = new SearchRequest(Constants.HOTEL_INDEX);
        request.source().query(QueryBuilders.matchQuery("name","如家"));
        //require_field_match 参数用于确保查询中必须包含某些字段
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LOGGER.info(">>>"+response.toString());
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        LOGGER.info(">>>总计数量："+total);
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits){
            LOGGER.info(">>>"+hit.getSourceAsString());

            //高亮部分解析
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("name")){
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField.getFragments().length > 0) {
                    Text name = highlightField.getFragments()[0];
                    LOGGER.info(">>>高亮内容：" + name);
                }else{
                    LOGGER.info("无高亮内容" );
                }
            }else{
                LOGGER.info("无高亮字段" );
            }

        }
    }
}
