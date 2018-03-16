package com.allen.audi.http;


import com.allen.audi.BuildConfig;
import com.allen.audi.util.PhoneInfoUtil;
import com.allen.audi.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @className: YmApiRequest
 * @classDescription: 网络请求
 * @author: YUHANG JO
 * @createTime: 15/6/18
 */
public class YmApiRequest {

    private Map<String, Object> apiMap = null;
    private Map<String, Converter.Factory> factorMap = null;

    private OkHttpClient client = null;

    private Retrofit mRetrofit;

    private static YmApiRequest instance;

    private YmApiRequest() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(interceptor);
        client = clientBuilder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://39.108.137.42:8080/audi/")
                .client(client)
                .build();
        apiMap = new HashMap<>();
        factorMap = new HashMap<>();
    }


    public static YmApiRequest getInstance() {
        if (instance == null) {
            instance = new YmApiRequest();
        }
        return instance;
    }

    // 添加user-agent
    private Interceptor interceptor = new Interceptor() {
        String userAgent = "audi/" + BuildConfig.VERSION_NAME
                + " (Linux; Android " + StringUtil.getValueEncoded(PhoneInfoUtil.getAndroidVersion())
                + "; " + StringUtil.getValueEncoded(android.os.Build.MODEL)
                + " Build/" + StringUtil.getValueEncoded(android.os.Build.ID) + ")";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("User-Agent", userAgent).build();
            return chain.proceed(request);
        }
    };

    public OkHttpClient getClient() {
        return client;
    }

    private void clearConverterFactory() {
        try {
            Class clx = mRetrofit.getClass();
            Field field = clx.getDeclaredField("converterFactories");
            field.setAccessible(true);
            //Retrofit 里面继续了list，封装成不可以修改的list
            List<Converter.Factory> factoryList = (List<Converter.Factory>) field.get(mRetrofit);
            //真身在父类里面
            Class listClx = factoryList.getClass();
            Class UnmodifiableList = listClx.getSuperclass();
            //获取真身
            Field listField = UnmodifiableList.getDeclaredField("list");
            listField.setAccessible(true);
            //这里才是真身
            factoryList = (List<Converter.Factory>) listField.get(factoryList);
            for (int index = 1; index < factoryList.size(); ) {
                factoryList.remove(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addConverterFactory(Converter.Factory factory) {
        Class clx = mRetrofit.getClass();
        try {
            Field field = clx.getDeclaredField("converterFactories");
            field.setAccessible(true);
            //Retrofit 里面继续了list，封装成不可以修改的list
            List<Converter.Factory> factoryList = (List<Converter.Factory>) field.get(mRetrofit);
            Class listClx = factoryList.getClass();
            Class UnmodifiableList = listClx.getSuperclass();
            Field listField = UnmodifiableList.getDeclaredField("list");
            listField.setAccessible(true);
            factoryList = (List<Converter.Factory>) listField.get(factoryList);
            factoryList.add(factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T create(Class<T> service, Class<? extends Converter.Factory> factory) {

        Object apiObject;
        if (!apiMap.containsKey(service.getName())) {
            apiObject = mRetrofit.create(service);
            apiMap.put(service.getName(), apiObject);
        } else {
            apiObject = apiMap.get(service.getName());
        }

        Converter.Factory factoryObject;
        clearConverterFactory();
        if (!factorMap.containsKey(factory.getName())) {
            try {
                Method method = factory.getMethod("create");
                factoryObject = (Converter.Factory) method.invoke(null);
                factorMap.put(factory.getName(), factoryObject);
                addConverterFactory(factoryObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            factoryObject = factorMap.get(factory.getName());
            addConverterFactory(factoryObject);
        }
        return (T) apiObject;
    }

    public <T> T create(Class<T> service) {
        return create(service, YmApiConverterFactory.class);
    }


}
