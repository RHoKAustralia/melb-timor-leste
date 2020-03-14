package org.rhok.linguist.network;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.DataSink;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.future.SimpleFuture;
import com.koushikdutta.async.future.TransformFuture;
import com.koushikdutta.async.parser.AsyncParser;
import com.koushikdutta.async.parser.ByteBufferListParser;
import com.koushikdutta.async.parser.StringParser;
import com.koushikdutta.async.stream.ByteBufferListInputStream;
import com.koushikdutta.ion.HeadersCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;

import net.servicestack.client.IReturn;
import net.servicestack.client.JsonSerializers;
import net.servicestack.client.ResponseStatus;
import net.servicestack.client.TimeSpan;

import org.rhok.linguist.BuildConfig;
import org.rhok.linguist.application.LinguistApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bramleyt on 20/07/2015.
 */
public class IonHelper {
    public interface IonHelperCallbacks {
        void onIonRequestPreExecute(HelperRequest<?> request);
        void onIonRequestStarting(HelperRequest<?> request);
        void onIonRequestFinished(HelperRequest<?> request, Response<?> response);
    }

    public static final String TAG = "IonHelper";
    public static final boolean LOG_REQUEST = LinguistApplication.DEBUG;
    public static final boolean LOG_RESPONSE = LinguistApplication.DEBUG;
    private static final int TIMEOUT_MS = 3000;
    private static Ion sIon;
    private Ion ion;

    private IonHelperCallbacks mConfigLoaderCallback;
    public IonHelper(){
        this(null);
    }
    public IonHelper(IonHelperCallbacks configureLoaderCallback){
        mConfigLoaderCallback=configureLoaderCallback;
        if(sIon==null){
            sIon = Ion.getDefault(LinguistApplication.getContextStatic());
            ion=sIon;
            initDefaults();
        }
        else {
            ion = sIon;
        }
    }
    public IonHelper(Ion instance, IonHelperCallbacks configureLoaderCallback){
        mConfigLoaderCallback=configureLoaderCallback;
        ion=instance;
        initDefaults();
    }
    
