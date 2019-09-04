package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 13:33
 * Describe: 生产线配置
 */
public class ProduceLine extends LitePalSupport implements Serializable {

    /**
     * KeyID : a01dd4fb-126b-4001-b46a-fb916916b66d
     * Name : sample string 2
     * Master : sample string 3    收料秤（主秤）
     * Power : sample string 4     继电器
     * Sapmle : sample string 5    采样秤
     * ShowOut : sample string 6   显示器
     */

    private int id;

    private String KeyID;
    private String Name;
    private String Master;
    private String Power;
    private String Sapmle;
    private String ShowOut;

    //是否禁用收料秤
    private boolean isMasterBaned;
    //是否禁用继电器
    private boolean isPowerBaned;
    //是否禁用采样秤
    private boolean isSapmleBaned;
    //是否禁用显示器
    private boolean isShowOutBaned;

    //蓝牙主电子秤的连接状态
    private boolean masterConnectionState = false;
    //继电器的连接状态
    private boolean relayConnectionState = false;
    //蓝牙采样电子秤的连接状态
    private boolean samplingConnectionState = false;
    //蓝牙Ble显示屏的连接状态
    private boolean showOutConnectionState = false;

    public boolean isMasterConnectionState() {
        return masterConnectionState;
    }

    public void setMasterConnectionState(boolean masterConnectionState) {
        this.masterConnectionState = masterConnectionState;
    }

    public boolean isRelayConnectionState() {
        return relayConnectionState;
    }

    public void setRelayConnectionState(boolean relayConnectionState) {
        this.relayConnectionState = relayConnectionState;
    }

    public boolean isSamplingConnectionState() {
        return samplingConnectionState;
    }

    public void setSamplingConnectionState(boolean samplingConnectionState) {
        this.samplingConnectionState = samplingConnectionState;
    }

    public boolean isShowOutConnectionState() {
        return showOutConnectionState;
    }

    public void setShowOutConnectionState(boolean showOutConnectionState) {
        this.showOutConnectionState = showOutConnectionState;
    }

    public boolean isMasterBaned() {
        return isMasterBaned;
    }

    public void setMasterBaned(boolean masterBaned) {
        isMasterBaned = masterBaned;
    }

    public boolean isPowerBaned() {
        return isPowerBaned;
    }

    public void setPowerBaned(boolean powerBaned) {
        isPowerBaned = powerBaned;
    }

    public boolean isSapmleBaned() {
        return isSapmleBaned;
    }

    public void setSapmleBaned(boolean sapmleBaned) {
        isSapmleBaned = sapmleBaned;
    }

    public boolean isShowOutBaned() {
        return isShowOutBaned;
    }

    public void setShowOutBaned(boolean showOutBaned) {
        isShowOutBaned = showOutBaned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String KeyID) {
        this.KeyID = KeyID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMaster() {
        return Master;
    }

    public void setMaster(String Master) {
        this.Master = Master;
    }

    public String getPower() {
        return Power;
    }

    public void setPower(String Power) {
        this.Power = Power;
    }

    public String getSapmle() {
        return Sapmle;
    }

    public void setSapmle(String Sapmle) {
        this.Sapmle = Sapmle;
    }

    public String getShowOut() {
        return ShowOut;
    }

    public void setShowOut(String ShowOut) {
        this.ShowOut = ShowOut;
    }

    @Override
    public String toString() {
        return "ProduceLine{" +
                "id=" + id +
                ", KeyID='" + KeyID + '\'' +
                ", Name='" + Name + '\'' +
                ", Master='" + Master + '\'' +
                ", Power='" + Power + '\'' +
                ", Sapmle='" + Sapmle + '\'' +
                ", ShowOut='" + ShowOut + '\'' +
                ", isMasterBaned=" + isMasterBaned +
                ", isPowerBaned=" + isPowerBaned +
                ", isSapmleBaned=" + isSapmleBaned +
                ", isShowOutBaned=" + isShowOutBaned +
                '}';
    }
}
