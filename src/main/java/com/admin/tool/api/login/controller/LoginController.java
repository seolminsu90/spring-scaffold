package com.admin.tool.api.login.controller;

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
public class LoginController {

    private final JWTUtil jwtUtil;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomUserDetailsDTO>> login() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        CustomUserDetailsDTO user = CustomUserDetailsDTO.builder()
                .id("id")
                .name("name")
                .authorities(authorities)
                .build();

        jwtUtil.createAndSetToken(user);
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, user));
    }

    @PostMapping("/auth/user")
    @Secured("ROLE_USER")
    public ResponseEntity<ApiResponse<UserDetails>> authTest(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.of(Code.SUCCESS.code, userDetails));
    }
}
