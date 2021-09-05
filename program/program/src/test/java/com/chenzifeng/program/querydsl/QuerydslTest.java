package com.chenzifeng.program.querydsl;

import com.chenzifeng.program.entity.UserLoginRecord;
import com.chenzifeng.program.service.UserLoginRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @ProjectName:
 * @ClassName: QuerydslTest
 * @Author: czf
 * @Description: querydsl测试类
 * @Date: 2021/9/5 11:19
 * @Version: 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuerydslTest {

    @Autowired
    private UserLoginRecordService loginService;

    @Test
    public void saveTest() {
        loginService.login("test1-0","test");
        loginService.login("test1-1","test");
        loginService.login("test1-2","test");
        loginService.login("test1-3","test");
    }

    @Test
    public void selectList(){
        List<UserLoginRecord> recordByCheckTime = loginService.findRecordByCheckTime(1630828356458L);
        recordByCheckTime.forEach(System.out::println);
    }
}
