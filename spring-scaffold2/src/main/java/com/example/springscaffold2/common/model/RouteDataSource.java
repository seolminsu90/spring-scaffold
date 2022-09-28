package com.example.springscaffold2.common.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class RouteDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("Resolved DBs {}", super.getResolvedDataSources());
        log.info("ThreadLocalContext.get() {}", ThreadLocalContext.get());
        return ThreadLocalContext.get();
    }
}

