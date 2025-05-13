## 高级功能探索 <!-- by 李杰东 -->

本章节展示超越基础功能的Spring Boot企业级增强能力，聚焦生产环境实战场景。

### 1. 企业级安全加固方案 <!-- by 李杰东 -->
实现OAuth2.0+JWT认证体系，构建API安全防护网。

**技术栈**：
- Spring Security 6.x
- JJWT令牌库
- OAuth2资源服务器

**配置步骤**：
```yaml
# application.yml 安全配置片段
security:
  oauth2:
    resourceserver:
      jwt:
        issuer-uri: https://auth-server.example.com <!-- [AI建议] 配置权威认证源 -->
        jwk-set-uri: ${issuer-uri}/.well-known/jwks.json


        @Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }
}


@RestController
public class ReactiveController {
    private final SensorRepository repository;

    public ReactiveController(SensorRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorData> streamData() {
        return repository.findAll()
            .delayElements(Duration.ofMillis(500))
            .log("sensor-stream", Level.FINE);
    }
}


<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>


spring:
  zipkin:
    base-url: http://localhost:9411
  sleuth:
    sampler:
      probability: 1.0 <!-- 演示用全量采样 -->

      
      chaos:
  monkey:
    enabled: true
    assaults:
      - level: 5
        latencyRangeStart: 1000
        latencyRangeEnd: 5000
        latencyActive: true


        # 8GB堆内存优化配置
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=35


spring:
  cache:
    cache-names: productCache,userCache
    caffeine:
      spec: maximumSize=500,expireAfterAccess=600s