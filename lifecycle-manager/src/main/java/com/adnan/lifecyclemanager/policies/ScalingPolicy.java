package com.adnan.lifecyclemanager.policies;

import java.util.Map;

public interface ScalingPolicy {
    
    public boolean check(Map metrics);
    
}
