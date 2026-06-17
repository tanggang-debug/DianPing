/*
package com.daren.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                // 核心：让 ES 8 用 7.x 的数据格式说话
                .withDefaultHeaders(new HttpHeaders() {{
                    add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
                    add("Content-Type", "application/vnd.elasticsearch+json;compatible-with=7");
                }})
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}*/
