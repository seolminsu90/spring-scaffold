package com.admin.tool.common.config.db;

import com.admin.tool.api.user.dao.RootMapper;
import com.admin.tool.common.aop.annotation.RoutingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@DependsOn({"rootDatasourceConfig"})
@MapperScan(value = "com.admin.tool.api", annotationClass = RoutingMapper.class, sqlSessionFactoryRef = "routingSessionFactory")
public class RouteDatasourceConfig {

    private final DatasourceRepository routeDatasourceProps;
    private final RootMapper rootMapper;

    @Bean("routingDataSource")
    public DataSource routingDataSource() {

        /**
         * rootMapper로부터 동적으로 Routes를 조회하면 아래의 옵션은 필요없다.
         * => routeDatasourceProps.getRoutes()
         * => application.yml의 repositories.routes
         */

        Integer world = rootMapper.selectWorld();
        log.info("====================================");
        log.info(" Hello World {}", world);
        log.info(" routeDatasourceProps.getRoutes 를 대체할 수 있다. (db로부터 읽는 동적 처리)");
        log.info("====================================");

        Map<Object, Object> dataSourceMap = new HashMap<>();
        AbstractRoutingDataSource routingDataSource = new RouteDatasource();

        boolean isSetDefault = false;
        for (Props prop : routeDatasourceProps.getRoutes()) {
            log.info("Create Datasoure {}", prop.getLabel());
            DataSource dataSource = XaDatasourceUtil.createDatasource(prop);
            dataSourceMap.put(prop.getLabel(), dataSource);
            if (!isSetDefault) {
                isSetDefault = true;
                routingDataSource.setDefaultTargetDataSource(dataSource);
            }
        }

        routingDataSource.setTargetDataSources(dataSourceMap);
        return routingDataSource;
    }

    @Bean(name = "routingSessionFactory")
    public SqlSessionFactory routingSessionFactory(@Qualifier("routingDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTransactionFactory(new ManagedTransactionFactory());
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));

        return sessionFactory.getObject();
    }

}
