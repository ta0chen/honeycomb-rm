package com.polycom.honeycomb.rm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by weigao on 26/9/2016.
 */
@Document(collection = "monitoringrecords") public class MonitoringRecord {
    @Id private String id;

    private LocalDateTime    startTime;
    private LocalDateTime    endTime;
    private StatusAndDetails statusAndDetails;

    @DBRef private Resource resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public StatusAndDetails getStatusAndDetails() {
        return statusAndDetails;
    }

    public void setStatusAndDetails(StatusAndDetails statusAndDetails) {
        this.statusAndDetails = statusAndDetails;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public static class StatusAndDetails {
        private Status status;
        private String details;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }

    public static enum Status {
        ONGOING, PASS, FAIL
    }
}
