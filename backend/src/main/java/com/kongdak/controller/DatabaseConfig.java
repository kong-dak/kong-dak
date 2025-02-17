package com.kongdak.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    private final EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    public DatabaseConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void checkEnvironment() {
        log.info("Hibernate Version: {}", org.hibernate.Version.getVersionString());
        log.info("Database: {}", entityManager.getEntityManagerFactory().getProperties().get("hibernate.dialect"));
    }
}