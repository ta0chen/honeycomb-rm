package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Tao Chen on 2016/10/26.
 */
public interface UserMongoRepository extends MongoRepository<User, String> {
    //    Page<User> findByUserName(String userName, Pageable pageable);
    User findByUserName(String userName);
}
