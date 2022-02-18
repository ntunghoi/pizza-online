package com.ntunghoi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class EngineTestConfiguration {
  @Bean
  public ModelMapper modelMapper() {
    System.out.println("Test configuration");
    return new ModelMapper();
  }
}
