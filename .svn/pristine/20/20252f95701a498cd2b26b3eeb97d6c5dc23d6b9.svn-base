package com.polycom.honeycomb.rm.utils;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by Tao Chen on 2016/11/9.
 */
public class CredentialUtil {
    public static String basicBase64CredTranslate(String user,
            String password) {
        String plainCreds = user + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        String base64Creds = DatatypeConverter
                .printBase64Binary(plainCredsBytes);
        return base64Creds;
    }
}
