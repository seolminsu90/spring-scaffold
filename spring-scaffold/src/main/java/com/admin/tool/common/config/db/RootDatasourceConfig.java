package com.admin.tool.common.config.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@MapperScan(value = "com.admin.tool.api", annotationClass = Mapper.class, sqlSessionFactoryRef = "rootSessionFactory")
public class RootDatasourceConfig {

    private final DatasourceRepository routeDatasourceProps;

    @Bean(name = "rootDataSource")
    public DataSource rootDataSource() {
        return XaDatasourceUtil.createDatasource(routeDatasourceProps.getRoot());
    }

    @Bean(name = "rootSessionFactory")
    public SqlSessionFactory rootSessionFactory(@Qualifier("rootDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/root/*.xml"));

        return sessionFactory.getObject();
    }

    @Bean(name = "rootSessionTemplate")
    public SqlSessionTemplate rootSqlSessionTemplate(@Qualifier("rootSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
