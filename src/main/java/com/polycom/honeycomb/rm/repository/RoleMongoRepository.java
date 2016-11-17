package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Tao Chen on 2016/10/28.
 */
public interface RoleMongoRepository extends MongoRepository<Role, String> {
}
