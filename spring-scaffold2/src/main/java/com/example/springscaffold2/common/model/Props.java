package com.example.springscaffold2.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Props {
    private String label;
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
}