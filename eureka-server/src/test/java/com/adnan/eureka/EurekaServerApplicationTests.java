package com.adnan.eureka;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class EurekaServerApplicationTests {
    
        @Value("${server.port}")
	private int port;

	@Test
	void contextLoads() {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> entity = new TestRestTemplate("admin", "eureka").getForEntity("http://localhost:" + port + "/actuator/env", Map.class);
            assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

}
