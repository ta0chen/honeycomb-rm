package com.polycom.honeycomb.rm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tao Chen on 2016/12/9.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Configuration")
public class ConstantsNotFoundException extends RuntimeException {
    public ConstantsNotFoundException(String id) {
        super(id + "not available");
    }
}
