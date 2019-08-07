package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/7 9:18
 * Describe: 扣重类型和品类等级
 */
public class Dict extends LitePalSupport {
    /**
     * DictType : 1
     * DictName : sample string 2
     * DictValue : sample string 3
     * CreateMan : sample string 4
     * CreateDate : 2019-08-07 09:10:17
     * State : 6
     * ID : 2f72df54-b4b6-4cd8-a849-28bfdebe12c9
     */

    /**
     * DictType = 2:扣重类型
     * DictType = 3:品类等级
     */

    private int id;

    private int DictType;
    private String DictName;
    private String DictValue;
    private String CreateMan;
    private String CreateDate;
    private int State;
    private String ID;

    /**
     * 扣重的重量
     */
    private double weight = -1d;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDictType() {
        return DictType;
    }

    public void setDictType(int DictType) {
        this.DictType = DictType;
    }

    public String getDictName() {
        return DictName;
    }

    public void setDictName(String DictName) {
        this.DictName = DictName;
    }

    public String getDictValue() {
        return DictValue;
    }

    public void setDictValue(String DictValue) {
        this.DictValue = DictValue;
    }

    public String getCreateMan() {
        return CreateMan;
    }

    public void setCreateMan(String CreateMan) {
        this.CreateMan = CreateMan;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", DictType=" + DictType +
                ", DictName='" + DictName + '\'' +
                ", DictValue='" + DictValue + '\'' +
                ", CreateMan='" + CreateMan + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                ", State=" + State +
                ", ID='" + ID + '\'' +
                '}';
    }
}
