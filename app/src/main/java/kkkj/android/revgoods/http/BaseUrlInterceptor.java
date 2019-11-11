package kkkj.android.revgoods.http;

import java.io.IOException;
import java.util.List;

import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: bpldbt
 * Date: 2019/11/11
 * Describe: 动态修改BaseUrl
 */
public class BaseUrlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取request
        Request request = chain.request();
        // 从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url();
        // 获取request的创建者builder
        Request.Builder builder = request.newBuilder();

        String baseUrl = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_BASE_URL);

        HttpUrl newBaseUrl = HttpUrl.parse(baseUrl);
        // 重建新的HttpUrl，修改需要修改的url部分
        HttpUrl newFullUrl = oldHttpUrl
                .newBuilder()
                // 更换网络协议
                .scheme(newBaseUrl.scheme())
                // 更换主机名
                .host(newBaseUrl.host())
                // 更换端口
                .port(newBaseUrl.port())
                .build();
        // 重建这个request，通过builder.url(newFullUrl).build()；
        // 然后返回一个response至此结束修改
        return chain.proceed(builder.url(newFullUrl).build());

    }
}
