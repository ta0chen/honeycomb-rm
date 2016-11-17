package com.polycom.honeycomb.rm.pool;

import com.polycom.honeycomb.rm.constants.HoneyRmConst;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.http.HttpClient;
import com.polycom.json.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Chen on 2016/11/15.
 */
public class PoolOperations {
    private String      RESOURCE_POOL_BASE_URL = "/api/pools";
    private HttpClient  httpClient             = new HttpClient();
    private HttpRequest httpRequest            = new HttpRequest();

    public PoolOperations(String host) {
        httpRequest.protocol("http").host(host).port(HoneyRmConst.PORT);
    }

    public List<ResourcePool> getResourcePool(String user, String password) {
        httpRequest.basicAuthentication(user, password).method(HoneyRmConst.GET)
                .path(RESOURCE_POOL_BASE_URL)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        final String responseBodyText = response.bodyText();
        return JsonUtils.fromJson(responseBodyText,
                                  new ArrayList<ResourcePool>().getClass(),
                                  ResourcePool.class);
    }

    public HttpResponse createResourcePool(String user,
            String password,
            String name,
            String description) {
        ResourcePool pool = new ResourcePool();
        pool.setName(name);
        pool.setDescription(description);

        String requestString = JsonUtils.toJson(pool);
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.POST).path(RESOURCE_POOL_BASE_URL)
                .bodyText(requestString)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }

    public HttpResponse deleteResourcePool(String user,
            String password,
            String id) {
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.DELETE)
                .path(RESOURCE_POOL_BASE_URL + "/" + id)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }
}
