package com.polycom.honeycomb.rm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tao Chen on 2016/11/11.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find the booking")
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String id) {
        super("Cannot find the booking with ID: " + id);
    }
}
