package com.allen.audi.http;

import android.text.TextUtils;

import com.allen.audi.util.SystemUtil;
import com.allen.audi.util.TransData;
import com.allen.audi.util.TransUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @className: YmApiConverterFactory
 * @classDescription: This converter decode the response.
 * @author: swallow
 * @createTime: 2015/6/11
 */
public class YmApiConverterFactory extends Converter.Factory {
    private static final String TAG = "YmApiConverterFactory";

    private final Gson gson;

    public YmApiConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    public static YmApiConverterFactory create() {
        return create(new Gson());
    }

    public static YmApiConverterFactory create(Gson gson) {
        return new YmApiConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new YmApiResponseBodyConverter<>(gson, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new YmApiRequestBodyConverter<>(gson, adapter);
    }

    final class YmApiResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final Type type;

        YmApiResponseBodyConverter(Gson gson, Type type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            BaseResponse baseResponse;
            try {

                // 解析Json数据返回的TransData对象
                TransData transData = TransUtil.getResponse(value.string());

                try {
                    if (transData.getCode().equals("400") ||
                            TextUtils.isEmpty(transData.getResult())) {
                        baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
                        baseResponse.setResponseCode(("200".equals(transData.getCode())));
                        baseResponse.setResponseMessage(transData.getMessage());
                    } else {
                        baseResponse = gson.fromJson(transData.getResult(), type);
                        baseResponse.setResponseCode(("200").equals(transData.getCode()));
                        baseResponse.setResponseMessage(transData.getMessage());
                        baseResponse.setResponseResult(transData.getResult());
                    }
                    return (T) baseResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
                    baseResponse.setResponseCode(("200").equals(transData.getCode()));
                    baseResponse.setResponseMessage(transData.getMessage());
                    baseResponse.setResponseResult(transData.getResult());
                    return (T) baseResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 从不返回一个空的Response
            baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
            try {
                baseResponse.setResponseCode(false);
                baseResponse.setResponseMessage("");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return (T) baseResponse;
            }
        }
    }

    final class YmApiRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        YmApiRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

//    @Override
//    public Object fromBody(TypedInput body, Type type) throws ConversionException {
//        BaseResponse baseResponse;
//        try {
//            String reString = IOUtil.readIt(body.in(), HTTP.UTF_8);
//            body.in().close();
//            // TEAg解密返回的数据
//            reString = Tea.decryptByBase64Tea(reString);
//            if (!AppConstant.IS_RELEASE)
//                Log.d(TAG, reString);
//            // 解析Json数据返回TransData对象
//            TransData transData = TransUtil.getResponse(reString);
//            // 如果退出登录则直接返回失败，不进行进一步解析。
//            if (YMHttpRequest.exitLogin(AppManager.getInstance().currentActivity(),false,
//                    transData.getResult())) {
//                baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
//                baseResponse.setResponseCode(false);
//                //Your account has been kicked!
//                baseResponse.setResponseMessage("");
//                return baseResponse;
//            }
//            try {
//                if (transData.getCode().equals("400") ||
//                        TextUtils.isEmpty(transData.getResult())) {
//                    baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
//                    baseResponse.setResponseCode(("200".equals(transData.getCode())));
//                    baseResponse.setResponseMessage(transData.getMessage());
//                } else {
//                    baseResponse = new Gson().fromJson(transData.getResult(), type);
//                    baseResponse.setResponseCode(("200".equals(transData.getCode())));
//                    baseResponse.setResponseMessage(transData.getMessage());
//                    baseResponse.setResponseResult(transData.getResult());
//                }
//                return baseResponse;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //从不返回一个空的Response.
//        baseResponse = (BaseResponse) SystemUtil.getObject(((Class) type).getName());
//        try {
//            baseResponse.setResponseCode(false);
//            //YmApiConverterFactory can not recognize the response!
//            baseResponse.setResponseMessage("");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            return baseResponse;
//        }
//    }
//
//    @Override
//    public TypedOutput toBody(Object object) {
//        return null;
//    }
}