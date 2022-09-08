package com.admin.tool.api.user.controller;

import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth")
public class TestController {

    // --------- Frontend Server ---------------------

    // 로그인 창 열기
    // /oauth/authserver/authorize?client_id={client-id}&redirect_uri=/oauth/apiserver/redirect&response_type=code





    // --------- Authorization Server ----------------
    // Front -> Authorization Server -> API Server
    @GetMapping("/authserver/authorize")
    public void oauthLoginImAuthorizationServer(HttpServletResponse response) throws IOException, InterruptedException {
        Thread.sleep(300);

        // 프론트 요청으로 로그인 창이 열리고 로그인 하는 곳
        // AuthorizationCode를 생성해서 백엔드로 넘겨준다.

        String redirect_uri = "http://localhost:8080/oauth/apiserver/redirect?authorizationCode=oauthprocesstest";
        response.sendRedirect(redirect_uri);
    }




    // --------- Api Server ----------------
    // Authorization Server -> Front -> API Server
    @GetMapping("/apiserver/redirect")
    public ResponseEntity<ApiResponse<String>> oauthLoginRedirectImAPIServer(@RequestParam("authorizationCode") String authorizationCode) throws IOException, InterruptedException {
        Thread.sleep(100);

        // API 서버 프로세스 (authorizationCode)
        
        // authorizationCode 를 가지고 인증 코드 요청함

        /* ex)
            /oauth/auth?
                client_id={client-id}
                    &client_secret={client-secret}
                    &redirect_uri={redirect-url}
                    &code={authorizationCode}
                    &grant_type=authorization_code
         */

        String accessToken = authorizationCode + "_HELLOACCESSTOKEN";
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, accessToken));
    }
}