package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.common.getpic.GetPicModel;


public class SamplingDetails extends LitePalSupport {
    private int id;
    /**
     * 是否已保存为单据 默认-1；未保存
     */
    private int hasBill = -1;

    /**
     * 采样次数,序号
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
     * 单次规格ID
     */
    private int specsId;

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
     *品类等级ID
     */
    private int matterLevelId;

    /**
     * 供应商ID
     */
    private int supplierId;

    /**
     * 品类ID
     */
    private int matterId;

    /**
     * 所属单据
     */
    private Bill bill;

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getMatterId() {
        return matterId;
    }

    public void setMatterId(int matterId) {
        this.matterId = matterId;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    private List<GetPicModel> modelList = new ArrayList<GetPicModel>();

    private List<Path> pathList = new ArrayList<>();

    public int getSpecsId() {
        return specsId;
    }

    public void setSpecsId(int specsId) {
        this.specsId = specsId;
    }

    public int getMatterLevelId() {
        return matterLevelId;
    }

    public void setMatterLevelId(int matterLevelId) {
        this.matterLevelId = matterLevelId;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
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
