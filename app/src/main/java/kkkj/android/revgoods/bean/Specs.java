package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.bean
 * 创建者:   Bpldbt
 * 创建时间: 2019/6/24 15:20
 * 描述:    规格
 */
public class Specs extends LitePalSupport {

    private int id;

    /**
     * 规格
     */
    private String Name;

    private String KeyID;

    /**
     * 价格
     */
    private double price = -1d;

    /**
     *关联的采样详情
     */
    private SamplingDetails samplingDetails;

    public SamplingDetails getSamplingDetails() {
        return samplingDetails;
    }

    public void setSamplingDetails(SamplingDetails samplingDetails) {
        this.samplingDetails = samplingDetails;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Specs{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", KeyID='" + KeyID + '\'' +
                '}';
    }
}
