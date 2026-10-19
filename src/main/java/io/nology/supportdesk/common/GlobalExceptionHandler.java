package io.nology.supportdesk.common;

import io.nology.supportdesk.common.dtos.ApiErrorResponse;
import io.nology.supportdesk.common.exceptions.EmailAlreadyExistsException;
import io.nology.supportdesk.common.exceptions.NotFoundException;
import io.nology.supportdesk.common.exceptions.UnprocessableContentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnprocessableContentException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessableContent(UnprocessableContentException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
