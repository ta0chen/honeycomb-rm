package com.polycom.honeycomb.rm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tao Chen on 2016/11/11.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal request")
public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(Object o) {
        super("Illegal request: " + o);
    }
}
