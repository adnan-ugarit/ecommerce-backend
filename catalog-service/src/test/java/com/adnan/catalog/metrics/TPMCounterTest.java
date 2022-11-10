package com.adnan.catalog.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TPMCounter.class})
@ExtendWith(SpringExtension.class)
public class TPMCounterTest {
    @Autowired
    private TPMCounter tPMCounter;

    @Test
    public void testIncrement() {
        this.tPMCounter.increment();
        assertEquals(2, this.tPMCounter.increment());
    }
}

