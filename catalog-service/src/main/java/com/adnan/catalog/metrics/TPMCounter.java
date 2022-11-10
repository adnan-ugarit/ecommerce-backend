package com.adnan.catalog.metrics;

import java.util.Calendar;
import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Component;

@Component
public class TPMCounter {
    
    private LongAdder count;
    private Calendar expiry;

    public TPMCounter() {
        reset();
    }
    
    private void reset() {
        count = new LongAdder();
        expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, 1);
    }
    
    private boolean isExpired() {
        return Calendar.getInstance().after(expiry);
    }
    
    public int increment() {
        if (isExpired()) {
            reset();
        }
        count.increment();
        return count.intValue();
    }
    
}
