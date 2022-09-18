package com.admin.tool.common.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Set;

public class RouteDatasource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalContext.get();
    }

    // 등록된 DataSource들의 Key 목록 가져옴
    public Set<Object> getRegistServers() {
        return super.getResolvedDataSources().keySet();
    }
}
