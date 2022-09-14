package com.admin.tool.api.user.dao;

import com.admin.tool.common.aop.annotation.LookupKey;
import com.admin.tool.common.aop.annotation.RoutingMapper;

@RoutingMapper
public interface UserMapper {
    public String selectTest(@LookupKey String key);
    public int transactionTest(@LookupKey String key, String value);
}
