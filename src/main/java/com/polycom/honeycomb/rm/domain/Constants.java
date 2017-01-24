package com.polycom.honeycomb.rm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by Tao Chen on 2016/12/9.
 */
@Document(collection = "constants") public class Constants {
    @Id private String id;

    private String poolName = ResourcePool.DEFAULT_POOL_ID;

    private Map<String, String> constants;

    public Map<String, String> getConstants() {
        return constants;
    }

    public void setConstants(Map<String, String> constants) {
        this.constants = constants;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
