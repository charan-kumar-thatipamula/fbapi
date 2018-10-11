package com.charan.fbgroup.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestApiManager {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T get(String baseUrl, String url, String query, HttpHeaders requestHeaders, Class<T> responseClassType) throws Exception{
        ResponseEntity<T> responseEntity = null;
        try {
            String fullUrl = baseUrl + url + query;
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
            responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, requestEntity, responseClassType);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                throw new Exception("Response other than 200 OK");
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.getMessage();
            String responseBody = httpClientErrorException.getResponseBodyAsString();
            System.out.println(responseBody);
            throw httpClientErrorException;
        } catch (Exception e) {
            throw e;
        }
    }
    public <T> T get(String fullUrl, HttpHeaders requestHeaders, Class<T> responseClassType) throws Exception{
        ResponseEntity<T> responseEntity = null;
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
            responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, requestEntity, responseClassType);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                throw new Exception("Response other than 200 OK");
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.getMessage();
            throw httpClientErrorException;
        } catch (Exception e) {
            throw e;
        }
    }
    public <T> T post(String graphUrl, String url, String query, Object body, HttpHeaders requestHeaders, Class<T> responseClassType) throws Exception{
        ResponseEntity<T> responseEntity = null;
        try {
            String fullUrl = graphUrl + "/" + url + "?" + query; // getFullUrl(baseUrl, url, query);
            String bodyJson = body.toString();
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(bodyJson, requestHeaders);
            responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, responseClassType);
            if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
                return responseEntity.getBody();
            } else {
                throw new Exception("Response other than 200 OK");
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            throw httpClientErrorException;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

//    private String getFullUrl(String baseUrl, String url, String query) {
//        StringBuilder fullUrl = new StringBuilder();
//        fullUrl.append(baseUrl);
//        if (url != null) {
//            fullUrl.append(url);
//        }
//        if (query != null && query.startsWith("?")) {
//            query = query.substring(1);
//        }
//        query = StringUtils.trimToNull(query);
//        if (query != null) {
//            fullUrl.append("?");
//            fullUrl.append(query);
//        }
//        return fullUrl.toString();
//    }

}
