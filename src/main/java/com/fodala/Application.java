package com.fodala;

import com.fodala.service.AntRunner;
import com.fodala.service.DataLoader;
import com.fodala.service.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final DataLoader dataLoader;

    public static List<String> messages = new ArrayList<>();
    private final Environment env;

    @Autowired
    private PDFService pdfService;

    @Autowired
    AntRunner antRunner;

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
        String classPath = "A:\\projects\\vectordb\\target\\vectordb-1.0-SNAPSHOT.jar";
        String filePath = "E:\\Users\\ernan\\Videos\\Working with the Java Module System (Certification 1Z0-819)\\Exercise Files\\04\\understanding-the-modular-jdk-slides.pdf";
        String buildFilePath = "A:\\projects\\vectordb\\src\\main\\resources\\anttasks\\convert_pdf_to_text.xml";
        Map<String, String> properties = Map.of("classPath", classPath,
                "filePath", filePath,
                "opPath", "A:\\text.txt");
        antRunner.run(buildFilePath, properties);
        if (Objects.equals(initDB, "always")) {
            logger.info("Initializing database.");
            try {
                createDatabaseSchema();
                loadData();
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        logger.info("Messages: {}", messages);
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
