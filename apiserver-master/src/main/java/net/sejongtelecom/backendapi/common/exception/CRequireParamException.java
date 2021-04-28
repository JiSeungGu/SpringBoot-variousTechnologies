package net.sejongtelecom.backendapi.common.exception;

public class CRequireParamException extends RuntimeException {

  public CRequireParamException(String msg, Throwable t) {
    super(msg, t);
  }

  public CRequireParamException(String msg) {
    super(msg);
  }

  public CRequireParamException() {
    super();
  }
}
