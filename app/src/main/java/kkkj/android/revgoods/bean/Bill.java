package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据
 */
public class Bill extends LitePalSupport {

    private int id;
    /**
     * 当前时间
     */
    String time;
    /**
     * 单据名称
     */
    private String name;

    /**
     * 是否已上传
     * 默认-1未上传
     */
    private int  isUpload = -1;
    /**
     * 采购人
     */
    private String purchaser;
    /**
     * 供应商
     */
    private Supplier supplier;
    /**
     *品类
     */
    private Matter matter;
    /**
     * 规格
     */
    private Specs specs;
    /**
     * 累计明细
     */
    private List<Cumulative> cumulativeList = new ArrayList<>();

    /**
     *采样详情
     */
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Matter getMatter() {
        return matter;
    }

    public void setMatter(Matter matter) {
        this.matter = matter;
    }

    public Specs getSpecs() {
        return specs;
    }

    public void setSpecs(Specs specs) {
        this.specs = specs;
    }

    public List<Cumulative> getCumulativeList() {
        return cumulativeList;
    }

    public void setCumulativeList(List<Cumulative> cumulativeList) {
        this.cumulativeList = cumulativeList;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
