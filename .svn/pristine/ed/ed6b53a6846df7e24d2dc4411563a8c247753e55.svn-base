package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by weigao on 13/9/2016.
 */
public interface ResourceMangoRepository
        extends MongoRepository<Resource, String> {
    Page<Resource> findByTypeIgnoreCase(String type, Pageable pageable);
}