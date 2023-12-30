package com.example.earthquakemobile.service;

import android.content.Context;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.ICronetEngineBuilder;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Request {

    private final String EARTHQUAKE_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2023-01-01&endtime=2023-12-31&latitude=41.8719&longitude=12.5674&maxradius=5";
    private final String API_URL = "http://www.bitesrl.it/clienti/univaq/corso/distributori.json";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private CronetEngine engine;
    private static volatile Request instance = null;

    public static synchronized Request getInstance(Context context){
        if(instance == null){
            synchronized (Request.class){
                if(instance == null)
                    instance = new Request(context);
            }
        }
        return instance;
    }
    private Request(Context context){
        engine = new CronetEngine.Builder(context)
                .enableHttp2(true)
                .enableQuic(true)
                .setStoragePath(context.getCacheDir().getAbsolutePath())
                .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISK, 10*1024*2024)
                .enableBrotli(true)
                .build();
    }

    public void requestDownload(Request.RequestCallback callback){
        try{
            engine.newUrlRequestBuilder(EARTHQUAKE_API_URL,callback,executor).build().start();
        }catch(Exception e){
            System.out.println("Gotcha");
            e.printStackTrace();
        }
    }

    public abstract static class RequestCallback extends UrlRequest.Callback{
        private final int BUFFER_SIZE = 1024*1024;
        private final ByteArrayOutputStream received = new ByteArrayOutputStream();
        private final WritableByteChannel channel = Channels.newChannel(received);
        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
            request.followRedirect();
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            onCompleted(request,info,null,error);
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
            if(info.getHttpStatusCode() == 200){
                request.read(ByteBuffer.allocateDirect(BUFFER_SIZE));
            }
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
            byteBuffer.flip();
            try{
                channel.write(byteBuffer);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            byteBuffer.clear();
            request.read(byteBuffer);
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            byte[] data = received.toByteArray();
            onCompleted(request,info,data,null);
        }

        public abstract void onCompleted(UrlRequest request, UrlResponseInfo info, byte[] data, CronetException error);
    }
}
