package com.example.springscaffold2.common.config;

import com.example.springscaffold2.common.model.Props;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
public class DatasourceConfig {
    /**
     * 예시라 중복 코드는 정리 안함
     */

    @Configuration
    @EnableJpaRepositories(basePackages = "com.example.springscaffold2.api.jpas.root", entityManagerFactoryRef = "rootEntityManagerFactory", transactionManagerRef = "multiTxManager")
    public class RootDatasourceConfig {
        @Primary
        @Bean(name = "rootEntityManagerFactory")
        @DependsOn("multiTxManager")
        public LocalContainerEntityManagerFactoryBean xaEntityManagerFactoryRoot() {
            HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
            jpaVendorAdapter.setShowSql(true);
            jpaVendorAdapter.setGenerateDdl(false);
            jpaVendorAdapter.setDatabase(Database.MYSQL);

            Properties properties = new Properties();
            properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
            properties.put("javax.persistence.transactionType", "JTA");

            LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
            entityManager.setDataSource(root());
            entityManager.setJpaVendorAdapter(jpaVendorAdapter);
            entityManager.setPackagesToScan("com.example.springscaffold2.api.jpas.root");
            entityManager.setPersistenceUnitName("root-persistence");
            entityManager.setJpaProperties(properties);

            return entityManager;
        }

        private DataSource root() {
            AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();

            Properties properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "1234");
            properties.setProperty("url", "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=UTC");

            dataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
            dataSource.setXaProperties(properties);
            dataSource.setUniqueResourceName("unique_DB_created_root");
            dataSource.setMinPoolSize(10);
            dataSource.setMaxPoolSize(20);
            dataSource.setBorrowConnectionTimeout(600);
            dataSource.setMaxIdleTime(60);

            return dataSource;
        }
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "com.example.springscaffold2.api.jpas.sub", entityManagerFactoryRef = "subEntityManagerFactory", transactionManagerRef = "multiTxManager")
    public class SubDatasourceConfig {
        @Bean(name = "subEntityManagerFactory")
        @DependsOn("multiTxManager")
        public LocalContainerEntityManagerFactoryBean xaEntityManagerFactorySub() {
            HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
            jpaVendorAdapter.setShowSql(true);
            jpaVendorAdapter.setGenerateDdl(false);
            jpaVendorAdapter.setDatabase(Database.MYSQL);

            Properties properties = new Properties();
            properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
            properties.put("javax.persistence.transactionType", "JTA");

            LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
            entityManager.setDataSource(sub());
            entityManager.setJpaVendorAdapter(jpaVendorAdapter);
            entityManager.setPackagesToScan("com.example.springscaffold2.api.jpas.sub");
            entityManager.setPersistenceUnitName("sub-persistence");
            entityManager.setJpaProperties(properties);

            return entityManager;
        }

        private DataSource sub() {
            AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();

            Properties properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "1234");
            properties.setProperty("url", "jdbc:mysql://localhost:3306/test2?characterEncoding=UTF-8&serverTimezone=UTC");

            dataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
            dataSource.setXaProperties(properties);
            dataSource.setUniqueResourceName("unique_DB_created_sub");
            dataSource.setMinPoolSize(10);
            dataSource.setMaxPoolSize(20);
            dataSource.setBorrowConnectionTimeout(600);
            dataSource.setMaxIdleTime(60);

            return dataSource;
        }
    }


}
