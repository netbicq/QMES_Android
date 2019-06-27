package kkkj.android.revgoods.event;

import kkkj.android.revgoods.bean.Device;

public class DeviceEvent {
    public String msg;
    public Device device;

    /**
     * 重置采样累计和累计计数
     */
    private boolean reset = false;

    /**
     * 采样累计（samplingNumber）
     */
    public int samplingNumber = -1;

    /**
     * 规格Specs Id
     */
    public int specsId = -1;

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
