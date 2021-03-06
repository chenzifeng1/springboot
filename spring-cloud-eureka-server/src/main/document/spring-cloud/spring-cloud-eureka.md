# spring-cloud-eureka
## 原理
Eureka分为Server端和Client端
- Register: 想要参与服务注册的发现的实例首先需要向Eureka服务器注册信息，注册是在第一次心跳发生时提交
- Renew: (续租，心跳)Eureka客户端需要每30s来发送一次心跳续租，更新通知Eureka服务器实例仍然是活动的，
如果服务器在90s内没有看到更新，则会将其在注册表中删除
- Fetch Registry: Eureka客户端从服务端获取注册表信息，缓存到本地。之后，客户端使用这些信息来查找其他服务，
通过获取上一个获取周期和当前获取周期之间的增量更新，可以定期（每30s）更新此信息。节点信息在服务器中保存时间更长（大约3分钟），
因此获取节点信息可能会再次返回相同实例，Eureka客户端胡自动处理重复的信息。在获得增量之后，Eureka客户端通过比较服务器返回的实例
基数来与服务器协调信息，如果由于某种原因信息不匹配，则再次获取整个注册表信息。
- Cancel: Eureka客户端在关闭时向Eureka服务器发送取消请求，该操作会从服务器的注册表中删除该实例，从而有效地将实例从通信量中取出
- Time Lag: 同步时间延迟
- Communication mechanism: 通讯机制 Http协议下的Rest请求


    

## 搭建
### 服务端：eureka-server
所需要的maven依赖如下：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
        <version>2.3.4.RELEASE</version>
    </dependency>
</dependencies>
```
- spring-cloud-starter-netflix-eureka-sever，这个是eureka-server所需要的依赖。
- spring-boot-starter-actuator，这个是一个探针组件，可以查看服务节点的环境情况，比如流量，启用线程数据等

之后我们需要在我们项目的启动类上加注解 `@EnableEurekaServer`  
application.yml的配置如下：
```yaml
eureka:
  client:
    # 是否将自己注册到eureka-server中，默认为true，但是这是个server端，就不需要了
    register-with-eureka: false
    # 是否从eureka-server中获取注册信息，由于是单节点，不需要同步其他数据，这里用false
    fetch-registry: false
    serviceUrl:
    #这个地方我得好好研究研究
     defaultZone: http://127.0.0.1:8080/eureka
  server:
    enable-self-preservation: false
spring:
  application:
    name: eureka-server
server:
  port: 8762
```
然后启动之后访问 [http://127.0.0.1:8762](http://127.0.0.1:8762) 即可查看当前服务节点情况
 
### 客户端：eureka-server
所需要的maven依赖如下：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
- spring-cloud-starter-netflix-eureka-client，这个是eureka-client所需要的依赖
- spring-boot-starter-web，不加这个依赖会报错

之后我们需要在客户端的启动类上加注解 `@EnableDiscoveryClient`,当然如果我们的注册中心本身就是Eureka，
那么我们可以使用`@EnableEurekaClient`  
application.yml的配置如下
```yaml
eureka:
  client:
    serviceUrl:
      # 服务server的url 这里的有坑，不要和server端的defaultZone完全相同，这里是server端服务启动的ip:port
      # Eureka所有的操作调用，都是基于Restful协议的
      defaultZone: http://127.0.0.1:8762/eureka
spring:
  application:
    # 服务名称
    name: chenzifeng-spring-cloud-eureka-client-1
# Tomcat的端口    
server:
  port: 8081
```

之后我们可以看一下 [http://127.0.0.1:8762](http://127.0.0.1:8762)
![注册中心](../../img/eureka注册中心.PNG)

当然，其实更建议使用idea自带的创建spring-boot项目的功能来创建项目,可以直接引入依赖，并且自动帮你进行版本之间配置。


## Eureka的一些机制
- AP: 分区容错性 Eureka通过降低数据一致性要求来提高可用性      
当我们启用多个Eureka节点来做注册中心集群时（为了防止单个注册中心挂掉之后，服务之间的调用出现问题），由于服务之间的调用
需要拉取服务列表（其他服务的地址等信息）。这样的话，每个服务需要向集群中的每个Eureka的注册中心上注册服务，服务注册也许并不是同时进行及完成，
这样的话就会造成Eureka服务中心之间的数据不一致。Eureka的注册中心之间也会相互传递消息，但是这种机制也不能满足较高的数据一致性。
    1. Eureka默认服务在线：服务注册上来的时候，Eureka会默认认为服务在线，服务状态会缓存在内存中。
    2. 那么当服务挂掉时，但是Eureka缓存的服务状态为正常，这时候就出现了数据不一致的情况。不过这问题不大，因为当其他服务需要调用这个挂掉的服务时，会用重试机制。
    3. Eureka感知服务状态是通过`心跳`来完成的，默认30s,所以在30s内服务出问题了，Eureka也无法知道。当30s之后没有接收到心跳包，才会任务服务出了问题。
    4. 另外，就是当Eureka注册中心挂掉之后，服务之间也可以通过之前缓存的服务列表来完成与其他服务的调用
   
