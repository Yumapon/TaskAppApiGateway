package com.yuma.apigateway.taskappapigateway.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CallApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private APIDestinations apiDestinations;

    public String getTasks(String email){

        System.out.println(email);

        //Headerの設定
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/json; charset=UTF-8");

        //URLの作成
        String url = "http://" + apiDestinations.getTaskapiurl() + "/yuma/task/getall"; 
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("email", email);

        //paramの設定
        Map<String, String> params = new HashMap<>();
        params.put("email", email);

        //Requestの実施
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity(httpHeaders),
                String.class
                );

        return response.getBody();
    }
    
}
