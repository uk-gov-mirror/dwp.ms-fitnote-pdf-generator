package uk.gov.dwp.health.fitnote.generator.domain.exception;

public class MissingDataException extends Exception {

  public MissingDataException(String message) {
    super(message);
  }

  public MissingDataException(Throwable cause) {
    super(cause);
  }
}
