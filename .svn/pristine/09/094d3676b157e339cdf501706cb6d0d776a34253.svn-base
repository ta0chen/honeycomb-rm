package com.polycom.honeycomb.rm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tao Chen on 2016/11/16.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Can not delete Resource Pool with resources")
public class ResourcePoolCanNotBeDeletedWithResourcesException
        extends RuntimeException {
    public ResourcePoolCanNotBeDeletedWithResourcesException() {
        super();
    }
}
