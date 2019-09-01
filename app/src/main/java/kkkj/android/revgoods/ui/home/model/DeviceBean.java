package kkkj.android.revgoods.ui.home.model;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home.model
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 10:44
 * 描述:    TODO
 */
public class DeviceBean {

    /**
     * type = 0;蓝牙电子秤
     * type = 1;蓝牙继电器
     * type = 2;wifi继电器
     * type = 3;蓝牙电子秤（采样连接的）
     * type = 0;蓝牙电子秤（手动计重连接的,手动计重只有一个电子秤）
     * type = 4;蓝牙Ble显示屏
     */
    private int type = -1;

    /**
     * 是否已连接
     */
    private boolean isConnected;

    /**
     * 连接状态改变
     */
    private boolean connectionChanged;

    /**
     * 连接失败Msg
     */
    private String failMsg;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnectionChanged() {
        return connectionChanged;
    }

    public void setConnectionChanged(boolean connectionChanged) {
        this.connectionChanged = connectionChanged;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

}
