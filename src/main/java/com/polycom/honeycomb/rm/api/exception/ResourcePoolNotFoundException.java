package com.polycom.honeycomb.rm.api.exception;

import com.polycom.honeycomb.rm.domain.ResourcePool;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by weigao on 14/9/2016.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Resource Pool")
public class ResourcePoolNotFoundException extends RuntimeException {
    public ResourcePoolNotFoundException(String id) {
        super(id + " not available");
    }

    public ResourcePoolNotFoundException(ResourcePool resourcePool) {
        super("Resource pool not found " + resourcePool);
    }
}
