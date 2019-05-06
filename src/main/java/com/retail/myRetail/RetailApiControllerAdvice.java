package com.retail.myRetail;

import com.retail.myRetail.exception.DataNotFoundException;
import com.retail.myRetail.message.RetailErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller exception handler.
 * <p>
 * For centralized exception handling for Retail API
 */
@ControllerAdvice
public class RetailApiControllerAdvice {


    private void getResponseMap(Map<String, Object> map, HttpServletRequest request, String message) {
        map.put("path", request.getRequestURI());
        if (StringUtils.isNotBlank(message)) {
            map.put("message", message);
        }
    }

    @ResponseBody
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDataNotFoundException(HttpServletRequest request, DataNotFoundException e) {

        Map<String, Object> map = new HashMap();

        getResponseMap(map, request, e.getMessage() == null ? RetailErrorCode.NOTFOUND : e.getMessage());
        return new ResponseEntity(map, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                     MethodArgumentNotValidException exception) {

        Map<String, Object> map = processBindingResult(exception.getBindingResult());
        getResponseMap(map, request, "");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> processBindingResult(BindingResult bindingResult) {
        Map<String, Object> map = new HashMap();
        map.put("target", bindingResult.getTarget());
        map.put("fieldErrors", processFieldErrors(bindingResult.getFieldErrors()));
        return map;
    }

    private List<Map<String, Object>> processFieldErrors(List<FieldError> fieldErrors) {
        List<Map<String, Object>> errors = new ArrayList();
        for (FieldError fieldError : fieldErrors) {
            Map<String, Object> error = new HashMap();
            error.put("field", fieldError.getField());
            error.put("rejectedValue", fieldError.getRejectedValue());
            errors.add(error);
        }
        return errors;
    }
}
