package com.adnan.lifecyclemanager.policies;

import org.springframework.stereotype.Component;

@Component
public class ScalingPolicies {
    
    public ScalingPolicy getPolicy(String serviceId) {
        if (serviceId == "test-test") {
            return new TpmScalingPolicy();
        }
        return new TpmScalingPolicy();
    }
    
}
