package com.steer.javaelasticsearch.entity;
public class User {

}
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
///**
// * createIndex=true 启动就可将实体类变成索引注入es中
// *
// * shards,replicas配置是哪个版本的？
// */
//@Document(indexName = "user_index",createIndex = true)
//public class User {
//    @Id
//    @Field(type = FieldType.Long)
//    private Long id;
//    //不创建索引
//    @Field(type = FieldType.Keyword,index = false)
//    private String name;
//    @Field(type = FieldType.Text,analyzer = "ik_max_word")
//    private String addr;
//    @Field(type = FieldType.Integer,index = false)
//    private int age;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public String getAddr() {
//        return addr;
//    }
//
//    public void setAddr(String addr) {
//        this.addr = addr;
//    }
//}
