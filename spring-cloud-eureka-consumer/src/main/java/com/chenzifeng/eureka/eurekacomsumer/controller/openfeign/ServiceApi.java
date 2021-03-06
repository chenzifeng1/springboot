package com.chenzifeng.eureka.eurekacomsumer.controller.openfeign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ProjectName: eureka-comsumer
 * @Package: com.chenzifeng.eureka.eurekacomsumer.controller.openfeign
 * @ClassName: ServiceApi
 * @Author: czf
 * @Description: name是服务名，另外就是openFeign里面内置了ribbon实现，同时引入ribbon会报错
 * @Date: 2020/12/21 10:20
 * @Version: 1.0
 */
//@FeignClient(contextId = "serviceApi",name = "provider",fallback = HystrixDowngradeService.class)
@FeignClient(contextId = "serviceApi", name = "provider", fallbackFactory = HystrixDowngradeService.class)
public interface ServiceApi {
    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/userAccount/getUserById")
    UserAccount getUserInfo(@RequestParam("userId") Integer userId);

    /**
     * 获取当前用户列表
     *
     * @return
     */
    @PostMapping("/userAccount/getUserList")
    List<UserAccount> getUserIdList();

    /**
     * 获取第一个用户信息
     *
     * @return
     */
    @GetMapping("/userAccount/getFirstUser")
    UserAccount getFirstUser();

    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/userAccount/add")
    String addUser(@RequestBody UserAccount user);

    /**
     * 服务异常测试方法
     *
     * @return
     */
    @GetMapping("/userAccount/exception")
    String exceptionRequestTest();

}
