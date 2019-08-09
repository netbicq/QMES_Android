package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.common.getpic.GetPicModel;


public class SamplingDetails extends LitePalSupport {
    private int id;

    private int hasBill = -1;

    /**
     * 采样次数
     */
    private int count;
    /**
     * 单次数量
     */
    private String number;
    /**
     * 单次重量
     */
    private String weight;
    /**
     * 单次规格
     */
    private Specs specs;

    /**
     * 规格占比
     */
    private double specsProportion;

    /**
     * 单价
     */
    private double price = -1d;

    /**
     *单重
     */
    private double SingalWeight;

    /**
     *品类等级
     */
    private MatterLevel matterLevel;

    private List<GetPicModel> modelList = new ArrayList<GetPicModel>();

    private List<Path> pathList = new ArrayList<>();

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }

    public MatterLevel getMatterLevel() {
        return matterLevel;
    }

    public void setMatterLevel(MatterLevel matterLevel) {
        this.matterLevel = matterLevel;
    }

    public double getSingalWeight() {
        return SingalWeight;
    }

    public void setSingalWeight(double singalWeight) {
        SingalWeight = singalWeight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Bill getmBill() {
        return mBill;
    }

    public void setmBill(Bill mBill) {
        this.mBill = mBill;
    }



    private Bill mBill;

    public double getSpecsProportion() {
        return specsProportion;
    }

    public void setSpecsProportion(double specsProportion) {
        this.specsProportion = specsProportion;
    }

    public int getHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }

    public Bill getBill() {
        return mBill;
    }

    public void setBill(Bill bill) {
        mBill = bill;
    }


    public Specs getSpecs() {
        return specs;
    }

    public void setSpecs(Specs specs) {
        this.specs = specs;
    }

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
