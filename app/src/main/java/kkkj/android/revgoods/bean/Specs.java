package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

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
     * KeyID : 11d51e9a-0f88-4d6a-8793-859ed513bf4a
     * Name : sample string 2
     * MinWeight : 3.0
     * MaxWeight : 4.0
     */

    /**
     * 采样单位
     * 1. 系统默认单位kg
     * 2. g
     */

    private String KeyID;
    private String Name;
    private double MinWeight;
    private double MaxWeight;

    private String value;

    public String getValue() {

        int unit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);

        if (unit == 1) {
            return MinWeight + "kg~" + MaxWeight + "kg";
        }else {
            return DoubleCountUtils.keep(MinWeight * 1000) + "g~" + DoubleCountUtils.keep(MaxWeight * 1000) + "g";
        }

    }

    public void setValue(String value) {
        this.value = value;
    }

    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();

    public List<SamplingDetails> getSamplingDetailsList() {
        return samplingDetailsList;
    }

    public void setSamplingDetailsList(List<SamplingDetails> samplingDetailsList) {
        this.samplingDetailsList = samplingDetailsList;
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

    public double getMinWeight() {
        return MinWeight;
    }

    public void setMinWeight(double MinWeight) {
        this.MinWeight = MinWeight;
    }

    public double getMaxWeight() {
        return MaxWeight;
    }

    public void setMaxWeight(double MaxWeight) {
        this.MaxWeight = MaxWeight;
    }

    @Override
    public String toString() {
        return "Specs{" +
                "id=" + id +
                ", KeyID='" + KeyID + '\'' +
                ", Name='" + Name + '\'' +
                ", MinWeight=" + MinWeight +
                ", MaxWeight=" + MaxWeight +
                '}';
    }
}
