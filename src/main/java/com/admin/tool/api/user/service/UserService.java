package com.admin.tool.api.user.service;

import com.admin.tool.api.user.dao.FixedUserMapper;
import com.admin.tool.api.user.dao.UserMapper;
import com.admin.tool.common.util.RestAPI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final FixedUserMapper fixedUserMapper;
    @Transactional("globalTxManager")
    public void xaTransactionTest() {
        fixedUserMapper.transactionTest("fix1");
        userMapper.transactionTest("h2", "1");
        userMapper.transactionTest("mysql", "2");
        fixedUserMapper.transactionTest("fix2");

        Random r = new Random();
        if(r.nextBoolean()) throw new RuntimeException("모두 안들어가는 테스트");
    }
}
