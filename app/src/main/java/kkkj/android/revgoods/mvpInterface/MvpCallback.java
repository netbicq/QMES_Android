package kkkj.android.revgoods.mvpInterface;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.net.SocketTimeoutException;

import kkkj.android.revgoods.http.ResultException;
import retrofit2.HttpException;

import static kkkj.android.revgoods.mvpInterface.MvpModel.*;


/**
 * MVP callback接口
 *
 * @param <T>
 */
public abstract class MvpCallback<T> {
    private MvpView mvpView;

    protected MvpCallback(MvpView view) {
        this.mvpView = view;
    }

    /**
     * 数据请求成功
     *
     * @param data 请求到的数据
     */
    public abstract void onSuccess(T data);

    /**
     * 使用网络API接口请求方式时，虽然已经请求成功但是由
     * 于{@code msg}的原因无法正常返回数据。
     */
    public void onFailure(String msg) {
        if(mvpView!=null)
        {
            mvpView.showToast(msg);
        }
    }
//    public abstract void onFailure(String msg);

    /**
     * 请求数据失败，指在请求网络API接口请求方式时，出现无法联网、
     * 缺少权限，内存泄露等原因导致无法连接到请求数据源。
     */
    public void onError(Throwable throwable) {
        //获取最根源的异常
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        if (throwable instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
//                    onPermissionError(ex);          //权限错误，需要实现
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    throwable.printStackTrace();
                    mvpView.showErr("网络错误:" + httpException.code());
                    break;
            }
        } else if (throwable instanceof ResultException) {    //服务器返回的错误
            ResultException resultException = (ResultException) throwable;
            if (resultException.getMessage().equals(INVALID) || resultException.getMessage().equals(INVALID2)) {
                mvpView.showErr("token失效");
                mvpView.goLogin();
            } else {
                mvpView.showErr(resultException.getMessage() + "");
            }

        } else if (throwable instanceof SocketTimeoutException) {
            throwable.printStackTrace();
            mvpView.showErr("请求超时");
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            throwable.printStackTrace();
            mvpView.showErr("JSON解析失败:" + throwable.getMessage());
        } else {
            throwable.printStackTrace();
            if (throwable.getMessage().contains("No address")) {
                mvpView.showErr("请检查网络");
            } else {
                mvpView.showErr("未知错误:" + throwable.getMessage());
            }

        }
        onComplete();
    }
//    public abstract void onError(String msg);

    /**
     * 当请求数据结束时，无论请求结果是成功，失败或是抛出异常都会执行此方法给用户做处理，通常做网络
     * 请求时可以在此处隐藏“正在加载”的等待控件
     */
    public void onComplete() {
//        if(mvpView==null)
//        {
//            return;
//        }
        Logger.d(mvpView.getClass().getName());
        mvpView.onComplete();
    }
}
