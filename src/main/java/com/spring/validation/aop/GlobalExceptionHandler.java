package com.spring.validation.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  /**
   * {@code MethodArgumentNotValidException} : {@code @RequestBody}에 매핑되는 DTO 클래스를 검증할 때 던져지는 예외
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("")
        ));
    return ResponseEntity.badRequest().body(errors);
  }

  /**
   * {@code ConstraintViolationException} : {@code @PathVariable}에 매핑되는 경로 파라미터 또는
   *                                        {@code @RequestParam}에 매핑되는 쿼리 파라미터를 검증할 때 던져지는 예외
   */
  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException e) {
    Map<String, String> errors = e.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                .reduce((first, second) -> second)
                .get().toString(),
            ConstraintViolation::getMessage
        ));
    return ResponseEntity.badRequest().body(errors);
  }
}
