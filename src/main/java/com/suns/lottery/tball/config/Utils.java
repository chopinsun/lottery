package com.suns.lottery.tball.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 18:15
 **/
@Configuration
public class Utils {

    @Bean
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter,
                                     StringHttpMessageConverter stringHttpMessageConverter){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(1000);
        manager.setDefaultMaxPerRoute(1000);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(manager).build();
        HttpComponentsClientHttpRequestFactory proxy = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate template = new RestTemplate(proxy);
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);
        converters.add(new BufferedImageHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        template.setMessageConverters(converters);

        return template;
    }


    @Bean
    public MappingJackson2HttpMessageConverter newMappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        MediaType[] mediaTypes = new MediaType[]{
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_STREAM_JSON,
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_PDF,
        };
        converter.setSupportedMediaTypes(Arrays.asList(mediaTypes));
        return converter;
    }

    @Bean
    public StringHttpMessageConverter newStringHttpMessageConverter(){
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.TEXT_PLAIN);
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(list);
        return converter;
    }
}
