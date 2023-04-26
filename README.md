
#### Micro Services

(1) Discovery Server/Client & Load Balance
Netflix Eureka
-Eureka Server
-Eureka Client (@LoadBalanced)

(2) Config Server
    - To store/provide microservice-name.yml on Git repo. back to microservices
        . Spring Cloud Configuration Server/ Apache Zookeeper / Hashicorp Consul

    @SpringBootApplication
    @EnableConfigServer
    - Externalized             (Property files)
    - Environment Specific     (Spring profile)
    - Consistency               (Spring cloud config server)     
    - Version history           (Git repo resource)
    - Real-time management      (Refreshing properties at runtime)
        . Add Actuator
            To refresh @RefreshScope (at Config Client)
            POST: http://localhost:8080/actuator/refresh

        . Add property:
            management.endpoints.web.exposure.include="*"
        . Add @RefreshScope to (Class level) where the code wants to get the properties 

    https://github.com/snguyenkim/config-repo.git
    ssh:git@github.com:snguyenkim/config-repo.git

    spring.cloud.config.server.git.uri=ssh://localhost/config-repo
    spring.cloud.config.server.git.clone-on-start=true
    spring.security.user.name=root
    spring.security.user.password=s3cr3t

        /{application}/{profile}[/{label}]
        /{application}-{profile}.yml
        /{application}-{profile}.properties
        /{label}/{application}-{profile}.yml
        /{label}/{application}-{profile}.properties
        
        The {label} placeholder refers to a Git branch, 
        {application} to the client's application name, and 
        the {profile} to the client's current active application profile.


    (*) Config Client
        spring-cloud-starter-config
        @RefreshScope

    Properties:
        spring.cloud.config.uri = <Config Server>

---

bootstrap.properties
-server.port = 8888
-spring.cloud.config.server.native.searchLocations=file:///C:/configprop/
-SPRING_PROFILES_ACTIVE=native

in file:///C:/configprop/ place your client application - application.properties file

- inventory-service.properties
- order-service.properties

(2) API Gateway - Spring Cloud Gateway
    What is API gateway in spring boot?
    Spring Cloud Gateway works by sitting between your API clients and your backend services. 
    It acts as a reverse proxy, routing requests from clients to the appropriate backend service 
    and then returning the service's response back to the client.
    
NOTE: WebFlux
    Spring Cloud Gateway provides a library for building an API Gateway on top of Spring WebFlux.

Abilities:
. Routing based on Request header (/api/inventory , /api/order, /eureka, ...)
. Authentication 
    -Authentication verifies the identity of a user or service, 
    -Authorization determines their access rights.

. Security (JWT, ...)
. Load Balancing
. SSL Termination (Meaning terminate SSL when using internal API)

Features:
    1. Route:
    The basic building block of the gateway. It is defined by an ID, a destination URI, a collection of predicates,
    and a collection of filters. A route is matched if the aggregate predicate is true.
    2. Predicate:
    This is a Java 8 Function Predicate. The input type is a Spring Framework ServerWebExchange.
    This lets you match on anything from the HTTP request, such as headers or parameters.
    3. Filter:
    These are instances of GatewayFilter that have been constructed with a specific factory.
    Here, you can modify requests and responses before or after sending the downstream request.

(3) Circuit Breaker
Resilience4J, Netflix Hytrix
Use of the Circuit Breaker pattern can allow a microservice to continue operating when a related service fails, 
preventing the failure from cascading and giving the failing service time to recover.

@CircuitBreaker : to apply circuit breaker rules
resilience4j.circuitbreaker.instances.<service-name>.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.CircuitBreakerService.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.CircuitBreakerService.automatic-transition-from-open-to-half-open-enabled=true
resilience4j.circuitbreaker.instances.CircuitBreakerService.wait-duration-in-open-state=5s
resilience4j.circuitbreaker.instances.CircuitBreakerService.permitted-number-of-calls-in-half-open-state=3
resilience4j.circuitbreaker.instances.CircuitBreakerService.sliding-window-size=10
resilience4j.circuitbreaker.instances.CircuitBreakerService.sliding-window-type=count_based

@TimeLimiter(name = "timeLimiterApi") : to apply timeout rules
resilience4j.timelimiter.metrics.enabled=true
resilience4j.timelimiter.instances.timeLimiterApi.timeout-duration=2s
resilience4j.timelimiter.instances.timeLimiterApi.cancel-running-future=true

-> HttpStatus.REQUEST_TIMEOUT

@Retry : to apply retry rules
resilience4j.retry.instances.retryApi.max-attempts=3
resilience4j.retry.instances.retryApi.wait-duration=1s
resilience4j.retry.metrics.legacy.enabled=true
resilience4j.retry.metrics.enabled=true

---

@GetMapping("/retry")
@Retry(name = "retryApi", fallbackMethod = "fallbackAfterRetry")
public String retryApi() {
return externalAPICaller.callApi();
}
public String fallbackAfterRetry(Exception ex) {
return "all retries have exhausted";
}


