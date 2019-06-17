package kkkj.android.revgoods.conn.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public interface ConnectBlueCallBack {
    void onStartConnect();

    void onConnectSuccess(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket);

    void onConnectFail(BluetoothDevice bluetoothDevice, String 连接失败);
}
