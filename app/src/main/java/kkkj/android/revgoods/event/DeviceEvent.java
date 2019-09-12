package kkkj.android.revgoods.event;

import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.ProduceLine;

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
     * 供应商Supplier Id
     */
    public int supplierId = -1;

    /**
     * 品类Matter Id
     */
    public int matterId = -1;

    /**
     * 品类等级MatterLevel Id
     */
    public int matterLevelId = -1;


    /**
     * 规格Specs Id
     */
    public int specsId = -1;

    /**
     *扣重数 + 1
     */
    public boolean add = false;

    /**
     * 生产线
     */
    private ProduceLine produceLine;

    public ProduceLine getProduceLine() {
        return produceLine;
    }

    /**
     * 间隔时间
     */
    private int intervalTime = -1;

    /**
     *生产线配置后刷新
     */
    private boolean isRefresh = false;

    public boolean isRefresh() {
        return isRefresh;
    }

    /**
     * 断开设备连接
     * disConnect
     * 1.收料秤
     * 2.继电器
     * 3.采样秤
     * 4.显示屏
     */
    private int disConnectType = -1;

    /**
     *连接设备
     */
    private int connectType = -1;

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }

    public int getDisConnectType() {
        return disConnectType;
    }

    public void setDisConnectType(int disConnectType) {
        this.disConnectType = disConnectType;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setProduceLine(ProduceLine produceLine) {
        this.produceLine = produceLine;
    }

    public int getMatterLevelId() {
        return matterLevelId;
    }

    public void setMatterLevelId(int matterLevelId) {
        this.matterLevelId = matterLevelId;
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
