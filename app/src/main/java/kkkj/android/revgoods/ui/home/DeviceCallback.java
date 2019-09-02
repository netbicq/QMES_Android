package kkkj.android.revgoods.ui.home;

import kkkj.android.revgoods.mvpInterface.MvpView;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 1:12
 * 描述:    所有硬件设备相关的回调
 */
public abstract class DeviceCallback<T> {

    private MvpView mvpView;

    public DeviceCallback(MvpView view) {
        this.mvpView = view;
    }

    /**
     * 设备连接后的回调
     */
    public abstract void isConnected(boolean isConnected);

    /**
     * 连接失败的回调
     */
    public abstract void onConnectFail(String msg);

    /**
     * 连接状态改变的回调
     */
    public abstract void onDisconnected();

    /**
     *  读取设备数据
     * @param data
     */
    public abstract void onRead(T data);


}
