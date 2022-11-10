package com.adnan.lifecyclemanager.engines;

import com.adnan.lifecyclemanager.rules.DeploymentRule;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeploymentEngine {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentEngine.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${deployment.host.ip}")
    private String HOST_IP;
    
    @Value("${deployment.host.user.name}")
    private String username;
    
    @Value("${deployment.host.user.password}")
    private String password;
    
    //The number of used instances for scaling
    public static int num_instances = 0;
    
    public void scaleUp(DeploymentRule rule, String serviceId) {
        if(!rule.execute())
            return;
        num_instances++;
        String command = "./Desktop/auto-scaling/start.sh "+ serviceId +" "+ password;
        Runnable runnable = () -> {
            LOGGER.info("Running new instance for {}", serviceId);
            executeSSH(command);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    private void executeSSH(String command) {
        try {
            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session=jsch.getSession(username, HOST_IP, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected to " + HOST_IP);
            ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            channelExec.disconnect();
            session.disconnect();
            System.out.println("Done!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void scaleDown(String serviceId) {
        if(num_instances == 0)
            return;
        num_instances--;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json");
        headers.setContentType(type);
        HttpEntity entity = new HttpEntity(headers);
        LOGGER.info("Shutting down {}", serviceId);
        restTemplate.postForObject("http://"+serviceId+"/actuator/shutdown", entity, Void.class);
    }
    
}
