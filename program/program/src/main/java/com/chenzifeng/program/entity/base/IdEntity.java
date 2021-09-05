package com.chenzifeng.program.entity.base;

import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @ProjectName:
 * @ClassName: IdEntity
 * @Author: czf
 * @Description: id
 * @Date: 2021/9/5 10:55
 * @Version: 1.0
 **/

public abstract class IdEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
