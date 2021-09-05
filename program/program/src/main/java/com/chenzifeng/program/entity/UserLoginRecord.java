package com.chenzifeng.program.entity;

import com.chenzifeng.program.entity.base.IdEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @ProjectName:
 * @ClassName: UserLoginRecord
 * @Author: czf
 * @Description: 用户登录信息表
 * @Date: 2021/9/5 10:54
 * @Version: 1.0
 **/
@Entity
@Data
public class UserLoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20)
    private String username;

    @Column(length = 20)
    private String password;

    @Column
    private Long loginTime;

    @Column
    private Long checkTime;

    @Override
    public String toString() {
        return "UserLoginRecord{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", loginTime=" + loginTime +
                ", checkTime=" + checkTime +
                '}';
    }
}
