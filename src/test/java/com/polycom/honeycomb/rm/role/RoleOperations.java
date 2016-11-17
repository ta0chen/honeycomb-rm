package com.polycom.honeycomb.rm.role;

import com.polycom.honeycomb.rm.constants.HoneyRmConst;
import com.polycom.honeycomb.rm.domain.Role;
import com.polycom.http.HttpClient;
import com.polycom.json.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Chen on 2016/11/15.
 */
public class RoleOperations {
    private String      ROLE_BASE_URL = "/api/roles";
    private HttpClient  httpClient    = new HttpClient();
    private HttpRequest httpRequest   = new HttpRequest();

    public RoleOperations(String host) {
        httpRequest.protocol("http").host(host).port(HoneyRmConst.PORT);
    }

    public List<Role> getRoles(String user, String password) {
        httpRequest.basicAuthentication(user, password).method(HoneyRmConst.GET)

                .path(ROLE_BASE_URL)
                .contentType(HoneyRmConst.CONTENT_TYPE, "UTF-8");
        final HttpResponse response = httpClient.send(httpRequest);
        final String responseBodyText = response.bodyText();
        return JsonUtils.fromJson(responseBodyText,
                                  new ArrayList<Role>().getClass(),
                                  Role.class);
    }
}
