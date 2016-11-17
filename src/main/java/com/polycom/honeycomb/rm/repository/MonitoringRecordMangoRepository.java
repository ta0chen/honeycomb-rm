package com.polycom.honeycomb.rm.repository;

import com.polycom.honeycomb.rm.domain.MonitoringRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by weigao on 26/9/2016.
 */
public interface MonitoringRecordMangoRepository
        extends MongoRepository<MonitoringRecord, String> {
}
