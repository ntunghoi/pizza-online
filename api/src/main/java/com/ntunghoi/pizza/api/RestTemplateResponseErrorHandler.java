package com.ntunghoi.pizza.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
  private static final Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
    return (httpResponse.getStatusCode().series() == Series.CLIENT_ERROR
        || httpResponse.getStatusCode().series() == Series.SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    logger.error("Error: Status Code {}", httpResponse.getStatusCode());
    if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
      ;
  }
}
