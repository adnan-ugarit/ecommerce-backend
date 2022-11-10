# Ecommerce Backend in Microservices Architecture with REST API by Spring Boot & Cloud

## How to run?

### Build all modules:

`ecommerce-backend> ./build.sh`

### Start modules in docker:

`ecommerce-backend> ./run.sh`

## Servers & Services

* RabbitMQ:
     * hostname: rabbitmq
     * Ports: 5672:5672, 15672:15672 (<host_port>:<container_port>)
     * Management_UI: http://localhost:15672
     * Username/password: guest/guest

* config-server:
    * hostname: config-server
    * Ports: 8888:8888
    * URL: http://localhost:8888/

* eureka-cluster:
    * hostname: eureka-server
    * Ports: 8761:8761, 8762:8761, 8763:8761
    * URL: http://localhost:8761/
	* Username/password: admin/eureka

* user-service:
    * hostname: user-service
    * Ports: 8484:8484
    * URL: http://localhost:8484/

* auth-service:
    * hostname: auth-service
    * Ports: 8585:8585
    * URL: http://localhost:8585/

* catalog-service:
    * hostname: catalog-service
    * Ports: 8181:8181
    * URL: http://localhost:8181/

* cart-service:
    * hostname: cart-service
    * Ports: 8282:8282
    * URL: http://localhost:8282/

* order-service:
    * hostname: order-service
    * Ports: 8383:8383
    * URL: http://localhost:8383/

* bank-service:
    * hostname: bank-service
    * Ports: 8989:8989, 27017:27017
    * URL: http://localhost:8989/

* payment-service:
    * hostname: payment-service
    * Ports: 8686:8686
    * URL: http://localhost:8686/

* api-gateway:
    * hostname: api-gateway
    * Ports: 8080:8080
    * URL: http://localhost:8080/

* lifecycle-manager:
    * hostname: lifecycle-manager
    * Ports: 8787:8787
    * URL: http://localhost:8787/

* prometheus-monitoring:
    * hostname: prometheus-monitoring
    * Ports: 9090:9090
    * URL: http://localhost:9090/

* zipkin-server:
    * hostname: zipkin-server
    * Ports: 9411:9411
    * URL: http://localhost:9411/

* elk-stack:
    * hostname: elk-stack
    * Ports: 5601:5601, 9200:9200, 5044:5044
    * Elasticsearch_URL: http://localhost:9200/
	* Logstash_URL: http://localhost:5044/
	* Kibana_URL: http://localhost:5601/


## Main access for testing

**Local:** http://localhost:8080/swagger-ui.html

**Remote:** https://adnan-gateway.herokuapp.com/swagger-ui.html

## Admin account

* Email: `admin@gmail.com`
* Password: `admin`

## Contact

* Email: mohammadadnan.ataya@hiast.edu.sy
