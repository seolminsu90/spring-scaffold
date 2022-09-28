package com.example.springscaffold2.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "route")
public class RouteDatasourceProps {

    @Getter
    @Setter
    private List<Props> datasource;
}
