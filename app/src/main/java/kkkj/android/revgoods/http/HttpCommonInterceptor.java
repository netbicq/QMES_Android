package kkkj.android.revgoods.http;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import kkkj.android.revgoods.app.RevGoods;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

class HttpCommonInterceptor implements Interceptor {
    private Map<String,String> mHeaderParamsMap = new HashMap<>();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public HttpCommonInterceptor() {
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
//        Log.d("HttpCommonInterceptor","add common params");
        Request oldRequest = chain.request();
        // 添加新的参数，添加到url 中
        /*HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder()
        .scheme(oldRequest.url().scheme())
        .host(oldRequest.url().host());*/

        // 新的请求
        Request.Builder requestBuilder =  oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());

        //添加公共参数,添加到header中
        mHeaderParamsMap.put("Token",  RevGoods.getInstance().getCommonParts().get("Token")+"");
        mHeaderParamsMap.put("AccountID",  RevGoods.getInstance().getCommonParts().get("AccountID")+"");
        if(mHeaderParamsMap.size() > 0){
            for(Map.Entry<String,String> params:mHeaderParamsMap.entrySet()){
                requestBuilder.header(params.getKey(),params.getValue());
            }
        }
        Request newRequest = requestBuilder.build();
        RequestBody requestBody = newRequest.body();
        boolean hasRequestBody = requestBody != null;
        Logger.d(newRequest.method()+"\nUrl:"+newRequest.url()+"\nheader:"+newRequest.headers());
        if (!hasRequestBody) {
//            Logger.d(hasRequestBody);
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                Logger.d(newRequest.method()+"\nUrl:"+newRequest.url()+"\nParams:"+buffer.readString(charset)+"\nheader:"+newRequest.headers());
            } else {
//                Logger.d(newRequest.method()+"\nUrl:"+newRequest.url()+"\nParams:"+buffer.readString(charset)+"\nheader:"+newRequest.headers());
            }
        }
        Response response;
        try {
            response = chain.proceed(newRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("Response"+"\nUrl:"+newRequest.url()+"\nResult:"+e.getMessage());
            throw e;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        if (contentLength != 0) {
            Logger.d("Response"+"\nUrl:"+newRequest.url()+"\nResult:"+buffer.clone().readString(charset));
        }
        if (!isPlaintext(buffer)) {
            return response;
        }
        return response;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
    public static class Builder{
        HttpCommonInterceptor mHttpCommonInterceptor;
        public Builder(){
            mHttpCommonInterceptor = new HttpCommonInterceptor();
        }

        public Builder addHeaderParams(String key, String value){
            if(!TextUtils.isEmpty(value))
            {
                mHttpCommonInterceptor.mHeaderParamsMap.put(key,value);
            }
            return this;
        }

        public Builder  addHeaderParams(String key, int value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, float value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, long value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, double value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public HttpCommonInterceptor build(){
            return mHttpCommonInterceptor;
        }
    }
}
