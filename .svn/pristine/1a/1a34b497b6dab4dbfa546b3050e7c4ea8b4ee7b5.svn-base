package com.polycom.honeycomb.rm.booking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polycom.honeycomb.rm.api.dto.BookingRequest;
import com.polycom.honeycomb.rm.constants.HoneyRmConst;
import com.polycom.honeycomb.rm.domain.Booking;
import com.polycom.honeycomb.rm.utils.PageImplBean;
import com.polycom.http.HttpClient;
import com.polycom.json.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Chen on 2016/11/17.
 */
public class BookingOperations {
    private String      BOOKING_BASE_URL = "/api/bookings";
    private HttpClient  httpClient       = new HttpClient();
    private HttpRequest httpRequest      = new HttpRequest();

    /**
     * The construction method
     *
     * @param host The host IP address
     */
    public BookingOperations(String host) {
        httpRequest.protocol("http").host(host).port(HoneyRmConst.PORT);
    }

    /**
     * List all resources already booked
     *
     * @param user     The login user
     * @param password The password
     * @return The booking list
     */
    public List<Booking> getResourceBookingList(String user, String password) {
        Page<Booking> bookingsPage = new PageImpl<Booking>(new ArrayList<Booking>());
        httpRequest.basicAuthentication(user, password).method(HoneyRmConst.GET)
                .path(BOOKING_BASE_URL)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        final String responseBodyText = response.bodyText();

        try {
            bookingsPage = ((PageImplBean<Booking>) new ObjectMapper()
                    .findAndRegisterModules().readValue(responseBodyText,
                                                        new TypeReference<PageImplBean<Booking>>() {
                                                        })).pageImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingsPage.getContent();
    }

    /**
     * Book a resource according to the request
     *
     * @param user     The login user
     * @param password The password
     * @param type     The resource device type
     * @param poolName The pool name where the resource located in
     * @param keywords The keywords related to the resource
     * @return HttpResponse
     */
    public HttpResponse bookingResource(String user,
            String password,
            String type,
            String poolName,
            String[] keywords) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setType(type);
        bookingRequest.setPoolName(poolName);
        bookingRequest.setKeywords(keywords);

        String requestString = JsonUtils.toJson(bookingRequest);
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.POST).path(BOOKING_BASE_URL)
                .bodyText(requestString)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }

    /**
     * Release a booked resource in the system
     *
     * @param user     The login user
     * @param password The password
     * @param id       The booking ID
     * @return HttpResponse
     */
    public HttpResponse releaseResourceBooking(String user,
            String password,
            String id) {
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.DELETE).path(BOOKING_BASE_URL + "/" + id)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }
}
