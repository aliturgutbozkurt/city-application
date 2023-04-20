package com.kuehnenagel.cities.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

  private HttpStatus status;
  private String message;

  public ApplicationException(String message, HttpStatus status, String message1) {
    super(message);
    this.status = status;
    this.message = message1;
  }
}
