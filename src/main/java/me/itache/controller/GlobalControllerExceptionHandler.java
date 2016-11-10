package me.itache.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.regex.PatternSyntaxException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse noHandlerFoundException(Exception ex) {
        logger.error(ex.getClass().getSimpleName() + ":" + ex.getMessage());
        ex.printStackTrace();
        return new ApiErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse argumentMismatchExceptionException(Exception ex) {
        logger.error(ex.getClass().getSimpleName() + ":" + ex.getMessage());
        ex.printStackTrace();
        return new ApiErrorResponse(400, ex.getMessage());
    }

    @ExceptionHandler(value = { PatternSyntaxException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidRegExpExceptionException(Exception ex) {
        logger.error(ex.getClass().getSimpleName() + ":" + ex.getMessage());
        ex.printStackTrace();
        return new ApiErrorResponse(400, "Invalid regular expression");
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex) {
        logger.error(ex.getClass().getSimpleName() + ":" + ex.getMessage());
        logger.error(ex.getStackTrace().toString());
        return new ApiErrorResponse(500, ex.getMessage());
    }

}
