package kkkj.android.revgoods.bean;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 17:05
 * Describe: 采样秤
 */
public class Sapmle {

    /**
     * DeviceType : 1:蓝牙；2:继电器
     * DeviceAddr : 192.168.123.105:10001
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
        return "Sapmle{" +
                "DeviceType=" + DeviceType +
                ", DeviceAddr='" + DeviceAddr + '\'' +
                '}';
    }
}
