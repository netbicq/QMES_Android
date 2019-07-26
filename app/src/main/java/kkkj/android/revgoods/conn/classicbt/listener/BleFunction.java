package kkkj.android.revgoods.conn.classicbt.listener;

import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import java.util.Set;

import kkkj.android.revgoods.conn.classicbt.scan.ScanConfig;

/**
 * @author AllenLiu
 * @date 2019/5/8
 */
public interface BleFunction {

    void init(Context context);
    void setConnectionUUID(String uuid);

    boolean isSupported();

    void setResultListener(ScanResultListener resultListener);

    Set<BluetoothDevice> getPairedDevices();

    void scan(ScanConfig scanConfig, ScanResultListener scanResultListener);

    void stopSearch();

    void connect(BluetoothDevice device, ConnectResultlistner connectResultlistner);
    void destory();
    void pin(BluetoothDevice device, PinResultListener resultListener);
    void cancelPin(BluetoothDevice device, ResultListener resultListener);
    void setServerConnectResultListener(ConnectResultlistner connectResultListener);
    void registerServerConnection(ConnectResultlistner connectResultListener);

    void setForegroundService(boolean foregroundService);
    void setNotification(Notification notification);
    void enableDiscoverable(long time);


}
