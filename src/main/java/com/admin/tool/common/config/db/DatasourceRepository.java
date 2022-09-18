package com.admin.tool.common.config.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "repositories")
public class DatasourceRepository {

    @Getter
    @Setter
    private Props root;

    @Getter
    @Setter
    private List<Props> routes;
}
