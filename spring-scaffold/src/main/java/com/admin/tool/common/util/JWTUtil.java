package com.admin.tool.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.admin.tool.common.model.CustomUserDetails;
import com.admin.tool.common.model.CustomUserDetailsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Primary
@Component("jwtUtil")
public class JWTUtil {
    private final String JWT_SIGN_KEY = "mysignkey";

    public void createAndSetToken(CustomUserDetailsDTO user) {
        Map<String, Object> claims = new HashMap<>();

        List<String> authList = new ArrayList<>();

        user.getAuthorities().forEach(auth -> authList.add(auth.getAuthority()));

        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("authList", authList);

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MINUTE, 3600);
        dt = c.getTime();

        String accessToken = Jwts.builder().setClaims(claims).setExpiration(dt).signWith(SignatureAlgorithm.HS512, JWT_SIGN_KEY).compact();

        user.setToken(accessToken);
    }

    @SuppressWarnings("unchecked")
    public CustomUserDetails checkToken(String token) {
        Jws<Claims> jwt = signAndParseJWT(token);
        if (jwt == null) return null;
        Claims token_body = jwt.getBody();

        List<String> auth_list = (List<String>) token_body.get("authList");
        String id = (String) token_body.get("id");
        String name = (String) token_body.get("name");

        CustomUserDetails user = new CustomUserDetails();
        user.setAuthorities(setAuthorities(auth_list));
        user.setId(id);
        user.setName(name);

        return user;
    }

    public Jws<Claims> signAndParseJWT(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SIGN_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            // ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
            return null;
        }
    }

    public Collection<GrantedAuthority> setAuthorities(List<String> auth_list) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        auth_list.stream().forEach(authority -> {
            authorities.add(new SimpleGrantedAuthority(authority));
        });

        return authorities;
    }
}
