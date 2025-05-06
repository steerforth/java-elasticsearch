package com.steer.javaelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * ElasticsearchTemplate ES7中废弃，不建议使用。基于transportClient
 * ElasticsearchRestTemplate 推荐使用，基于RestHignLevelClient
 */
@SpringBootApplication
public class JavaElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaElasticsearchApplication.class, args);
    }

}
