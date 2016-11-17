package com.polycom.honeycomb.rm.api;

import com.polycom.honeycomb.rm.api.dto.SelfTestRequest;
import com.polycom.honeycomb.rm.domain.MonitoringRecord;
import com.polycom.honeycomb.rm.domain.Resource;
import com.polycom.honeycomb.rm.repository.MonitoringRecordMangoRepository;
import com.polycom.honeycomb.rm.repository.ResourceMangoRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.time.LocalDateTime;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by weigao on 26/9/2016.
 */
@RestController @RequestMapping(value = "/api/monitorings")
public class MonitoringRecordController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(MonitoringRecordController.class);
    @Autowired ResourceMangoRepository         resourceRepository;
    @Autowired MonitoringRecordMangoRepository monitoringRecordRepository;

    EventBus eventBus = EventBus.create(Environment.initialize());

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MonitoringRecord createMonitoring(
            @RequestBody SelfTestRequest selfTestRequest) {
        final Resource resource = resourceRepository
                .findOne(selfTestRequest.getResourceId());

        MonitoringRecord newMonitoringRecord = new MonitoringRecord();
        newMonitoringRecord.setStartTime(LocalDateTime.now());
        newMonitoringRecord.setResource(resource);
        MonitoringRecord.StatusAndDetails statusAndDetails = new MonitoringRecord.StatusAndDetails();
        statusAndDetails.setStatus(MonitoringRecord.Status.ONGOING);
        newMonitoringRecord.setStatusAndDetails(statusAndDetails);
        monitoringRecordRepository.save(newMonitoringRecord);

        String recordId = newMonitoringRecord.getId();

        eventBus.receive($(recordId), (Event<Resource> ev) -> {
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MonitoringRecord.StatusAndDetails pass = new MonitoringRecord.StatusAndDetails();
            pass.setStatus(MonitoringRecord.Status.PASS);
            return pass;
        });

        eventBus.sendAndReceive(recordId, Event.wrap(resource), s -> {
            MonitoringRecord monitoringRecord = monitoringRecordRepository
                    .findOne(recordId);
            monitoringRecord
                    .setStatusAndDetails((MonitoringRecord.StatusAndDetails) s
                            .getData());
            monitoringRecord.setEndTime(LocalDateTime.now());
            monitoringRecordRepository.save(monitoringRecord);
        });

        return newMonitoringRecord;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<MonitoringRecord> listMonitoringRecords(Pageable pageable) {
        Page<MonitoringRecord> monitoringRecords = monitoringRecordRepository
                .findAll(pageable);

        return monitoringRecords;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public MonitoringRecord getOneMonitoringRecord(
            @PathVariable("id") String id) {
        return monitoringRecordRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMonitoringRecord(@PathVariable("id") String id) {
        monitoringRecordRepository.delete(id);
    }
}
