//package com.africa.ussd.config;
//
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.configuration.FluentConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FlywayConfig {
//    @Value("${spring.datasource.url}")
//    private String dataSourceUrl;
//
//    @Value("${spring.datasource.username}")
//    private String dataSourceUsername;
//
//    @Value("${spring.datasource.password}")
//    private String dataSourcePassword;
//
//    @Bean
//    public Flyway flyway() {
//        FluentConfiguration configuration = Flyway.configure()
//                .dataSource(dataSourceUrl, dataSourceUsername, dataSourcePassword)
//                .locations("filesystem:C:/Users/HP/Desktop/WB Project/ussd/ussd/src/main/resources/db/migration");
//
//        return new Flyway(configuration);
//    }
//}
