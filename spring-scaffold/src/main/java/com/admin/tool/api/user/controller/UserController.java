package com.admin.tool.api.user.controller;

import com.admin.tool.api.user.service.UserService;
import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import com.admin.tool.common.model.CustomUserDetailsDTO;
import com.admin.tool.common.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // H2 와 Mysql 두개 DB 사이에서의 Transaction 예시
    @GetMapping("/xa")
    public ResponseEntity<ApiResponse<String>> xaTransactionTest() {
        userService.xaTransactionTest();
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, "transaction test"));
    }

    // Transaction ReadOnly일때 slave보는지 확인
    @GetMapping("")
    public ResponseEntity<ApiResponse<Integer>> noTransactionTest() {
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, userService.noTransactionTest()));
    }

    @GetMapping("/slave")
    public ResponseEntity<ApiResponse<Integer>> transactionMasterTest() {
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, userService.transactionSlaveTest()));
    }

    @GetMapping("/master")
    public ResponseEntity<ApiResponse<Integer>> transactionSlaveTest() {
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, userService.transactionMasterTest()));
    }

    // 프론트엔드와의 Cookie 공유 테스트 (다른 도메인에도 되도록)
    // 테스트 결과 RestAPI에서 쿠키는 같은 도메인 그룹일때나 써야겠다...
    @GetMapping("/cookieSet")
    public ResponseEntity<ApiResponse<String>> cookieSetTest(@CookieValue(name = "MakeClient", required = false) String c1,
                                                             @CookieValue(name = "MakeServer", required = false) String c2,
                                                             HttpServletResponse response) {
        System.out.println("MakeClient : " + c1);
        System.out.println("MakeServer : " + c2);

        ResponseCookie cookie = ResponseCookie.from("MakeServer", "FromServer")
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .maxAge(60 * 60 * 2)// 2 Hours
                //.domain("예)abcd.com")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, "Hello world"));
    }
}
