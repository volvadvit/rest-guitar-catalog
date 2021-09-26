package com.zuzex.vvolkov.components;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.constants.Violation;
import com.zuzex.vvolkov.exceptions.InvalidInputParameterExceptions;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CommonExceptionHandler {

    @ResponseBody
    @ExceptionHandler(PropertyValueException.class)
    ResponseEntity<ResponseMapper> propertyValueInvalid(PropertyValueException e) {
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        "property: " + e.getPropertyName() + ". " + e.getMessage(), e.getCause()));
    }

    @ResponseBody
    @ExceptionHandler(JsonMappingException.class)
    ResponseEntity<ResponseMapper> wrongJsonMapping(JsonMappingException e) {
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), e.getPath()));
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ResponseMapper> missingRequestParameter(MissingServletRequestParameterException e) {
        String response = e.getParameterName() + e.getMessage();
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        "parameter: " + e.getParameterName() + ". " + e.getMessage(), e.getCause()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ResponseMapper> onMethodArgumentNotValidException(
            ConstraintViolationException e) {
        List<Violation> errorList = new ArrayList<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            errorList.add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage())
            );
        }
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        "Invalid input parameters. Details in body", errorList));
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ResponseMapper> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<Violation> errorList = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorList.add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        "Invalid input parameters. Details in body", errorList));
    }

}
