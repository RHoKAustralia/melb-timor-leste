//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.rhok.linguist.network;;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.servicestack.client.IReturn;
import net.servicestack.client.JsonSerializers;
import net.servicestack.client.Log;
import net.servicestack.client.ServiceClient;
import net.servicestack.client.TimeSpan;
import net.servicestack.client.Utils;
import net.servicestack.client.WebServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public class SSClient implements ServiceClient {
    static Charset UTF8 = Charset.forName("UTF-8");
    String baseUrl;
    String replyUrl;
    Integer timeoutMs;
    public ConnectionFilter RequestFilter;
    public ConnectionFilter ResponseFilter;
    public ExceptionFilter ExceptionFilter;
    public static ConnectionFilter GlobalRequestFilter;
    public static ConnectionFilter GlobalResponseFilter;
    public static ExceptionFilter GlobalExceptionFilter;
    Gson gson;

    public SSClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/")?baseUrl:baseUrl + "/";
        this.replyUrl = this.baseUrl + "json/reply/";
    }

    public void setTimeout(int timeoutMs) {
        this.timeoutMs = Integer.valueOf(timeoutMs);
    }

    public GsonBuilder getGsonBuilder() {
        return (new GsonBuilder()).registerTypeAdapter(Date.class, JsonSerializers.getDateSerializer()).registerTypeAdapter(Date.class, JsonSerializers.getDateDeserializer()).registerTypeAdapter(TimeSpan.class, JsonSerializers.getTimeSpanSerializer()).registerTypeAdapter(TimeSpan.class, JsonSerializers.getTimeSpanDeserializer()).registerTypeAdapter(UUID.class, JsonSerializers.getGuidSerializer()).registerTypeAdapter(UUID.class, JsonSerializers.getGuidDeserializer());
    }

    public Gson getGson() {
        if(this.gson == null) {
            this.gson = this.getGsonBuilder().create();
        }

        return this.gson;
    }

    public String toJson(Object o) {
        String json = this.getGson().toJson(o);
        return json;
    }

    public Object fromJson(String json, Class c) {
        Object o = this.getGson().fromJson(json, c);
        return o;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String createUrl(Object requestDto) {
        return this.createUrl(requestDto, (Map)null);
    }

    public String createUrl(Object requestDto, Map<String, String> query) {
        String requestUrl = this.replyUrl + requestDto.getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        Object lastField = null;

        try {
            Field[] e = Utils.getSerializableFields(requestDto.getClass());
            int entry = e.length;

            for(int key = 0; key < entry; ++key) {
                Field val = e[key];
                Object val1 = val.get(requestDto);
                if(val1 != null) {
                    sb.append(sb.length() == 0?"?":"&");
                    sb.append(URLEncoder.encode(val.getName(), "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(val1.toString(), "UTF-8"));
                }
            }

            if(query == null) {
                return requestUrl + sb.toString();
            } else {
                Iterator var12 = query.entrySet().iterator();

                while(var12.hasNext()) {
                    Entry var13 = (Entry)var12.next();
                    String var14 = (String)var13.getKey();
                    String var15 = (String)var13.getValue();
                    sb.append(sb.length() == 0?"?":"&");
                    sb.append(URLEncoder.encode(var14, "UTF-8"));
                    sb.append("=");
                    if(var15 != null) {
                        sb.append(URLEncoder.encode(var15, "UTF-8"));
                    }
                }

                return requestUrl + sb.toString();
            }
        } catch (UnsupportedEncodingException | IllegalAccessException var11) {
            throw new RuntimeException(var11);
        }
    }

    public HttpURLConnection createRequest(String url, String httpMethod) {
        return this.createRequest(url, httpMethod, (byte[])null, (String)null);
    }

    public HttpURLConnection createRequest(String url, String httpMethod, Object request) {
        String contentType = null;
        byte[] requestBody = null;
        if(request != null) {
            contentType = "application/json";
            String json = this.getGson().toJson(request);
            if(Log.isDebugEnabled()) {
                Log.d(json);
            }

            requestBody = json.getBytes(UTF8);
        }

        return this.createRequest(url, httpMethod, requestBody, contentType);
    }

    public HttpURLConnection createRequest(String requestUrl, String httpMethod, byte[] requestBody, String requestType) {
        try {
            URL e = new URL(requestUrl);
            HttpURLConnection req = (HttpURLConnection)e.openConnection();
            if(this.timeoutMs != null) {
                req.setConnectTimeout(this.timeoutMs.intValue());
                req.setReadTimeout(this.timeoutMs.intValue());
            }

            req.setRequestMethod(httpMethod);
            req.setRequestProperty("Accept", "application/json");
            if(requestType != null) {
                req.setRequestProperty("Content-Type", requestType);
            }

            if(requestBody != null) {
                req.setRequestProperty("Content-Length", Integer.toString(requestBody.length));
                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
                wr.write(requestBody);
                wr.flush();
                wr.close();
            }

            if(this.RequestFilter != null) {
                this.RequestFilter.exec(req);
            }

            if(GlobalRequestFilter != null) {
                GlobalRequestFilter.exec(req);
            }

            return req;
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }
    }

    public static RuntimeException createException(HttpURLConnection res, int responseCode) {
        WebServiceException webEx = null;

        try {
            String e = Utils.readToEnd(res.getErrorStream(), UTF8.name());
            webEx = new WebServiceException(responseCode, res.getResponseMessage(), e);
            if(Utils.matchesContentType(res.getHeaderField("Content-Type"), "application/json")) {
                JSONObject jResponse = new JSONObject(e);
                Iterator keys = jResponse.keys();

                while(keys.hasNext()) {
                    String key = (String)keys.next();
                    String varName = Utils.sanitizeVarName(key);
                    if(varName.equals("responsestatus")) {
                        webEx.setResponseStatus(Utils.createResponseStatus(jResponse.get(key)));
                        break;
                    }
                }
            }

            return webEx;
        } catch (JSONException | IOException var8) {
            return (RuntimeException)(webEx != null?webEx:new RuntimeException(var8));
        }
    }

    public <TResponse> TResponse send(HttpURLConnection req, Object responseClass) {
        Object var9;
        try {
            Class e = responseClass instanceof Class?(Class)responseClass:null;
            Type resType = responseClass instanceof Type?(Type)responseClass:null;
            if(e == null && resType == null) {
                throw new RuntimeException("responseClass \'" + responseClass.getClass().getSimpleName() + "\' must be a Class or Type");
            }

            int responseCode = req.getResponseCode();
            if(responseCode >= 400) {
                RuntimeException is1 = createException(req, responseCode);
                if(this.ExceptionFilter != null) {
                    this.ExceptionFilter.exec(req, is1);
                }

                if(GlobalExceptionFilter != null) {
                    GlobalExceptionFilter.exec(req, is1);
                }

                throw is1;
            }

            if(this.ResponseFilter != null) {
                this.ResponseFilter.exec(req);
            }

            if(GlobalRequestFilter != null) {
                GlobalRequestFilter.exec(req);
            }

            InputStream is = req.getInputStream();
            Object response;
            if(!Log.isDebugEnabled()) {
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(is));
                response = e != null?this.getGson().fromJson(reader1, e):this.getGson().fromJson(reader1, resType);
                reader1.close();
                var9 = response;
                return (TResponse) var9;
            }

            String reader = Utils.readToEnd(is, UTF8.name());
            Log.d(reader);
            response = e != null?this.getGson().fromJson(reader, e):this.getGson().fromJson(reader, resType);
            var9 = response;
        } catch (IOException var13) {
            throw new RuntimeException(var13);
        } finally {
            req.disconnect();
        }

        return (TResponse) var9;
    }

    static String typeName(Object o) {
        return o.getClass().getSimpleName();
    }

    private String resolveUrl(String relativeOrAbsoluteUrl) {
        return !relativeOrAbsoluteUrl.startsWith("http:") && !relativeOrAbsoluteUrl.startsWith("https:")?Utils.combinePath(this.baseUrl, relativeOrAbsoluteUrl):relativeOrAbsoluteUrl;
    }

    public <TResponse> TResponse get(IReturn<TResponse> request) {
        return this.send(this.createRequest(this.createUrl(request), "GET"), request.getResponseType());
    }

    public <TResponse> TResponse get(IReturn<TResponse> request, Map<String, String> queryParams) {
        return this.send(this.createRequest(this.createUrl(request, queryParams), "GET"), request.getResponseType());
    }

    public <TResponse> TResponse get(String path, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "GET"), responseType);
    }

    public <TResponse> TResponse get(String path, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "GET"), responseType);
    }

    public HttpURLConnection get(String path) {
        return this.createRequest(this.resolveUrl(path), "GET");
    }

    public <TResponse> TResponse post(IReturn<TResponse> request) {
        return this.send(this.createRequest(Utils.combinePath(this.replyUrl, typeName(request)), "POST", request), request.getResponseType());
    }

    public <TResponse> TResponse post(String path, Object request, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "POST", request), responseType);
    }

    public <TResponse> TResponse post(String path, Object request, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "POST", request), responseType);
    }

    public <TResponse> TResponse post(String path, byte[] requestBody, String contentType, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "POST", requestBody, contentType), responseType);
    }

    public <TResponse> TResponse post(String path, byte[] requestBody, String contentType, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "POST", requestBody, contentType), responseType);
    }

    public HttpURLConnection post(String path, byte[] requestBody, String contentType) {
        return this.createRequest(this.resolveUrl(path), "POST", requestBody, contentType);
    }

    public <TResponse> TResponse put(IReturn<TResponse> request) {
        return this.send(this.createRequest(Utils.combinePath(this.replyUrl, typeName(request)), "PUT", request), request.getResponseType());
    }

    public <TResponse> TResponse put(String path, Object request, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "PUT", request), responseType);
    }

    public <TResponse> TResponse put(String path, Object request, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "PUT", request), responseType);
    }

    public <TResponse> TResponse put(String path, byte[] requestBody, String contentType, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "PUT", requestBody, contentType), responseType);
    }

    public <TResponse> TResponse put(String path, byte[] requestBody, String contentType, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "PUT", requestBody, contentType), responseType);
    }

    public HttpURLConnection put(String path, byte[] requestBody, String contentType) {
        return this.createRequest(this.resolveUrl(path), "PUT", requestBody, contentType);
    }

    public <TResponse> TResponse delete(IReturn<TResponse> request) {
        return this.send(this.createRequest(this.createUrl(request), "DELETE"), request.getResponseType());
    }

    public <TResponse> TResponse delete(IReturn<TResponse> request, Map<String, String> queryParams) {
        return this.send(this.createRequest(this.createUrl(request, queryParams), "DELETE"), request.getResponseType());
    }

    public <TResponse> TResponse delete(String path, Class responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "DELETE"), responseType);
    }

    public <TResponse> TResponse delete(String path, Type responseType) {
        return this.send(this.createRequest(this.resolveUrl(path), "DELETE"), responseType);
    }

    public HttpURLConnection delete(String path) {
        return this.createRequest(this.resolveUrl(path), "DELETE");
    }





}
