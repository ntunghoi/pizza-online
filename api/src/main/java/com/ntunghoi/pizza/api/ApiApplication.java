package com.ntunghoi.pizza.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .errorHandler(new RestTemplateResponseErrorHandler())
        .build();
  }
}
