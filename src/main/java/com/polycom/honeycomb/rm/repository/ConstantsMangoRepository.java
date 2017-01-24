package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.Constants;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Tao Chen on 2016/12/9.
 */
public interface ConstantsMangoRepository
        extends MongoRepository<Constants, String> {
}
