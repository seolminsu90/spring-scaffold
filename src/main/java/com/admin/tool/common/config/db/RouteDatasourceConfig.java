package com.admin.tool.common.config.db;

import com.admin.tool.common.aop.annotation.RoutingMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@AllArgsConstructor
@MapperScan(value = "com.admin.tool.api", annotationClass = RoutingMapper.class, sqlSessionFactoryRef = "routingSessionFactory")
public class RouteDatasourceConfig {

    private final RouteDatasourceProps routeDatasourceProps;

    /**
     * lobby -< games와 같은 구조일 때
     * lobby의 routeDatasource, games의 routeDatasource 2개로 구현해서
     * @DependsOn(lobby) 일 때 lobby sessionfactory로 games의 datasource 정보 불러와서 설정하는 식으로 구현
     * ex) lobby key: resion, value: lobbydatasource
     * ex) game  key: server, value: gamedatasource
     *
     * RoutingMapper도 Lobby, Game 분리하면 됨
     * Master/Slave의 경우
     * TransactionSynchronizationManager.isCurrentTransactionReadOnly() 를 key lookup에 활용필요
     */

    @Bean("routingDataSource")
    public DataSource routingDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();

        AbstractRoutingDataSource routingDataSource = new RouteDataSource();

        boolean isSetDefault = false;
        for (Props prop : routeDatasourceProps.getDatasource()) {
            log.info("Create Datasoure {}", prop.getLabel());
            DataSource dataSource = datasource(prop);
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
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));

        return sessionFactory.getObject();
    }


    @Bean
    public DataSource lazyRoutingDataSource(
            @Qualifier(value = "routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier(value = "lazyRoutingDataSource") DataSource lazyRoutingDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(lazyRoutingDataSource);
        return transactionManager;
    }

    private DataSource datasource(Props props) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(props.getJdbcUrl())
                .driverClassName(props.getDriverClassName())
                .username(props.getUsername())
                .password(props.getPassword())
                .build();
    }
}
