package com.ntunghoi.pizza.engine.exceptions;

public class InvalidRequestException extends Exception {
  public InvalidRequestException(String reason) {
    super(reason);
  }

  public InvalidRequestException(String reason, Throwable throwable) {
    super(reason, throwable);
  }
}
