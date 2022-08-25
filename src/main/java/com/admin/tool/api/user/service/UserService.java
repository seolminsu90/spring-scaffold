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
    @Transactional
    public String greetingApi() {
        return userMapper.selectTest("root");
    }

    public ResponseEntity<Map> greetingFromOtherApi() {
        return api.getTest();
    }
}
