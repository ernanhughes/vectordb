package com.fodala.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServerConfig {

    private static final Logger log = LoggerFactory.getLogger(ServerConfig.class);

    @Bean
    public CommandLineRunner applicationStartup(Environment environment) {
        return args -> {
            var port = environment.getProperty("local.server.port");
            var contextPath = environment.getProperty("server.servlet.context-path", "");
            var applicationName = environment.getProperty("spring.application.name");
            log.info("{} is running at http://localhost:{}{}", applicationName, port, contextPath);
        };
    }

    @Bean
    public ApplicationListener<ServletWebServerInitializedEvent> onServletWebServerInitialized() {
        return event -> {
            var port = event.getWebServer().getPort();
            System.getProperties().put("local.server.port", port);
        };
    }

}

