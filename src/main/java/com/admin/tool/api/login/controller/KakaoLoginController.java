package com.admin.tool.api.login.controller;

import com.admin.tool.api.login.model.KakaoAuth;
import com.admin.tool.api.login.model.KakaoDTO;
import com.admin.tool.api.login.service.LoginService;
import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class KakaoLoginController {
    private final Logger logger =  LoggerFactory.getLogger(this.getClass());
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<KakaoAuth>> getKakaoAuth(@RequestBody KakaoDTO dto){
        logger.info("====== Get AccessToken ======");
        ResponseEntity<KakaoAuth> kakaoAuthResponse = loginService.getAuth(dto.getCode());

        if(kakaoAuthResponse.getStatusCode().is4xxClientError()){
            return ResponseEntity.ok(ApiResponse.of(Code.FAIL.code, kakaoAuthResponse.getBody()));
        } else if(kakaoAuthResponse.getStatusCode().is5xxServerError()){
            return ResponseEntity.ok(ApiResponse.of(Code.FAIL.code, kakaoAuthResponse.getBody()));
        }

        // 실제 서비스에서는 아래의 구현 필요

        // 1. token을 이용해 유저 정보를 가져오는 로직....

        // 2. 유저정보를 이용해 로그인 처리 하는 로직... (로그인 or 생성)

        // 3. 유저 기본 정보 및 권한(Spring security authority)을 가지고 JWT 생성하는 로직...

        // 4. JWT 리턴

        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, kakaoAuthResponse.getBody()));
    }
}
