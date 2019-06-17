package kkkj.android.revgoods.conn.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface ScanBlueCallBack {
    void onScanStarted();

    void onScanFinished();

    void onScanning(BluetoothDevice device);
}
