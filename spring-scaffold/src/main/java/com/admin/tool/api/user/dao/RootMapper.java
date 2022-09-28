package com.admin.tool.api.user.dao;

import com.admin.tool.common.aop.annotation.LookupKey;
import com.admin.tool.common.aop.annotation.RoutingMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RootMapper {
    public int selectWorld();
    public int transactionTest(String value);
}
