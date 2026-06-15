package com.innervix.model3d.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseConnectionLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionLogger.class);

    private final DataSource dataSource;
    private final Environment environment;

    public DatabaseConnectionLogger(DataSource dataSource, Environment environment) {
        this.dataSource = dataSource;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        String profiles = activeProfiles();
        String configuredUrl = Optional.ofNullable(environment.getProperty("spring.datasource.url")).orElse("unknown");

        try (Connection connection = dataSource.getConnection()) {
            var metaData = connection.getMetaData();
            log.info(
                    "DATABASE CONNECTED | profile={} | url={} | user={} | product={} {}",
                    profiles,
                    metaData.getURL(),
                    metaData.getUserName(),
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion()
            );
        } catch (Exception ex) {
            log.error(
                    "DATABASE NOT CONNECTED | profile={} | url={} | reason={}",
                    profiles,
                    configuredUrl,
                    ex.getMessage()
            );
        }
    }

    private String activeProfiles() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }
        return String.join(",", Arrays.asList(profiles));
    }
}
