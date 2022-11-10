package com.adnan.lifecyclemanager.rules;

import org.springframework.stereotype.Component;

@Component
public class DeploymentRules {
    
    public DeploymentRule getDeploymentRule(String serviceId) {
        if (serviceId == "test-test") {
            return new DummyDeploymentRule();
        }
        return new DummyDeploymentRule();
    }
    
}
