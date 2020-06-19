package com.txs.springbootes.es.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data  //生成get set tostring
//@NoArgsConstructor  //生成无参构造方法
@AllArgsConstructor //有参构造
@Document(indexName = "txs_user",shards = 3,replicas = 1)
public class User {

    @Id
    private Long id;

    @Field(type= FieldType.Text,analyzer = "ik_max_word")
    private String name;

    @Field(type= FieldType.Integer)
    private Integer age;

    @Field(type= FieldType.Text,analyzer = "ik_max_word")
    private String desc;
}
