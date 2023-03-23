package com.example.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * эксепш - класс
 * {@link pro.sky.whiskerspawstailtelegrambot.exception.CustomExceptionHandler#handleInvalidTraceIdException(IFElementExist)}
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ElemNotFound extends RuntimeException {
    public ElemNotFound() {

    }
}
