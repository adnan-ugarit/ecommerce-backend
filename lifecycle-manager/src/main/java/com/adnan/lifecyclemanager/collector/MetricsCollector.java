package com.adnan.lifecyclemanager.collector;

import com.adnan.lifecyclemanager.engines.DecisionEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class MetricsCollector {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollector.class);
    
    @Autowired
    DiscoveryClient eurekaClient;
    
    @Autowired
    DecisionEngine decisionEngine;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public void start() {
        while (true) {
            eurekaClient.getServices().forEach(service -> {
                LOGGER.info("Fetching metrics from {} using RestTemplate", service);
                File file = restTemplate.execute("http://"+service+"/actuator/prometheus", HttpMethod.GET, null, clientHttpResponse -> {
                    File ret = File.createTempFile("download", "tmp");
                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                    return ret;
                });
                Map<String, String> metrics = new HashMap<>();
                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (!line.startsWith("#")) {
                            String[] metric = line.split(" ");
                            metrics.put(metric[0], metric[1]);
                        }
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                decisionEngine.execute(service, metrics);
            });
            try {
                //Pause for half minute
                Thread.sleep(30000);
            }
            catch (InterruptedException ex) {
                LOGGER.error("{}", ex);
                Thread.currentThread().interrupt();
            }
        }
    }
    
}
