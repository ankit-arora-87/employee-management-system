package com.ems.userservice.exceptions;

import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.*;

@ControllerAdvice
@RestController
public class AppResponseExceptionEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                "Something went wrong, please try again later. You can ask admin to check the application logs."
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserNotFoundException.class, NoSuchElementException.class})
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public final ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UniqueLoginException.class})
    public final ResponseEntity<Object> handleUniqueLoginException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidNumberFormatException.class})
    public final ResponseEntity<Object> handleInvalidNumberFormatException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({StorageException.class})
    public final ResponseEntity<Object> handleStorageException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({DataIntegrityViolationException.class, NonUniqueResultException.class})
    public final ResponseEntity<Object> handleDatabaseException(Exception ex, WebRequest request) {

        request.getParameterMap().entrySet().forEach(System.out::println);
        String message = ex.getMessage();
        if(ex.getCause() instanceof ConstraintViolationException){
             message = "Employee login not unique";
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                message
        );
            return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().stream().forEach((error) -> {
            HashMap<String, String> errorData = new HashMap<>();
            errorData.put(error.getField(), error.getDefaultMessage());
            errors.add(errorData);
        });
        ExceptionResponse exceptionResponse = new ExceptionResponse(

                "There are some validation constraints are failing in your request. Please fix listed validation errors!",
                errors
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransactionSystemException.class, ConstraintViolationException.class})
    public final ResponseEntity<Object> handleConstraintException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidDateParseException.class, DateTimeParseException.class})
    public final ResponseEntity<Object> handleInvalidDateParseException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
