package kkkj.android.revgoods.conn.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface PinBlueCallBack {
    void onBondRequest();

    void onBondFail(BluetoothDevice device);

    void onBonding(BluetoothDevice device);

    void onBondSuccess(BluetoothDevice device);
}
