package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.ResourcePool;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by weigao on 14/9/2016.
 */
public interface ResourcePoolMangoRepository
        extends MongoRepository<ResourcePool, String> {
    ResourcePool findFirstByNameIgnoreCase(String name);
}
