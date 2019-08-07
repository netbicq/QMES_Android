package kkkj.android.revgoods.bean;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 17:08
 * Describe: 显示器
 */
public class ShowOut {

    /**
     * DeviceType : 1:蓝牙；2:继电器
     * DeviceAddr : 20:17:03:15:05:65
     */

    private int DeviceType;
    private String DeviceAddr;

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int DeviceType) {
        this.DeviceType = DeviceType;
    }

    public String getDeviceAddr() {
        return DeviceAddr;
    }

    public void setDeviceAddr(String DeviceAddr) {
        this.DeviceAddr = DeviceAddr;
    }

    @Override
    public String toString() {
        return "ShowOut{" +
                "DeviceType=" + DeviceType +
                ", DeviceAddr='" + DeviceAddr + '\'' +
                '}';
    }
}
