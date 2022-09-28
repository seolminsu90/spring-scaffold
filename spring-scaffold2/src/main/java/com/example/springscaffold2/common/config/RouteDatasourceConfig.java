package com.example.springscaffold2.common.config;

import com.example.springscaffold2.common.model.Props;
import com.example.springscaffold2.common.model.RouteDataSource;
import com.example.springscaffold2.common.model.RouteDatasourceProps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@AllArgsConstructor
@MapperScan(value = "com.example.springscaffold2.api", annotationClass = Mapper.class, sqlSessionFactoryRef = "routeSessionFactory") // for Mybatis
public class RouteDatasourceConfig {
    private final RouteDatasourceProps routeDatasourceProps;

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

    // for Mybatis
    @Bean(name = "routeSessionFactory")
    public SqlSessionFactory xaSessionFactory(@Qualifier("routingDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
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

        dataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        dataSource.setXaProperties(properties);
        dataSource.setUniqueResourceName("unique_DB_" + props.getLabel());
        dataSource.setMinPoolSize(10);
        dataSource.setMaxPoolSize(20);
        dataSource.setBorrowConnectionTimeout(600);
        dataSource.setMaxIdleTime(60);

        return dataSource;
    }

}
