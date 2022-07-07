package uk.gov.dwp.health.fitnote.generator.domain.exception;

public class InvalidJsonException extends Exception {

  public InvalidJsonException(String message) {
    super(message);
  }

  public InvalidJsonException(Throwable cause) {
    super(cause);
  }
}
