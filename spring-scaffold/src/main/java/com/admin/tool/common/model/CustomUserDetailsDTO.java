package com.admin.tool.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
public class CustomUserDetailsDTO {
    private String id;
    private String name;
    private LocalDateTime regDate;
    private String token;
    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetailsDTO readFrom(CustomUserDetails c) {
        if (c == null) return null;
        return CustomUserDetailsDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .regDate(c.getRegDate())
                .authorities(c.getAuthorities()).build();
    }
}
