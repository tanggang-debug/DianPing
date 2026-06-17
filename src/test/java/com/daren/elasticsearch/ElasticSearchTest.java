//package com.hmdp.elasticsearch;
//
//import co.elastic.clients.elasticsearch.ingest.simulate.Document;
//import co.elastic.clients.json.JsonData;
//import com.hmdp.pojo.es.System;
//import org.apache.lucene.util.QueryBuilder;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.aggregations.metrics.Max;
//import org.elasticsearch.search.sort.FieldSortBuilder;
//import org.elasticsearch.search.sort.SortOrder;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.system.SystemProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.IndexOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.Query;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@SpringBootTest
//public class ElasticSearchTest {
//    @Autowired
//    private ElasticsearchOperations esOperations;
//
//    @Test
//    void testMyFirstDSL() {
//        // 1. 创建 POJO 实例并填充数据
//        System order = new System();
//        order.setTitle("Java 高并发秒杀系统实战");
//        order.setTags(Arrays.asList("Java", "Redis"));
//        order.setPrice(199.0);
//        order.setCreatedAt(LocalDateTime.now());
//
//        // 2. 发送出去！
//        // save 方法会自动判断：如果 ID 已存在则更新，不存在则新增
//        esOperations.save(order);
//        java.lang.System.out.println("文档已成功发送至 ES！");
//    }
//
//    @Test
//    void testSaveAll() {
//        // 1. 创建 POJO 实例并填充数据
//        ArrayList<System> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            System order = new System();
//            order.setId(i + "");
//            order.setTitle("Java 高并发秒杀系统实战" + i);
//            order.setTags(Arrays.asList("Java", "Redis"));
//            order.setPrice(i + .0);
//            order.setCreatedAt(LocalDateTime.now());
//            list.add(order);
//        }
//        // 2. 发送出去！
//        // save 方法会自动判断：如果 ID 已存在则更新，不存在则新增
//        esOperations.save(list);
//        java.lang.System.out.println("文档已成功发送至 ES！");
//    }
//
//    @Test
//    void CreateIndex() {
//        // 2. 发送出去！
//        // save 方法会自动判断：如果 ID 已存在则更新，不存在则新增
//        IndexOperations indexOperations = esOperations.indexOps(System.class);
//        boolean b = indexOperations.create();
//        indexOperations.putMapping(System.class);
//        java.lang.System.out.println(b);
//    }
//
//    @Test
//    void GetIndex() {
//        // 2. 发送出去！
//        // save 方法会自动判断：如果 ID 已存在则更新，不存在则新增
//        IndexOperations indexOperations = esOperations.indexOps(System.class);
////        boolean b = indexOperations.create();
//        System system = esOperations.get("1001", System.class);
//        java.lang.System.out.println(system);
////        java.lang.System.out.println(b);
//    }
//
//    @Test
//    void GetAll() {
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q.matchAll(m -> m))
//                .withPageable(PageRequest.of(0, 100)) // 获取前50条
//                .build();
//        SearchHits<System> search = esOperations.search(query, System.class);
//        search.getSearchHits().forEach(hit -> {
//            System system = hit.getContent();
//            java.lang.System.out.println(system);
//        });
//    }
//
//    @Test
//    void testSearch() {
//        // 1. 使用老版本的构建器 NativeSearchQueryBuilder
//        // 1. 构建 BoolQueryBuilder
//        // 2. 放入 NativeSearchQuery
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.rangeQuery("price").gte("10").lt("100"))
//                .withPageable(PageRequest.of(0, 100))
//                .build();
//        // 2. 执行查询
//        SearchHits<System> searchHits = esOperations.search(searchQuery, System.class);
//        List<SearchHit<System>> searchHits1 = searchHits.getSearchHits();
//        for (SearchHit<System> item : searchHits1) {
//            java.lang.System.out.println(item.getContent());
//        }
//    }
//
//    @Test
//    void testSort() {
//        // 1. 使用老版本的构建器 NativeSearchQueryBuilder
//        // 1. 构建 BoolQueryBuilder
//        // 2. 放入 NativeSearchQuery
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withSort(new FieldSortBuilder("price").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(0, 100))
//                .build();
//        // 2. 执行查询
//        SearchHits<System> searchHits = esOperations.search(searchQuery, System.class);
//        List<SearchHit<System>> searchHits1 = searchHits.getSearchHits();
//        for (SearchHit<System> item : searchHits1) {
//            java.lang.System.out.println(item.getContent());
//        }
//    }
//
//    @Test
//    void testMaxPricePerBrand() {
//        // 1. 构建嵌套聚合：brand分组 -> price最大值
//        AbstractAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg")
//                .field("createdAt")
//                .subAggregation(AggregationBuilders.max("max_price").field("price"));
//
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchAllQuery())
//                .addAggregation(brandAgg)
//                .withPageable(Pageable.unpaged())
//                .build();
//
//        // 2. 执行并解析
//        SearchHits<System> hits = esOperations.search(searchQuery, System.class);
//        Aggregations aggs = (Aggregations) hits.getAggregations().aggregations();
//
//        // 3. 解析桶数据
//        Terms terms = aggs.get("brand_agg");
//        for (Terms.Bucket bucket : terms.getBuckets()) {
//            Max max = bucket.getAggregations().get("max_price");
//            java.lang.System.out.println(bucket.getKeyAsString() + " 品牌的最高价是: " + max.getValue());
//        }
//    }
//}
