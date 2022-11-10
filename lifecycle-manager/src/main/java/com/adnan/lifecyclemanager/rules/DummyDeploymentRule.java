package com.adnan.lifecyclemanager.rules;

import com.adnan.lifecyclemanager.engines.DeploymentEngine;

public class DummyDeploymentRule implements DeploymentRule {
    
    private static final int MAX_INSTANCES = 2;
    
    public boolean execute(){
        if (DeploymentEngine.num_instances < MAX_INSTANCES)
            return true;
        return false;
    }
    
}
