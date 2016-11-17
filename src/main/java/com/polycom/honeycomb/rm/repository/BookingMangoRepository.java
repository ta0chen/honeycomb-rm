package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by weigao on 20/9/2016.
 */
public interface BookingMangoRepository
        extends MongoRepository<Booking, String> {
}
