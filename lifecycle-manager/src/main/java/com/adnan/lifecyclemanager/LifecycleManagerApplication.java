package com.adnan.lifecyclemanager;

import com.adnan.lifecyclemanager.collector.MetricsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class LifecycleManagerApplication implements CommandLineRunner {

	@Autowired
	MetricsCollector metricsCollector;
        
        public static void main(String[] args) {
		SpringApplication.run(LifecycleManagerApplication.class, args);
	}
        
        @Override
	public void run(String... arg0) throws Exception {
		metricsCollector.start();
	}
        
        @Bean
        @LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
