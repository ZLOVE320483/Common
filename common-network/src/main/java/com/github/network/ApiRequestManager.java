package com.github.network;

import android.text.TextUtils;

import com.github.network.app.BaseApplication;
import com.github.network.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zlove on 2018/1/19.
 */

public class ApiRequestManager {

    public static final int READ_TIME_OUT = 20 * 1000;
    public static final int CONNECT_TIME_OUT = 20 * 1000;

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    private static final int CACHE_SIZE = 1024 * 1024 * 100;

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private static ApiRequestManager instance;

    private ApiRequestManager(String baseUrl) {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getLogInterceptor())
                .addInterceptor(getHeaderInterceptor())
                .addInterceptor(getRewriteCacheControlInterceptor())
                .addNetworkInterceptor(getRewriteCacheControlInterceptor())
                .cache(getCache())
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static ApiRequestManager getInstance(String baseUrl) {
        if (instance == null) {
            synchronized (ApiRequestManager.class) {
                if (instance == null) {
                    instance = new ApiRequestManager(baseUrl);
                }
            }
        }
        return instance;
    }

    public <T> T getService(Class<T> service) {
        return retrofit.create(service);
    }


    private Interceptor getLogInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logInterceptor;
    }

    private Interceptor getHeaderInterceptor() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = addHeaderParam(chain.request().newBuilder());
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    private Interceptor getRewriteCacheControlInterceptor() {
        Interceptor reWriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();
                if (!NetworkUtils.isNetworkAvailable(BaseApplication.getAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetworkUtils.isNetworkAvailable(BaseApplication.getAppContext())) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };
        return reWriteCacheControlInterceptor;
    }

    private Request addHeaderParam(Request.Builder builder) {
        HashMap<String, String> headerParam = new HashMap<>();
        for (Map.Entry<String, String> entry : headerParam.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    private Cache getCache() {
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        return cache;
    }
}
