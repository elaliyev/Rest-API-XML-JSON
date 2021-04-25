package com.example.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Helper {




    public static Map<String, String> getRequestParameters(String query) {

        Map<String, String> parameters = new HashMap<>();

        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                parameters.put(pair[0], pair[1]);
            }else{
                parameters.put(pair[0], "");
            }
        }
        return parameters;
    }


}