#### Service Discovery - spring-cloud-netflix Eureka Server.

    @EnableEurekaServer
    eureka.instance.hostname = localhost
    eureka.client.register-with-eurake = false
    eureka.client.fetch-registry = false
    server.port = 8761

                                Inventory Service (1) - Down ?

    Order Service ------->      Inventory Service (2) - Up ?

                                Inventory Service (3) - Up ?

    (1) Discovery Server

        Service Name    -   IP Address
        Service Name    -   IP Address
        ...

    (2) Steps
        - Order Service ---- Where is Inventory Service         ----> Discovery Server
                        <--- Here you are Inventory Service (2) -----

                        ----> Send request to Inventory Service (2) ----> Inventory Service (2)

    (3) Eureka UI
        http://localhost:8761

#### Add Eureka-Client into services - spring-cloud-netflix Eureka Client.

    @EnableEurekaClient (Application)
    property
        spring.application.name = service-name
        eureka.client.serviceUrl.defaultZone = http://localhost:8761/eureka

    (1) Define WebClient with fix IP
    @Configuration
    Public class WebClientCofig {
        @Bean
        public WebClient webClient() {
            returb WebClient.builder().build();
        }
    }

    Use:
        @Autowired
        private WebClient webClient;

        webClient.get().uri("http://localhost:9001/api/inventory").retrieve()....

    (2) Define WebClient with service-name
    @Configuration
    @LoadBalanced
    Public class WebClientCofig {
        @Bean
        public WebClient.Builder webClientBuilder() {
            returb WebClient.builder();
        }
    }

    Use:
        @Autowired
        private WebClient.Builder webClientBuilder;

        webClient.build().get().uri("http://inventory-service/api/inventory").retrieve()....

#### API Gateway

    Coonect to Discovery Server
    eureka.client.serviceUrl.defaultZone = http://eureka:password@localhost:8761/eureka
    spring.application.name = api-gateway
    logging.level.root = INFO
    logging.level.org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator = INFO
    logging.level.org.springframework.cloud.gateway = TRACE

    (1) Config properties
    spring.cloud.gateway.routes[0].id = inventory-service
    spring.cloud.gateway.routes[0].uri = lb://inventory-service
    spring.cloud.gateway.routes[0].predicates[0] = Path=/api/inventory


    (2) WIth filter http://localhost:8080/eureka/web -> http://localhost:8761/
    spring.cloud.gateway.routes[1].id = discovery-server
    spring.cloud.gateway.routes[1].uri = lb://localhost:8761
    spring.cloud.gateway.routes[1].predicates[0] = Path=/eureka/web
    spring.cloud.gateway.routes[1].filters[0] = SetPath=/

    spring.cloud.gateway.routes[2].id = discovery-server-static
    spring.cloud.gateway.routes[2].uri = lb://localhost:8761
    spring.cloud.gateway.routes[2].predicates[0] = Path=/eureka/**

#### Security - Authenticate Server - KeyCloak

    Ref.: Spring Security OAuth2 Tutorial Using KEYCLOAK

#### Circuit Breaker - Spring Cloud Circuit Breaker

    Resilience4J - Netflix Hytrix

    API service (Ex.: Order Service)
    Add pom:    spring cloud circuit breaker - resielence4j
                spring boot actuator
    Config:
        # actuator
        management.health.circuitbreakers.enable = true
        management.endpoints.web.exposure.include = *
        management.endpoint.health.show-details = always

        # circuit breaker apply for Inventory service calls in Order Service
        resielence4j.circuitbreaker.instances.inventory.regiserHealthIndicator=true
        resielence4j.circuitbreaker.instances.inventory.event-consumer-buffer-size=10
        resielence4j.circuitbreaker.instances.inventory
        resielence4j.circuitbreaker.instances.inventory

    1. Where to ADD Circuit Breaker in CODE
        - Controller
            @CircuitBreaker(name = "inventory" , fallbackMethod = "fallbackOrderPlace") before API
            @PostMapping
            public .... placeOrder(@RequestBody OrderRequest orderRequest) {

            }

            public fallbackOrderPlace

        - Service
            CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(70.0f)
                .build();

            CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
            CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

            Supplier<List<Flight>> flightsSupplier = () -> service.searchFlights(request);
            Supplier<List<Flight>> decoratedFlightsSupplier = circuitBreaker.decorateSupplier(flightsSupplier);
            for (int i=0; i<20; i++) {
                try {
                    System.out.println(decoratedFlightsSupplier.get());
                }
                catch (...) {
                    // Exception handling
                }
            }

        # resielence4j Timeout
        @TimeLimiter(name="inventory")

#### Distributed Tracing - Spring Cloud Sleuth (with sleuth-zipkin)

    From BEGINNING to END of a Request
    traceID (span 1, span 2, ... )
