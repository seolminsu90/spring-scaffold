package com.admin.tool.common.config.db;

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