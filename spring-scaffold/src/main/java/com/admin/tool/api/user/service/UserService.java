package com.admin.tool.api.user.service;

import com.admin.tool.api.user.dao.FixedUserMapper;
import com.admin.tool.api.user.dao.RootMapper;
import com.admin.tool.api.user.dao.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final FixedUserMapper fixedUserMapper;

    private final RootMapper rootMapper;
    @Transactional("globalTxManager")
    public void xaTransactionTest() {
        //userMapper.transactionTest("h2", "1");
        userMapper.transactionTest("mysql", "2");
        userMapper.transactionTest("mysql2", "3");
        rootMapper.transactionTest("i'm'.. not route database !!");
        fixedUserMapper.transactionTest("fix...");

        Random r = new Random();
        if(r.nextBoolean()) throw new RuntimeException("모두 안들어가는 테스트");
    }
    public int noTransactionTest(){
        Integer result = userMapper.readOnlyTest("mysql");
        log.info("get : {}", result);
        return result;
    }

    @Transactional(value = "globalTxManager", readOnly = true)
    public int transactionSlaveTest(){
        Integer result = userMapper.readOnlyTest("mysql");
        log.info("get : {}", result);
        return result;
    }

    @Transactional(value = "globalTxManager")
    public int transactionMasterTest(){
        Integer result = userMapper.readOnlyTest("mysql");
        log.info("get : {}", result);
        return result;
    }
}
