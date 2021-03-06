package com.polycom.honeycomb.rm.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Created by Tao Chen on 2016/11/14.
 */
public class HttpHeaderUtil {
    public static HttpHeaders createBasicAuthHeader(String user,
            String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization",
                    "Basic " + CredentialUtil
                            .basicBase64CredTranslate(user, password));
        return headers;
    }
}
