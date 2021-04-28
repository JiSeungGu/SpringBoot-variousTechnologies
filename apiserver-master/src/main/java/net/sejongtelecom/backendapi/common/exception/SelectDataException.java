package net.sejongtelecom.backendapi.common.exception;

public class SelectDataException extends RuntimeException {
  public SelectDataException(String msg, Throwable t) {
    super(msg, t);
  }

  public SelectDataException(String msg) {
    super(msg);
  }

  public SelectDataException() {
    super();
  }
}

