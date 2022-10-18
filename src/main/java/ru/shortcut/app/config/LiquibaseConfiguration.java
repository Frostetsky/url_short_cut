package ru.shortcut.app.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

    @Bean
    @ConditionalOnBean(DataSource.class)
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/changelog-master.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
