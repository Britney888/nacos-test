# nacos踩坑日记

## Spring cloud Alibaba Nacos服务注册发现和配置中心

版本注意：
  孵化版本（groupId为org.springframework.cloud）：
      spring-cloud-alibaba-dependencies版本0.9.0：与Spring Cloud Greenwich兼容
      spring-cloud-alibaba-dependencies版本0.2.2：与Spring Cloud Finchley兼容
      且若想支持动态刷新配置，Spring Boot必须引用2.0.X版本系列的，大于或者等于2.1.X版本的暂时不支持动态刷新配置！

  毕业版本（groupId为com.alibaba.cloud，目前基本用这个了）：
      spring-cloud-alibaba-dependencies版本2.1.0.RELEASE：与Spring Cloud Greenwich兼容
      spring-cloud-alibaba-dependencies版本2.2.0.RELEASE：与Spring Cloud Hoxton兼容

### 创建服务提供者

springcloud未集成nacos需要手动添加maven依赖

```java
 <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>nacos-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>nacos-server</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-cloud-alibaba.version>2.2.0.RELEASE</spring-cloud-alibaba.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- nacos配置中心-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
           <!-- <version>${spring-cloud-alibaba.version}</version>-->
        </dependency>
        <!-- nacos注册中心-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
           <!-- <version>${spring-cloud-alibaba.version}</version>-->
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>1.2.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
                <exclusions>
                    <exclusion>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>nacos-client</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

启动器上加上服务发现注解

```java
@EnableDiscoveryClient    //服务发现
@EnableFeignClients("com.example.democlient.client")//消费者
@SpringBootApplication
public class NacosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }

```

**`@EnableDiscoveryClient`开启Spring Cloud的服务注册与发现**

创建配置文件`bootstrap.properties`，并配置服务名称和Nacos地址

```
spring.application.name=nacos-server
server.port=8622
spring.cloud.nacos.config.server-addr=master:8848
spring.cloud.nacos.discovery.server-addr=master:8848
```

注意:

**注意**：这里必须使用`bootstrap.properties`或者`bootstrap.yaml`。同时，`spring.application.name`值必须与上一阶段Nacos中创建的配置Data Id匹配（除了.properties或者.yaml后缀）。

在bootstrap.properties中配置需要写成：
spring.cloud.nacos.discovery.server-addr=xxx.xxx.xxx.xxx:8848
spring.cloud.nacos.config.server-addr=xxx.xxx.xxx.xxx:8848
缺任意一个就会报错

缺少discovery服务发现的地址配置server-addr报错

```java
2021-03-02 17:42:30.958 ERROR 6824 --- [ main] com.alibaba.nacos.client.naming          : [NA] failed to request

java.net.ConnectException: Connection refused: connect
	at java.net.DualStackPlainSocketImpl.waitForConnect(Native Method) ~[na:1.8.0_101]
	at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:85) ~[na:1.8.0_101]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_101]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_101]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_101]
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172) ~[na:1.8.0_101]
	at java
————————————————
```

缺少config`下的`server-addr报错

```java
2021-03-02 17:42:30.958 ERROR 36536 --- [ main] c.a.n.client.config.impl.ClientWorker    : [fixed-localhost_8848] [sub-server] get server config exception, dataId=nacos-server, group=DEFAULT_GROUP, tenant=

java.net.ConnectException: no available server
	at com.alibaba.nacos.client.config.http.ServerHttpAgent.httpGet(ServerHttpAgent.java:123) ~[nacos-client-1.2.0.jar:na]
	at com.alibaba.nacos.client.config.http.MetricsHttpAgent.httpGet(MetricsHttpAgent.java:48) ~[nacos-client-1.2.0.jar:na]
	at com.alibaba.nacos.client.config.impl.ClientWorker.getServerConfig(ClientWorker.java:230) ~[nacos-client-1.2.0.jar:na]
	at com.alibaba.nacos.client.config.NacosConfigService.getConfigInner(NacosConfigService.java:143) [nacos-client-1.2.0.jar:na]
	at com.alibaba.nacos.client.config.NacosConfigService.getConfig(NacosConfigService.java:92) [nacos-client-1.2.0.jar:na]
```

在源码中---如果不设置默认是localhost

![image-20210309150859808](https://i.loli.net/2021/03/09/59UvhuBPgL468xW.png)

在那nacos上添加配置信息

![image-20210309144632746](https://i.loli.net/2021/03/09/RE5weXzlBSNO9oP.png)

注意:命名是    `服务名字.properties` 或者 `服务名字.yaml`

创建controller测试类

```java
@Slf4j
@Controller
@RestController
@RefreshScope
public class TestController {
    @Value(value = "${fileServerBaseUrl}")
    private String URL;
    @GetMapping("hello")
    public String test(){
        return this.URL;
    }
}
```

`@RefreshScope`，主要用来让这个类下的配置内容支持动态刷新，也就是当我们的应用启动之后，修改了Nacos中的配置内容之后，这里也会马上生效。

启动成功后访问 

![image-20210309145218863](https://i.loli.net/2021/03/09/FsMXltyfVigGheB.png)

可以看到注册中心上服务已注册

![image-20210309151037513](https://i.loli.net/2021/03/09/6xFL2imA1bohWkf.png)



### 创建client

maven依赖

```
    <dependencies>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.8.1</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.2.2.RELEASE</version>
            <optional>true</optional>
        </dependency>
    </dependencies>
```

创建开放接口

```
@FeignClient(value = "nacos-server")
public interface OpenApi {
   @PostMapping("/getInfo")
   String getOpenMesg(@RequestParam("name") String name);
}
```

### 创建消费者

maven依赖

```
<properties>
        <java.version>1.8</java.version>
        <spring-cloud-alibaba.version>2.2.0.RELEASE</spring-cloud-alibaba.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- nacos配置中心-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            <!-- <version>${spring-cloud-alibaba.version}</version>-->
        </dependency>
        <!-- nacos注册中心-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <!-- <version>${spring-cloud-alibaba.version}</version>-->
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>1.2.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.2.2.RELEASE</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.2.2.RELEASE</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>demo-client</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
                <exclusions>
                    <exclusion>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>nacos-client</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

启动类

```
@EnableFeignClients("com.example.democlient.client")
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }
    @Bean
    @LoadBalanced //开启负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

配置文件

```
spring.application.name=nacos-consumer
spring.cloud.nacos.config.server-addr=master:8848
spring.cloud.nacos.discovery.server-addr=master:8848
```

创建测试类

```
@Slf4j
@RestController
@Controller
public class TestController {

    @Resource
    OpenApi openApi;
    @PostMapping("/test")
    String test(@RequestParam("name")String name){
        log.info("请求参数{}",name);
        return openApi.getOpenMesg(name);
    }

}
```

启动项目    (项目已注册到配置中心)

![image-20210309172824492](https://i.loli.net/2021/03/09/wbPUTgnhXiG4FaC.png)

测试接口   localhost:8623/test

![image-20210309172926293](https://i.loli.net/2021/03/09/s2hb5lIxGmECnoR.png)