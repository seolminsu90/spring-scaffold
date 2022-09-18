package com.admin.tool.common.config.db;

import com.admin.tool.common.aop.annotation.RoutingMapper;
import com.atomikos.icatch.jta.TransactionManagerImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            DataSource dataSource = xaDatasource(prop);
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

    private DataSource xaDatasource(Props props) {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();

        log.info("===XA DataSource ===");
        log.info(props.getLabel());

        Properties properties = new Properties();
        properties.setProperty("user", props.getUsername());
        properties.setProperty("password", props.getPassword());
        properties.setProperty("url", props.getJdbcUrl());

        dataSource.setXaDataSourceClassName(props.getDatasourceClassName());    // 각 DB가 지원하는 XA Datasource로 설정해야함.
        dataSource.setXaProperties(properties);

        dataSource.setUniqueResourceName("unique_" + props.getLabel());   // 관리 유니크 명명
        dataSource.setBorrowConnectionTimeout(600);                             // 커넥션 풀 대기 타임아웃 시간
        dataSource.setMaxIdleTime(60);                                          // Idle 상태인 커넥션 풀 자동 반환 시간
        dataSource.setMinPoolSize(10);                                           // 커넥션 풀 min/max 개수
        dataSource.setMaxPoolSize(20);                                          // 커넥션 풀 min/max 개수

        // 기타 추가 옵션은 com.atomikos.jdbc.AtomikosDataSourceBean::doInit() 확인

        return dataSource;
    }

}
