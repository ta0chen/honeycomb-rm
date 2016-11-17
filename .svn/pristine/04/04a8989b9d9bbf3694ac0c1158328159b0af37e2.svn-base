package com.polycom.honeycomb.rm.api.exception;

import com.polycom.honeycomb.rm.api.dto.BookingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by weigao on 14/9/2016.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Resource")
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String id) {
        super(id + " not available");
    }

    public ResourceNotFoundException(BookingRequest bookingRequest) {
        super("No resource found for " + bookingRequest);
    }
}
