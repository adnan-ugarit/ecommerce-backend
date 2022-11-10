package com.adnan.lifecyclemanager.policies;

import java.util.Map;

public class TpmScalingPolicy implements ScalingPolicy {
    
    public boolean check(Map metrics) {
        if (metrics.containsKey("gauge_tpm")) {
            int tpm = (int) Double.parseDouble((String) metrics.get("gauge_tpm"));
            return (tpm > 10);
        }
        return false;
    }
    
}
