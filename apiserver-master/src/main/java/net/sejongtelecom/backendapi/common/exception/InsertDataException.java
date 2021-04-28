package net.sejongtelecom.backendapi.common.exception;


public class InsertDataException extends RuntimeException {
  public InsertDataException(String msg, Throwable t) {
    super(msg, t);
  }

  public InsertDataException(String msg) {
    super(msg);
  }

  public InsertDataException() {
    super();
  }
}
