package com.admin.tool.api.user.service;

import com.admin.tool.api.user.dao.UserMapper;
import com.admin.tool.common.util.RestAPI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final RestAPI api;
    @Transactional("multiTxManager")
    public void xaTransactionTest(Map<String, Object> params) {
        userMapper.transactionTest("root", "1");
        userMapper.transactionTest("root2", "2");
        if("1".equals(String.valueOf(params.get("Error")))) throw new RuntimeException("1,2 들어가다 에러나는 테스트");
        userMapper.transactionTest("root3", "3");
        userMapper.transactionTest("root", "4");
        userMapper.transactionTest("root2", "5");
        userMapper.transactionTest("root3", "6");
        if("2".equals(String.valueOf(params.get("Error")))) throw new RuntimeException("모두 안들어가는 테스트");
    }
    @Transactional
    public String greetingApi() {
        return userMapper.selectTest("root");
    }

    public ResponseEntity<Map> greetingFromOtherApi() {
        return api.getTest();
    }
}
