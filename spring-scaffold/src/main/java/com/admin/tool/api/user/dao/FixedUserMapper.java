package com.admin.tool.api.user.dao;

import com.admin.tool.common.aop.annotation.LookupKey;
import com.admin.tool.common.aop.annotation.RoutingMapper;
import org.apache.ibatis.annotations.Mapper;

@RoutingMapper(fixedLookupKey = "mysql")
public interface FixedUserMapper {
    public int transactionTest(String value);
}
