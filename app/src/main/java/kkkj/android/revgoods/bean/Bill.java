package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;
import kkkj.android.revgoods.bean.bill.PurPrices;

/**
 * 单据
 */
public class Bill extends LitePalSupport {

    /**
     * UUID
     */
    private String UUID;

    private int id;
    /**
     * 计称总重量
     */
    private double weight;
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
     * 扣重率
     */
    private int deductionMix;

    /**
     * 供应商ID
     */
    private int supplierId;
    /**
     *品类ID
     */
    private int matterId;

    /**
     * 计价方式
     */

    private int samplingBySpecsId = -1;

    /**
     * 累计明细
     */
    private List<Cumulative> cumulativeList = new ArrayList<>();

    /**
     *采样详情
     */
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();

    /**
     * 扣重明细
     */
    private List<Deduction> deductionList = new ArrayList<>();


    private List<BillDetails> billDetailsList = new ArrayList<>();


    public List<BillDetails> getBillDetailsList() {
        return billDetailsList;
    }

    public void setBillDetailsList(List<BillDetails> billDetailsList) {
        this.billDetailsList = billDetailsList;
    }

    public int getSamplingBySpecsId() {
        return samplingBySpecsId;
    }

    public void setSamplingBySpecsId(int samplingBySpecsId) {
        this.samplingBySpecsId = samplingBySpecsId;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

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

    public List<Deduction> getDeductionList() {
        return deductionList;
    }

    public void setDeductionList(List<Deduction> deductionList) {
        this.deductionList = deductionList;
    }


    public int getDeductionMix() {
        return deductionMix;
    }

    public void setDeductionMix(int deductionMix) {
        this.deductionMix = deductionMix;
    }

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
