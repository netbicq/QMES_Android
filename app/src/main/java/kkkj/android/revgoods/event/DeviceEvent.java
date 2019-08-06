package kkkj.android.revgoods.event;

import kkkj.android.revgoods.bean.Device;

/**
 * MainActivity  EventBus
 */
public class DeviceEvent {
    public String msg;
    public Device device;

    /**
     * 单据删除后更新显示
     */
    public boolean resetUploadCount = false;

    /**
     * 重置采样累计和累计计数
     * 更新单据：已上传/未上传
     */
    private boolean reset = false;

    /**
     * 采样累计（samplingNumber）
     */
    public int samplingNumber = -1;

    /**
     * 供应商Supplier Id
     */
    public String supplierId = "";

    /**
     * 品类Matter Id
     */
    public int matterId = -1;

    /**
     * 规格Specs Id
     */
    public int specsId = -1;

    /**
     *扣重数 + 1
     */
    public boolean add = false;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public int getMatterId() {
        return matterId;
    }

    public void setMatterId(int matterId) {
        this.matterId = matterId;
    }

    public boolean isResetUploadCount() {
        return resetUploadCount;
    }

    public void setResetUploadCount(boolean resetUploadCount) {
        this.resetUploadCount = resetUploadCount;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public int getSpecsId() {
        return specsId;
    }

    public void setSpecsId(int specsId) {
        this.specsId = specsId;
    }

    public DeviceEvent(){

    }
    public int getSamplingNumber() {
        return samplingNumber;
    }

    public void setSamplingNumber(int samplingNumber) {
        this.samplingNumber = samplingNumber;
    }

    public DeviceEvent(String msg) {
        this.msg = msg;
    }

    public DeviceEvent(Device device) {
        this.device = device;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
