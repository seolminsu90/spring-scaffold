package com.admin.tool.common.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Set;

public class RouteDataSource extends AbstractRoutingDataSource {
    // Master/Slave의 경우  determineCurrentLookupKey override에 ThreadLocal이 아니라
    // TransactionSynchronizationManager.isCurrentTransactionReadOnly() 를 key lookup에 활용필요
    // @Transactional(readOnly = true) 시 해당 값 전달받게 됨.
    // ex) LookupKey => ThreadLocalContext.get() + { isMaster True/False ...}
    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalContext.get();
    }


    // 등록된 DataSource들의 Key 목록 가져옴
    public Set<Object> getRegistServers() {
        return super.getResolvedDataSources().keySet();
    }
}
