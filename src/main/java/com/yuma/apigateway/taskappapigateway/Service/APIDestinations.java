package com.yuma.apigateway.taskappapigateway.Service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "taskapp.destinations")
public class APIDestinations {

    @NonNull
    @Getter
    @Setter
    private String taskapiurl;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
