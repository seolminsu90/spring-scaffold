package com.admin.tool.api.login.service;

import com.admin.tool.api.login.model.KakaoAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final RestTemplate restTemplate;

    public ResponseEntity<KakaoAuth> getAuth(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "cd6834db1e342873d1cd1a762091551c");
        params.add("redirect_uri", "http://localhost:8081/oauth/redirect"); // 인가 코드를 받은 리다이렉트 URL 검증 (검증용이고 실제 리다이렉트 되는 것은 아님)
        params.add("client_secret", "{ INSERT_CLIENT_SECRET }");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoAuth> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                KakaoAuth.class
        );

        return response;
    }
}
