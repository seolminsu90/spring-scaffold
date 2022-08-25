package com.admin.tool.common.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalContext.get();
    }

    public Set<Integer> getServers() {
        Map<Object, DataSource> resolvedDataSources = super.getResolvedDataSources();
        return resolvedDataSources.keySet()
                .stream()
                .map(key -> Integer.valueOf(key.toString()))
                .collect(Collectors.toSet());
    }
}
