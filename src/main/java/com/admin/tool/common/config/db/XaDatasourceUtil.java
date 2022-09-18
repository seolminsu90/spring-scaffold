package com.admin.tool.common.config.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
public class XaDatasourceUtil {

    public static DataSource createDatasource(Props props) {
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
