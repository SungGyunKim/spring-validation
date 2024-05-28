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
   * <h1>{@code MethodArgumentNotValidException}</h1>
   * <ul>
   *   <li>
   *     Controller의 메서드에 @Valid를 달면 ArgumentResolver에서
   *     유효성 검사를 실시하며 실패시 발생하는 Exception이다.<br>
   *     Exception이 발생하면 DefaultHandlerExceptionResolver에 의해 400 Bad Request로 응답한다.
   *     {@code @RequestBody}, {@code @RequestParam}, {@code @PathVariable}, {@code @ModelAttribute}
   *   </li>
   * </ul>
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
   * <h1>{@code ConstraintViolationException}</h1>
   * <ul>
   *   <li>
   *     Service의 메서드에 @Valid를 달고 클래스에 @Validated를 달면 AOP 기반으로 MethodValidationInterceptor에서
   *     유효성 검사를 실시하며 실패시 발생하는 Exception이다.
   *   </li>
   * </ul>
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
