/*
package com.hmdp.pojo.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(indexName = "my_index") // 对应你的索引名
public class System {
    @Id
    private String id; // ES 中的 _id

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}*/
