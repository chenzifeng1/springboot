package com.chenzifeng.program.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @ProjectName:
 * @ClassName: QuerydslConfig
 * @Author: czf
 * @Description: querydsl的配置类
 * @Date: 2021/9/5 11:05
 * @Version: 1.0
 **/
@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private final EntityManager entityManager;

    public QuerydslConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    public JPAQueryFactory queryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
