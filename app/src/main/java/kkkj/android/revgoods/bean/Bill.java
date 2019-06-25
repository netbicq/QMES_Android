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
     * 采样次数
     */
    private List<Integer> samplingCount;
    /**
     * 采样数量
     */
    private int  samplingNumber;
    /**
     *采样重量
     */
    private String samplingWeight;

    /**
     *采样详情
     */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
