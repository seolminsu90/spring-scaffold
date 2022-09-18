package com.admin.tool.common.config.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Props {
    private String label;
    private String driverClassName;
    private String datasourceClassName;
    private String jdbcUrl;
    private String username;
    private String password;
}