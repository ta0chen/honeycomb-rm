package com.polycom.honeycomb.rm.constants;

import com.polycom.honeycomb.rm.domain.Constants;
import com.polycom.honeycomb.http.HttpClient;
import com.polycom.honeycomb.json.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Chen on 2016/12/12.
 */
public class ConstantsOperations {
    private String      CONFIG_BASE_URL = "/api/constants";
    private HttpClient  httpClient      = new HttpClient();
    private HttpRequest httpRequest     = new HttpRequest();

    public ConstantsOperations(String host) {
        httpRequest.protocol("http").host(host).port(HoneyRmConst.PORT);
    }

    /**
     * Create configuration for specific pool
     *
     * @param loginUser     Login user
     * @param loginPassword Login password
     * @param poolName      The pool name
     * @param detailConfig  The detail constants
     * @return The HTTP response
     */
    public HttpResponse createConfigurations(String loginUser,
            String loginPassword,
            String poolName,
            Map<String, String> detailConfig) {
        Constants configs = new Constants();
        configs.setPoolName(poolName);
        configs.setConstants(detailConfig);

        String requestString = JsonUtils.toJson(configs);
        httpRequest.basicAuthentication(loginUser, loginPassword)
                .method(HoneyRmConst.POST).path(CONFIG_BASE_URL)
                .bodyText(requestString)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }

    /**
     * Get the constants which belongs to the same pool with the login user
     *
     * @param loginUser     Login user
     * @param loginPassword Login password
     * @return The list of the constants
     */
    public List<Constants> getConfigs(String loginUser, String loginPassword) {
        httpRequest.basicAuthentication(loginUser, loginPassword)
                .method(HoneyRmConst.GET).path(CONFIG_BASE_URL)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        final String responseBodyText = response.bodyText();
        return JsonUtils.fromJson(responseBodyText,
                                  new ArrayList<Constants>().getClass(),
                                  Constants.class);
    }

    /**
     * Delete the constants
     *
     * @param loginUser     Login user
     * @param loginPassword Login password
     * @param id            The configuration ID
     * @return The HTTP response
     */
    public HttpResponse deleteConfigurations(String loginUser,
            String loginPassword,
            String id) {
        httpRequest.basicAuthentication(loginUser, loginPassword)
                .method(HoneyRmConst.DELETE).path(CONFIG_BASE_URL + "/" + id)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        return response;
    }
}
