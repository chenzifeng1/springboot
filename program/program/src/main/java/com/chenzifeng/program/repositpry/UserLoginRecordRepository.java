package com.chenzifeng.program.repositpry;

import com.chenzifeng.program.entity.UserLoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName:
 * @ClassName: UserLoginRecordRepository
 * @Author: czf
 * @Description:
 * @Date: 2021/9/5 11:00
 * @Version: 1.0
 **/

public interface UserLoginRecordRepository extends JpaRepository<UserLoginRecord, Long>, QuerydslPredicateExecutor<UserLoginRecord> {


    @Query("update UserLoginRecord set loginTime=?1, checkTime=?2 where username=?3")
    @Transactional
    @Modifying
    void updateLoginRecord(long loginTime,long checkTime,String username);
}
