package com.gmail.eugene.shchemelyov.market.web.exception.handler;

import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExpectedExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String defaultErrorHandler(
            HttpServletRequest httpServletRequest,
            ExpectedException e
    ) {
        httpServletRequest.setAttribute("exception", e.getMessage());
        return "error/expect";
    }
}
