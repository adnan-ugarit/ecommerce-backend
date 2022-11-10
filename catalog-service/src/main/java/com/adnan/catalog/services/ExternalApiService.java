package com.adnan.catalog.services;

import com.adnan.catalog.dto.ExternalProductDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalApiService.class);
    
    @HystrixCommand(fallbackMethod = "getFallbackProducts",
            commandProperties = {
                 @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            }
    )
    public List<ExternalProductDto> consumeProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity entity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExternalProductDto[]> response = restTemplate.exchange("https://fakestoreapi.com/products", HttpMethod.GET, entity, ExternalProductDto[].class);
        return Arrays.asList(response.getBody());
    }
    
    List<ExternalProductDto> getFallbackProducts() {
        LOGGER.info("Returning fallback for external products");
        return new ArrayList<>();
    }
    
}
