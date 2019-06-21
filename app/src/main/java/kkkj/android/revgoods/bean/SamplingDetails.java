package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

import kkkj.android.revgoods.common.getpic.GetPicModel;

public class SamplingDetails extends LitePalSupport {
    private int id;
    private int count;
    private String number;
    private String weight;
    private List<GetPicModel> modelList;

    public List<GetPicModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<GetPicModel> modelList) {
        this.modelList = modelList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
