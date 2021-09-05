package com.chenzifeng.program.service;

import com.chenzifeng.program.entity.UserLoginRecord;

import java.util.List;

/**
 * @ProjectName:
 * @InterfaceName: UserLoginRecordService
 * @Author: czf
 * @Description: 用户登录查询接口
 * @Date: 2021/9/5 11:17
 * @Version: 1.0
 **/
public interface UserLoginRecordService {
    /**
     * 登录接口
     * @param username
     * @param password
     */
    UserLoginRecord login(String username, String password);

    /**
     * 查询登录用户的检查时间
     * @param checkTime
     * @return
     */
    List<UserLoginRecord> findRecordByCheckTime(Long checkTime);

    /**
     * 更新
     * @param userLoginRecord
     * @return
     */
    UserLoginRecord update(UserLoginRecord userLoginRecord);

    /**
     * 更新
     * @param userLoginRecord
     */
    void updateRecord(UserLoginRecord userLoginRecord);

}
