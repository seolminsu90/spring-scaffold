package com.admin.tool.api.user.controller;

import com.admin.tool.api.user.service.UserService;
import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import com.admin.tool.common.model.CustomUserDetailsDTO;
import com.admin.tool.common.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<String>> getResponse() {
        String greetingApi = userService.greetingApi();
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, greetingApi));
    }

    @GetMapping("/rest")
    public ResponseEntity<Map> getResponseTest() {
        return userService.greetingFromOtherApi();
    }
}
