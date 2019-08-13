package kkkj.android.revgoods.bean;

/**
 * 连接设备实体类
 */
public class Device {
    /**
     * type = 0;蓝牙电子秤
     * type = 1;蓝牙继电器
     * type = 2;wifi继电器
     * type = 3;蓝牙电子秤（采样连接的）
     * type = 0;蓝牙电子秤（手动计重连接的,手动计重只有一个电子秤）
     * type = 4;蓝牙Ble显示屏
     */
    private int type = -1;

    private String name;

    private String bluetoothName;

    private String wifiName;
    /**
     * 蓝牙设备Mac地址
     */
    private String bluetoothMac;
    /**
     * WiFi设备IP
     */
    private String wifiIp;
    /**
     * Wifi设备端口号
     */
    private int wifiPort;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    public String getWifiIp() {
        return wifiIp;
    }

    public void setWifiIp(String wifiIp) {
        this.wifiIp = wifiIp;
    }

    public int getWifiPort() {
        return wifiPort;
    }

    public void setWifiPort(int wifiPort) {
        this.wifiPort = wifiPort;
    }
}
