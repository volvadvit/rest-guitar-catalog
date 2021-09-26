package com.zuzex.vvolkov.components;

import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.exceptions.GuitarNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GuitarExceptionHandler {

    @ResponseBody
    @ExceptionHandler(GuitarNotExistsException.class)
    ResponseEntity<ResponseMapper> guitarNotExist(GuitarNotExistsException e) {
        return ResponseEntity.badRequest().body(
                new ResponseMapper(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), e.getCause()));
    }
}
