package kkkj.android.revgoods.bean;

public class MyEvent {
    public String msg;
    public Device device;

    public MyEvent(String msg) {
        this.msg = msg;
    }

    public MyEvent(Device device) {
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
