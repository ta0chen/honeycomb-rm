package com.polycom.honeycomb.rm.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polycom.honeycomb.rm.constants.HoneyRmConst;
import com.polycom.honeycomb.rm.domain.Resource;
import com.polycom.honeycomb.rm.utils.PageImplBean;
import com.polycom.honeycomb.http.HttpClient;
import com.polycom.honeycomb.json.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Chen on 2016/11/16.
 */
public class ResourceOperations {
    private String      RESOURCE_BASE_URL = "/api/resources";
    private HttpClient  httpClient        = new HttpClient();
    private HttpRequest httpRequest       = new HttpRequest();

    public ResourceOperations(String host) {
        httpRequest.protocol("http").host(host).port(HoneyRmConst.PORT);
    }

    public List<Resource> getResource(String user, String password) {
        Page<Resource> resourcesPage = new PageImpl<Resource>(new ArrayList<Resource>());
        httpRequest.basicAuthentication(user, password).method(HoneyRmConst.GET)
                .path(RESOURCE_BASE_URL)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        final String responseBodyText = response.bodyText();

        try {
            resourcesPage = ((PageImplBean<Resource>) new ObjectMapper()
                    .readValue(responseBodyText,
                               new TypeReference<PageImplBean<Resource>>() {
                               })).pageImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourcesPage.getContent();
    }

    public HttpResponse createResource(String user,
            String password,
            String type,
            String[] keywords,
            String poolName,
            Map<String, Object> infos) {
        Resource resource = new Resource();
        resource.setType(type);
        resource.setKeywords(keywords);
        resource.setInfos(infos);
        resource.setPoolId(poolName);

        String requestString = JsonUtils.toJson(resource);
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.POST).path(RESOURCE_BASE_URL)
                .bodyText(requestString)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }

    public HttpResponse deleteResource(String user,
            String password,
            String id) {
        httpRequest.basicAuthentication(user, password)
                .method(HoneyRmConst.DELETE).path(RESOURCE_BASE_URL + "/" + id)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }
}
