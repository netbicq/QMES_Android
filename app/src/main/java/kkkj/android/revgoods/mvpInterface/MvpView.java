package kkkj.android.revgoods.mvpInterface;

import android.content.Context;

/**
 * MVP VIEW 基本接口
 */
public interface MvpView {
    /**
     * 显示正在加载view
     */
    void showLoading();
    /**
     * 关闭正在加载view
     */
    void hideLoading();
    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
    /**
     * 显示请求错误提示
     */
    void showErr(String msg);
    /**
     * 获取上下文
     * @return 上下文
     */
    void goLogin();
    Context getContext();
    void onComplete();
}
