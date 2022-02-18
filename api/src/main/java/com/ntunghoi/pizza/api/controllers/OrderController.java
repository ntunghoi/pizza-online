package com.ntunghoi.pizza.api.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import com.ntunghoi.pizza.api.models.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  @Value("${pizza.engine.port}")
  private int enginePort;

  @Value("${pizza.engine.baseUrl}")
  private String engineBaseUrl;

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping
  private ResponseEntity<Order[]> getAllOrders(@RequestHeader("Authorization") String authHeader) {
    logger.info("Engine Base URL: {}", engineBaseUrl);
    logger.info("Authorization: {}", authHeader);

    RequestEntity<Void> request = RequestEntity.get(URI.create(String.format("%s/orders", engineBaseUrl)))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", authHeader)
        .build();

    ResponseEntity<Order[]> responseEntity = restTemplate.exchange(request, Order[].class);
    logger.info("Calling: status code: {}; response: {}", responseEntity.getStatusCode(), responseEntity.getBody());

    return ResponseEntity.ok(responseEntity.getBody());
  }

  @PostMapping
  private ResponseEntity<Order> placeOrder(@RequestHeader("Authorization") String authHeader,
      @RequestBody String requestBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Authorization", authHeader);

    logger.info("Request: {}", decode(requestBody));
    HttpEntity<String> httpEnttiy = new HttpEntity<String>(decode(requestBody), headers);

    try {
      ResponseEntity<Order> responseEntity = restTemplate.postForEntity(String.format("%s/orders", engineBaseUrl),
          httpEnttiy,
          Order.class);
      logger.info("Calling: status code: {}; response: {}", responseEntity.getStatusCode(), responseEntity.getBody());
      if (responseEntity.getStatusCodeValue() != 200) {
        return responseEntity;
      }

      return ResponseEntity.ok(responseEntity.getBody());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  private String decode(String raw) {
    try {
      return URLDecoder.decode(raw, "UTF-8");
    } catch (UnsupportedEncodingException uee) {
      logger.error("Unsupported encoing", uee);
    }

    return "";
  }
}