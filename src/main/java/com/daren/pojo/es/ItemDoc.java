/*
package com.daren.pojo.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "auto_index")
public class ItemDoc {
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    
    @Field(type = FieldType.Double)
    private Double price;
    
    @Field(type = FieldType.Keyword)
    private List<String> tags;

    // 新增补全字段
    @CompletionField(analyzer = "ik_smart")
    private List<String> suggestion; 

    */
/**
     * 在数据同步时调用，整合补全词
     *//*

    public void createSuggestions() {
        this.suggestion = new ArrayList<>();
        // 将标题放入补全列表
        this.suggestion.add(this.title);
        // 将所有标签也放入补全列表
        if (this.tags != null) {
            this.suggestion.addAll(this.tags);
        }
    }
}*/
