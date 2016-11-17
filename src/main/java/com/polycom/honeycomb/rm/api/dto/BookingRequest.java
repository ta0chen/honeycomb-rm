package com.polycom.honeycomb.rm.api.dto;

/**
 * Created by weigao on 20/9/2016.
 */
public class BookingRequest {

    private String   poolName;
    private String   type;
    private String[] keywords;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
}
