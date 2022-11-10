package com.adnan.lifecyclemanager.engines;

import com.adnan.lifecyclemanager.rules.DeploymentRules;
import com.adnan.lifecyclemanager.policies.ScalingPolicies;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DecisionEngine {
    
    @Autowired
    ScalingPolicies scalingPolicies;
    
    @Autowired
    DeploymentEngine deploymentEngine;
    
    @Autowired
    DeploymentRules deploymentRules;
    
    public void execute(String serviceId, Map metrics) {
        if (scalingPolicies.getPolicy(serviceId).check(metrics)) {
            deploymentEngine.scaleUp(deploymentRules.getDeploymentRule(serviceId), serviceId);
            try {
                //Pause for ten minutes
                Thread.sleep(600000);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(DecisionEngine.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }
        else {
            deploymentEngine.scaleDown(serviceId);
        }
    }
    
}
