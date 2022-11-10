package com.adnan.gateway.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary //Override original swagger implementation with custom swagger resource provider.
public class DocumentationConfig implements SwaggerResourcesProvider {
    
    @Override
    public List get() {
        List resources = new ArrayList();
        resources.add(swaggerResource("catalog-service", "/api/catalog-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("cart-service", "/api/cart-servic/v2/api-docs", "2.0"));
        resources.add(swaggerResource("order-service", "/api/order-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("user-service", "/api/user-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("auth-service", "/api/auth-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("payment-service", "/api/payment-service/v2/api-docs", "2.0"));
        return resources;
    }
    
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
    
}
