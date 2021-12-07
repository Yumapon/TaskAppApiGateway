package com.yuma.apigateway.taskappapigateway.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "api.destinations")
public class APIDestinations {

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.taskapiurl.getall}")
    private String taskapiurlGetall;//taskappのURL

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.taskapiurl.get}")
    private String taskapiurlGet;//taskappのURL

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.taskapiurl.delete}")
    private String taskapiurlDelete;//taskappのURL

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.taskapiurl.change}")
    private String taskapiurlChange;//taskappのURL

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.taskapiurl.add}")
    private String taskapiurlAdd;//taskappのURL

    @NonNull
    @Getter
    @Setter
    @Value("${api.destinations.dnmonster.get}")
    private String dnmonsterGet;//taskappのURL

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
