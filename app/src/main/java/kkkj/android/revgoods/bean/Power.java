package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 16:13
 * Describe: 传送带开关
 */
public class Power{

    /**
     * InLine : 1
     * OutLine : 2
     * DeviceType : 1:蓝牙；2:继电器
     * DeviceAddr : 192.168.123.105:10001
     */

    private int InLine;
    private int OutLine;
    private int DeviceType;
    private String DeviceAddr;

    public int getInLine() {
        return InLine;
    }

    public void setInLine(int InLine) {
        this.InLine = InLine;
    }

    public int getOutLine() {
        return OutLine;
    }

    public void setOutLine(int OutLine) {
        this.OutLine = OutLine;
    }

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
        return "Power{" +
                ", InLine=" + InLine +
                ", OutLine=" + OutLine +
                ", DeviceType=" + DeviceType +
                ", DeviceAddr='" + DeviceAddr + '\'' +
                '}';
    }
}
