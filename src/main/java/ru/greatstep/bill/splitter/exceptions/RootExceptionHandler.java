package ru.greatstep.bill.splitter.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.greatstep.bill.splitter.models.util.ErrorDto;

@ControllerAdvice
public class RootExceptionHandler {

    private static final String VALIDATION_ERROR_PATTERN =
            "Ошибка в поле %s в значении %s - %s";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String actualValue = String.valueOf(fieldError.getRejectedValue());
                String errorMessage = fieldError.getDefaultMessage();
                message.append(String.format(VALIDATION_ERROR_PATTERN, fieldName, actualValue, errorMessage))
                        .append(". ");
            } else {
                message.append(error.getDefaultMessage()).append(". ");
            }
        });
        return ResponseEntity.badRequest().body(new ErrorDto(message.toString()));
    }
}
