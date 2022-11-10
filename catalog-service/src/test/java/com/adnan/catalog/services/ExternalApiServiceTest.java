package com.adnan.catalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExternalApiService.class})
@ExtendWith(SpringExtension.class)
public class ExternalApiServiceTest {
    @Autowired
    private ExternalApiService externalApiService;

    @Test
    public void testConsumeProducts() {
        assertNotEquals(0, this.externalApiService.consumeProducts().size());
    }

    @Test
    public void testGetFallbackProducts() {
        assertEquals(0, this.externalApiService.getFallbackProducts().size());
    }
}

