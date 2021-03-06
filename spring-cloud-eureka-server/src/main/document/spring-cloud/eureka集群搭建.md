# 高可用的Eureka服务注册中心搭建
Eureka作为服务注册中心，如果只有一个注册中心的节点，那么当这个节点挂掉之后，如果由服务地址发生变化了
那么会导致其他服务获取不到相应的服务地址。因此我们可以配置多个Eureka的注册中心来作为副本，当一个Eureka注册中心
挂掉之后，其他的副本还可以继续提供服务。

## 配置
首先我们可以通过不同配置文件来配置不同的服务注册中心，比如我们可以创建两个配置文件：
`application-erk1.yml`,`application-erk2.yml`。配置信息如下  
- erk1:
    ```yaml
    eureka:
      client:
        service-url:
          defaultZone: http://erk2.com:7002/eruaka
      instance:
        hostname: erk1.com
    server:
      port: 7001
    ```

- erk2:
    ```yaml
    eureka:
      client:
        service-url:
          defaultZone: http://erk1.com:7001/eruaka
      instance:
        hostname: erk1.com
    server:
      port: 7002
    ```

这里有个属性：`eureka.instance.hostname` 是标记服务节点的主机名的，用来查询主机ip。
我们在`application.yml`的配置如下:
```yaml
    spring:
      profiles:
        active: erk1
      application:
        name: EurekaServer
```
 通过激活不同的配置文件，来启用多个不同实例。`application.name`是标志服务名称的，这里我们统一处理，表示多个服务注册中心的节点是
 同一个服务的副本。
 
 ## Eureka服务注册方式
 在学习过程中遇到了一个问题，我配置了`actuator`，但是在eureka-server的监控页面访问，却无法访问到服务节点的actuator监控信息。
 我的服务节点配置了`eureka.instance.hostname=provider`,然后访问的时候eureka就会根据配置对 `provider`这个主机名进行访问，但是由于我没有做主机名和ip地址的映射。
 调用服务时无法根据主机名找到对应的ip进行服务调用，因此无法查看 `provider:port/actuator/info`的信息，去掉`hostname`这个配置之后可以正常访问。  
  eureka默认的服务注册方式是 ip+port+服务名。我们可以通过指定实例的hostname来通过主机名+端口+服务名访问。
 ## Ribbon/LoadBalancer做软负载均衡
 ## RestTemplate/Openfeign实现服务调用
 ## Actuator实现服务监控
 ## Hystrix实现熔断，降级，隔离
 谈熔断之前，先介绍一下什么是`雪崩效应`。在微服务架构体系中，存在各个服务之间的相互调用关系，若其中一个服务提供者A无法提供正常服务，会导致其他调用A的服务也
 出现问题，这种错误的响应扩散会想雪崩一样越滚越大，最后导致整个系统都不可用。  
 ![雪崩效应](..\img\雪崩效应.PNG)
 如何阻止这种因为一个服务节点的问题而引起雪崩效应，对应的手段就是对这个服务节点进行`熔断`。
 - 熔断:在多次调用某一服务出问题时，就禁止一切请求该服务节点的行为。熔断的目的是实现`快速失败`，避免大量无效请求影响系统吞吐量。我们的请求需求会被`降级`处理，当然熔断并未永远禁止向问题服务节点进行请求。
 请求的状态可以分为三类：开、闭、半开（可以参考电路的开闭）
   1. 闭：请求可以正确被服务处理，允许请求打向服务节点。
   2. 开：请求无法被正确处理，甚至会使服务节点崩溃。不允许任何请求打向服务节点
   3. 半开：无法预知请求是否会被正确处理，只能进行尝试，如果成功则状态转为闭，如果不成功则转为开
 - 降级:在服务无法正确响应请求时，我们又不能完全不响应用户，只能退而求其次给用户一个可以接受的友好的响应。可以使用的降级策略：
    1. 静态资源响应：返回给用户一个预设的静态响应，比如说错误页面或者其他
    2. 旧缓存响应：如果无法获取到最新的数据，那么就暂时使用之前的缓存来进行响应
    3. 请求写进MQ，延迟执行
 - 资源隔离:什么是资源隔离呢，当consumer发送Http请求调用服务时，每一个http请求我们需要一个线程去发起，那么我们需要对调用的服务http请求的数量进行限制，不可能无限制的任由服务开启线程发送http请求。
 Hystrix使用线程池来维持对每个服务请求的最大请求数量，我们可以针对不同服务来设置每个服务节点的可以接受的最大请求阈值，线程池可以隔离运行环境，这样即使调用服务的代码存在问题或是线程池资源耗尽也不会影响其他的服务调用，但是会带来额外的维护线程池的开销。
 如果是对性能有严格要求而且确信自己调用服务的客户端代码不会出问题的话, 
 可以使用Hystrix的信号模式(Semaphores)来隔离资源  
 ![资源隔离](..\img\资源隔离.PNG)
 
 ### 线程隔离与信号量
 hystrix使用线程隔离或者是信号量机制来实现对服务请求限制。
 
