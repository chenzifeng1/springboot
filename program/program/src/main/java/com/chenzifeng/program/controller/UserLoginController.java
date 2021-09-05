package com.chenzifeng.program.controller;

import com.chenzifeng.program.service.UserLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName:
 * @ClassName: UserLoginController
 * @Author: czf
 * @Description:
 * @Date: 2021/9/5 11:19
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @PostMapping("/login")
    public String login(String username,String password){
        userLoginRecordService.login(username, password);

        return "success";
    }
}
