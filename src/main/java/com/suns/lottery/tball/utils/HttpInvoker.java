package com.suns.lottery.tball.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class HttpInvoker {

    @Autowired
    private RestTemplate template;

    public <T> T get(String url, Class<T> clazz, Map<String, ?> urlVariables){
        ResponseEntity<T> response= http(HttpMethod.GET,url,null,clazz,urlVariables);
        log.info("http request [{}] recived a response ------> {}",url, JSONObject.toJSONString(response));
        return response.getBody();
    }
    public <T> T get(String url, Class<T> clazz, MultiValueMap<String, String> header, MultiValueMap<String,String> body){
        ResponseEntity<T> response= http(HttpMethod.GET,url,new HttpEntity<>(body,header),clazz,new HashMap<>(2));
        log.info("http request [{}] recived a response -------> {}",url, JSONObject.toJSONString(response));
        return response.getBody();
    }

    public <T> T post(String url, Class<T> clazz, HttpEntity<?> requestEntity, Map<String, ?> urlVariables){
        ResponseEntity<T> response= http(HttpMethod.POST,url,requestEntity,clazz,urlVariables);
        log.info("http request [{}] recived a response -------> {}",url, JSONObject.toJSONString(response));
        return response.getBody();
    }

    public <T> T post(String url, Class<T> clazz, HttpEntity<?> requestEntity){
        ResponseEntity<T> response= http(HttpMethod.POST,url,requestEntity,clazz,new HashMap<>(4));
        log.info("http request [{}] recived a response -------> {}",url, JSONObject.toJSONString(response));
        return response.getBody();
    }

    public <T> ResponseEntity<T> http(HttpMethod method, String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables){
        ResponseEntity<T> result = template.exchange(url,method,requestEntity,responseType,uriVariables);
        return result;
    }

}
