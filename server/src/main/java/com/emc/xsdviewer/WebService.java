package com.emc.xsdviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebService {
    protected WebService() {
    }

    public static void main(String[] args) {
        SpringApplication.run(WebService.class, args);
    }
}