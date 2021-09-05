package com.chenzifeng.program.service.impl;

import com.chenzifeng.program.entity.QUserLoginRecord;
import com.chenzifeng.program.entity.UserLoginRecord;
import com.chenzifeng.program.repositpry.UserLoginRecordRepository;
import com.chenzifeng.program.service.UserLoginRecordService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @ProjectName:
 * @ClassName: UserLoginRecordServiceImpl
 * @Author: czf
 * @Description:
 * @Date: 2021/9/5 11:17
 * @Version: 1.0
 **/
@Service
public class UserLoginRecordServiceImpl implements UserLoginRecordService {

    @Autowired
    private UserLoginRecordRepository loginRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Override
    public UserLoginRecord login(String username, String password) {
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUsername(username);
        userLoginRecord.setPassword(password);
        long currentTimeMillis = System.currentTimeMillis();
        userLoginRecord.setLoginTime(currentTimeMillis);
        userLoginRecord.setCheckTime(currentTimeMillis);
        if (isUserExists(username)) {
            updateRecord(userLoginRecord);
            return userLoginRecord;
        } else {
            return loginRepository.save(userLoginRecord);
        }

    }

    @Override
    public List<UserLoginRecord> findRecordByCheckTime(Long checkTime) {
        QUserLoginRecord userLoginRecord = QUserLoginRecord.userLoginRecord;
        BooleanExpression booleanExpression = userLoginRecord.checkTime.ne(checkTime);
        return (List<UserLoginRecord>) loginRepository.findAll(booleanExpression);
    }

    @Override
    public UserLoginRecord update(UserLoginRecord userLoginRecord) {
        QUserLoginRecord qUserLoginRecord = QUserLoginRecord.userLoginRecord;

        long execute = jpaQueryFactory.update(qUserLoginRecord)
                .set(qUserLoginRecord.checkTime, System.currentTimeMillis())
                .where(qUserLoginRecord.username.eq(userLoginRecord.getUsername()))
                .execute();
        return execute == 0 ? null : userLoginRecord;
    }

    @Override
    public void updateRecord(UserLoginRecord userLoginRecord) {
        loginRepository.updateLoginRecord(userLoginRecord.getLoginTime(),userLoginRecord.getCheckTime(),userLoginRecord.getUsername());
    }


    /**
     * 判断用户是否存在
     *
     * @param username
     * @return
     */
    private boolean isUserExists(String username) {
        QUserLoginRecord userLoginRecord = QUserLoginRecord.userLoginRecord;
        BooleanExpression expression = userLoginRecord.username.eq(username);
        return loginRepository.exists(expression);
    }


}
