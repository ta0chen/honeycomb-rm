package com.polycom.honeycomb.rm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tao Chen on 2016/11/8.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Role")
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String id) {
        super(id + " not available");
    }
}
