package com.fodala;

import com.fodala.service.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Objects;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final DataLoader dataLoader;

    private final Environment env;

    public Application(@Autowired DataLoader dataLoader, @Autowired Environment env) {
        this.dataLoader = dataLoader;
        this.env = env;
    }

    public static void main(String[] args) {
        logger.info("Starting VectorDB application with args: {}", args);
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        String initDB = env.getProperty("spring.datasource.initialization-mode");
        if (Objects.equals(initDB, "always")) {
            logger.info("Initializing database.");
            try {
                createDatabaseSchema();
                loadData();
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void createDatabase() throws InterruptedException {
        dataLoader.createDatabase();
        Thread.sleep(1000);
    }

    void createDatabaseSchema() throws InterruptedException {
        dataLoader.loadData("data/schema.sql");
        Thread.sleep(1000);
    }

    void loadData() throws InterruptedException {
        dataLoader.loadData("data/data.sql");
        Thread.sleep(1000);
    }
}