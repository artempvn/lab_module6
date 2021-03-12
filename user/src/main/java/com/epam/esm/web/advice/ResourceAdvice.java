package com.epam.esm.web.advice;

import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.exception.TagException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ResourceAdvice {

  public static final String DELIMITER = "; ";
  private final ReloadableResourceBundleMessageSource messageSource;

  public ResourceAdvice(ReloadableResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(ResourceValidationException.class)
  public ResponseEntity<ErrorResponse> handleException(ResourceValidationException e) {
    String textMessage =
        messageSource.getMessage("error.notExist", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getResourceId());
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e) {
    String textMessage =
        messageSource.getMessage("error.notExist", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getResourceId());
    String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TagException.class)
  public ResponseEntity<ErrorResponse> handleException(TagException e) {
    String textMessage =
        messageSource.getMessage("error.noTags", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ErrorResponse> handleException(BindException e) {
    String errorMessage =
        (e.getBindingResult().getFieldErrors())
            .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(DELIMITER));
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getErrorCount());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException e) {
    String errorCode = String.format("%s", HttpStatus.METHOD_NOT_ALLOWED.value());
    String textMessage =
        messageSource.getMessage("error.methodNotSupported", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getMethod());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
    String textMessage =
        messageSource.getMessage(
            "error.methodArgumentTypeMismatch", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getName());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
    String textMessage =
        messageSource.getMessage("error.notReadable", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
