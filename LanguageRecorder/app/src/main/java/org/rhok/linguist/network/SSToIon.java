//  Copyright (c) 2015 ServiceStack LLC. All rights reserved.
//  License: https://servicestack.net/bsd-license.txt

package org.rhok.linguist.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.servicestack.client.AsyncServiceClient;
import net.servicestack.client.ConnectionFilter;
import net.servicestack.client.ExceptionFilter;
import net.servicestack.client.HttpHeaders;
import net.servicestack.client.HttpMethods;
import net.servicestack.client.IReturn;
import net.servicestack.client.JsonSerializers;
import net.servicestack.client.Log;
import net.servicestack.client.MimeTypes;
import net.servicestack.client.ServiceClient;
import net.servicestack.client.TimeSpan;
import net.servicestack.client.Utils;
import net.servicestack.client.WebServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public class SSToIon  {

    public final String baseUrl;
    public final String replyUrl;



    public SSToIon(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.replyUrl = this.baseUrl + "json/reply/";
    }





    public String createUrl(Object requestDto){
        return createUrl(requestDto, null);
    }

    public String createUrlNoParams(Object requestDto){
        return Utils.combinePath(replyUrl, typeName(requestDto));
    }

    public String createUrl(Object requestDto, Map<String,String> query){
        String requestUrl = this.replyUrl + requestDto.getClass().getSimpleName();

        StringBuilder sb = new StringBuilder();
        Field lastField = null;
        try {
            for (Field f : Utils.getSerializableFields(requestDto.getClass())) {
                Object val = f.get(requestDto);

                if (val == null)
                    continue;

                sb.append(sb.length() == 0 ? "?" : "&");
                sb.append(URLEncoder.encode(f.getName(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(val.toString(), "UTF-8"));
            }

            if (query != null) {
                for (Map.Entry<String, String> entry : query.entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();

                    sb.append(sb.length() == 0 ? "?" : "&");
                    sb.append(URLEncoder.encode(key, "UTF-8"));
                    sb.append("=");
                    if (val != null) {
                        sb.append(URLEncoder.encode(val, "UTF-8"));
                    }
                }
            }
        } catch (IllegalAccessException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return requestUrl + sb.toString();
    }





    public static String typeName(Object o){
        return o.getClass().getSimpleName();
    }

    public String resolveUrl(String relativeOrAbsoluteUrl) {
        return relativeOrAbsoluteUrl.startsWith("http:")
                || relativeOrAbsoluteUrl.startsWith("https:")
                ? relativeOrAbsoluteUrl
                : Utils.combinePath(baseUrl, relativeOrAbsoluteUrl);
    }



}