    private void initDefaults(){
        //ssToIon=new SSToIon(EQPayApplication.getWebserviceUrl());
        Ion.Config config = ion.configure();
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Date.class, JsonSerializers.getDateSerializer())
                .registerTypeAdapter(Date.class, PCJsonSerializers.getDateDeserializer())
                .registerTypeAdapter(TimeSpan.class, JsonSerializers.getTimeSpanSerializer())
                .registerTypeAdapter(TimeSpan.class, JsonSerializers.getTimeSpanDeserializer())
                .registerTypeAdapter(UUID.class, JsonSerializers.getGuidSerializer())
                .registerTypeAdapter(UUID.class, JsonSerializers.getGuidDeserializer());
        builder.setExclusionStrategies(PCJsonSerializers.getUnderscoreExclusionStrategy());
        config.setGson(builder.create());
        config.userAgent(getDefaultUserAgentString());
        config.setLogging("ION", LinguistApplication.DEBUG ? 0 : Log.ASSERT + 1);
    }

    public Ion getIon(){
        return ion;
    }
    public LoadBuilder<Builders.Any.B> build(Fragment fragment){
        return ion.build(fragment);
    }

    public <TResponse> HelperRequest<TResponse> doGet(Fragment f, IReturn<TResponse> request){
        return doGet(build(f), request);
    }
    public <TResponse> HelperRequest<TResponse> doGet(LoadBuilder<Builders.Any.B> loadBuilder, Class<TResponse> responseClass, String urlPath){
        if(urlPath.startsWith("/")) urlPath=urlPath.substring(1);
        String url = LinguistApplication.getWebserviceUrl()+urlPath;
        Builders.Any.B b2 = loadBuilder.load(url);
        b2.setTimeout(TIMEOUT_MS);
        addDefaultHeaders(b2);
        return new HelperRequest<>(mConfigLoaderCallback, "GET", b2, url, responseClass);
    }
    public <TResponse> HelperRequest<TResponse> doGet(LoadBuilder<Builders.Any.B> loadBuilder, IReturn<TResponse> request){
        String url = new SSToIon(LinguistApplication.getWebserviceUrl()).createUrl(request);
        Builders.Any.B b2 = loadBuilder.load(url);
        addDefaultHeaders(b2);

        return new HelperRequest<>(request, mConfigLoaderCallback, "GET", b2, url);
    }

    public <TResponse> HelperRequest<TResponse> doPost(LoadBuilder<Builders.Any.B> loadBuilder, IReturn<TResponse> request, String urlPath){
        if(urlPath.startsWith("/")) urlPath=urlPath.substring(1);
        String url = LinguistApplication.getWebserviceUrl()+urlPath;
        Builders.Any.B b2 = loadBuilder.load(url);
        addDefaultHeaders(b2);
        if(LOG_REQUEST){
            String json = ion.configure().getGson().toJson(request);
            Log.d("requestDump", json);
            b2.setStringBody(json);
            b2.setHeader("Content-Type", "application/json");
        }
        else b2.setJsonPojoBody(request);
        return new HelperRequest<>(request, mConfigLoaderCallback, "POST", b2, url);
    }

    public <TResponse> HelperRequest<TResponse> doPost(LoadBuilder<Builders.Any.B> loadBuilder, IReturn<TResponse> request){
        String url = new SSToIon(LinguistApplication.getWebserviceUrl()).createUrlNoParams(request);
        Builders.Any.B b2 = loadBuilder.load(url);
        addDefaultHeaders(b2);
        if(LOG_REQUEST){
            String json = ion.configure().getGson().toJson(request);
            Log.d("requestDump", json);
            b2.setStringBody(json);
            b2.setHeader("Content-Type", "application/json");
        }
        else b2.setJsonPojoBody(request);
        return new HelperRequest<>(request, mConfigLoaderCallback, "POST", b2, url);
    }
    public <TResponse> HelperRequest<TResponse> doPut(LoadBuilder<Builders.Any.B> loadBuilder, IReturn<TResponse> request){
        String url = new SSToIon(LinguistApplication.getWebserviceUrl()).createUrlNoParams(request);
        Builders.Any.B b2 = loadBuilder.load("PUT", url);
        addDefaultHeaders(b2);
        if(LOG_REQUEST){
            String json = ion.configure().getGson().toJson(request);
            Log.d("requestDump", json);
            b2.setStringBody(json);
            b2.setHeader("Content-Type", "application/json");
        }
        else b2.setJsonPojoBody(request);

        return new HelperRequest<>(request, mConfigLoaderCallback, "PUT", b2, url);
    }
    public <TResponse> HelperRequest<TResponse> doDelete(LoadBuilder<Builders.Any.B> loadBuilder, IReturn<TResponse> request){
        String url = new SSToIon(LinguistApplication.getWebserviceUrl()).createUrl(request);
        Builders.Any.B b2 = loadBuilder.load("DELETE", url);
        addDefaultHeaders(b2);
        return new HelperRequest<>(request, mConfigLoaderCallback, "DELETE", b2, url);
    }

    private <TResponse> Future<Response<TResponse>> setTypeByClass(Builders.Any.B b2, Class<TResponse> resClass){

        CustomGson<TResponse> customGson= new CustomGson<TResponse>(ion.configure().getGson(), resClass);

        return b2.as(customGson).withResponse();
    }
    private <TResponse> Future<Response<TResponse>> setType(Builders.Any.B b2, IReturn<TResponse> request){
        Object responseClass = request.getResponseType();
        Class resClass = responseClass instanceof Class ? (Class)responseClass : null;
        Type resType = responseClass instanceof Type ? (Type)responseClass : null;
        if (resClass == null && resType == null)
            throw new RuntimeException("responseClass '" + responseClass.getClass().getSimpleName() + "' must be a Class or Type");
        CustomGson<TResponse> customGson;
        if(resClass!=null){
            customGson=new CustomGson(ion.configure().getGson(), resClass);
        }
        else{
            customGson=new CustomGson(ion.configure().getGson(), (TypeToken)TypeToken.get(resType));
        }
        return b2.as(customGson).withResponse();
        //if(resClass!=null)return b2.as(resClass).withResponse();
        //return b2.as((TypeToken)TypeToken.get(resType)).withResponse();
    }



    private String getDefaultUserAgentString() {
        String device = (Build.MANUFACTURER+" "+Build.MODEL)
                .replace('/', '-').replace('(', '-').replace(')', '-');
        return String.format("%s/%s (%d);android/%s (%d);%s",
                LinguistApplication.getContextStatic().getPackageName(),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                device
        );
    }

    public Builders.Any.B addDefaultHeaders(Builders.Any.B builder){
        return builder.setHeader("Accept", "application/json");
    }

    public static class ResponseStatusException extends Exception{
        HeadersResponse mHeaders;
        ResponseStatus mResponseStatus;
        public ResponseStatusException(HeadersResponse headers, ResponseStatus responseStatus){
            super("HTTP "+headers.code()+": "+headers.message());
            this.mHeaders=headers;
            this.mResponseStatus=responseStatus;

        }
    }

    public static class CustomGson<T> implements AsyncParser<T>, HeadersCallback {
        Gson gson;
        Type type;
        HeadersResponse mHeaders;
        final int MIN_ERROR_CODE=800;
        public CustomGson(Gson gson, Class < T > clazz) {
            this.gson = gson;
            type = clazz;
        }
        public CustomGson(Gson gson, TypeToken<T> token) {
            this.gson = gson;
            type=token.getType();
        }
        @Override
        public void onHeaders(HeadersResponse headers) {
            mHeaders=headers;
        }
        @Override
        public Future<T> parse(DataEmitter emitter) {

            if(LOG_RESPONSE){
                return new StringParser().parse(emitter).then(new TransformFuture<T, String>(){

                    @Override
                    protected void transform(String result) throws Exception {
                        Log.d("responseDump", result);
                        if(mHeaders!=null && mHeaders.code()>=MIN_ERROR_CODE){
                            ResponseStatus responseStatus = gson.fromJson(result, ResponseStatus.class);
                            throw new ResponseStatusException(mHeaders, responseStatus);
                        }
                        else {
                            T ret = gson.fromJson(result, getType());
                            mHeaders=null;
                            setComplete(ret);
                        }
                    }
                });
            }
            else return new ByteBufferListParser().parse(emitter)
                    .then(new TransformFuture<T, ByteBufferList>() {
                        @Override
                        protected void transform(ByteBufferList result) throws Exception {
                            ByteBufferListInputStream bin = new ByteBufferListInputStream(result);
                            if(mHeaders!=null && mHeaders.code()>=MIN_ERROR_CODE){
                                ResponseStatus responseStatus = gson.fromJson(new JsonReader(new InputStreamReader(bin)), ResponseStatus.class);
                                throw new ResponseStatusException(mHeaders, responseStatus);
                            }
                            T ret = (T)gson.fromJson(new JsonReader(new InputStreamReader(bin)), getType());
                            mHeaders=null;
                            setComplete(ret);
                        }
                    });
        }
        @Override
        public void write(DataSink sink, T pojo, CompletedCallback completed) {
            //write() used for caching only...
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(bout);
            gson.toJson(pojo, type, out);
            try {
                out.flush();
            }
            catch (final Exception e) {
                throw new AssertionError(e);
            }
            Util.writeAll(sink, bout.toByteArray(), completed);
        }

        @Override
        public Type getType() {
            return type;
        }


    }

    public class HelperRequest<TResponse>{
        private Builders.Any.B ionBuilder;
        private String url;
        private String method;
        private IReturn<TResponse> request;
        private Class<TResponse> responseClass;
        private WeakReference<IonHelperCallbacks> callback;
        HelperRequest(IReturn<TResponse> request, IonHelperCallbacks callback, String method, Builders.Any.B builder, String url){
            this.request=request;
            this.method=method;
            this.ionBuilder=builder;
            this.url=url;
            if(callback!=null)this.callback=new WeakReference<IonHelperCallbacks>(callback);
        }
        HelperRequest(IonHelperCallbacks callback, String method, Builders.Any.B builder, String url, Class<TResponse> responseClass){
            this.responseClass=responseClass;
            this.method=method;
            this.ionBuilder=builder;
            this.url=url;
            if(callback!=null)this.callback=new WeakReference<IonHelperCallbacks>(callback);
        }
        public Builders.Any.B getBuilder(){
            return ionBuilder;
        }

        public Future<Response<TResponse>> go(){
            IonHelperCallbacks cb = callback!=null?callback.get():null;
            if(cb!=null) cb.onIonRequestPreExecute(this);
            Future<Response<TResponse>> future;
            if(responseClass!=null) future=setTypeByClass(ionBuilder, responseClass);
            else future= setType(ionBuilder, request);

            if(cb!=null){
                final SimpleFuture<Response<TResponse>> futureChained = new SimpleFuture<>();
                 future.setCallback(new FutureCallback<Response<TResponse>>() {
                    @Override
                    public void onCompleted(Exception e, Response<TResponse> result) {
                        IonHelperCallbacks cb = callback.get();
                        if(cb!=null) cb.onIonRequestFinished(HelperRequest.this, result);
                        futureChained.setComplete(e, result);
                    }
                });
                cb.onIonRequestStarting(this);
                return futureChained;
            }
            else return future;
        }

        public String getUrl() {
            return url;
        }

        public String getHTTPMethod() {
            return method;
        }
        public boolean isPOST(){
            return "POST".equals(method);
        }
        public Class<TResponse> getResponseType(){
            if(request!=null && request.getResponseType() instanceof Class) return (Class<TResponse>) request.getResponseType();
            return responseClass;
        }
        public IReturn<TResponse> getRequest(){
            return request;
        }
    }
}
