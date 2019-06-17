package kkkj.android.revgoods.mvpInterface;


import kkkj.android.revgoods.http.RetrofitServiceManager;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.http.api.APIApp;
import kkkj.android.revgoods.http.api.APIAttachfile;
import kkkj.android.revgoods.http.api.APIAuth;

/**
 * MVP Model基类
 * @param <T>
 */
public class MvpModel<T extends RevGRequest,E extends RevGResponse> {
    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    public static int RESPONSE_OK = 200;
    public static int UNLOGIN = 2;
    public static String INVALID = "非法请求";
    public static String INVALID2 = "登录超时";
    public static String INVALID3 = "token失效";

    public static APIAuth apiAuth= RetrofitServiceManager.getInstance().create(APIAuth.class);
    public static APIAttachfile apiAttachfile=RetrofitServiceManager.getInstance().create(APIAttachfile.class);
    public static APIApp apiApp=RetrofitServiceManager.getInstance().create(APIApp.class);

    public void getResponse(T t,final MvpCallback<E> callback){}

    public E getResponseSync(T t){
        return null;
    }
}
