package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 13:33
 * Describe: 生产线配置
 */
public class ProduceLine extends LitePalSupport {

    /**
     * KeyID : a01dd4fb-126b-4001-b46a-fb916916b66d
     * Name : sample string 2
     * Master : sample string 3    收料秤（主秤）
     * Power : sample string 4     传送带继电器
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
                '}';
    }
}
