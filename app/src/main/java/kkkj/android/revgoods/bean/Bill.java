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
     * 单据名称
     */
    private String name;
    /**
     * 采购人
     */
    private String purchaser;
    /**
     * 供应商
     */
    private String supplier;
    /**
     *品类
     */
    private String matter;
    /**
     * 规格
     */
    private String specs;
    /**
     * 累计明细
     */
    private List<Cumulative> cumulativeList = new ArrayList<>();

    /**
     *采样详情
     */
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
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
