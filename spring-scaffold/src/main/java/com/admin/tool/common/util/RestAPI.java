package com.admin.tool.common.util;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class RestAPI {
    private final RestTemplate restTemplate;

    public ResponseEntity<Map> getTest(){
        return restTemplate.getForEntity("http://localhost:8080/api/users", Map.class);
    }

}
