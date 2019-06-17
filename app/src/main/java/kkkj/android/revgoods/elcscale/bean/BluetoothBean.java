package kkkj.android.revgoods.elcscale.bean;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.InvocationTargetException;

import kkkj.android.revgoods.conn.bluetooth.BluetoothManager;

public class BluetoothBean {
    boolean changeFlag = false;
    float weight=0.00f;
    boolean isListen = false;
    BluetoothDevice bluetoothDevice;
    BluetoothManager myBluetoothManager;

    public boolean isChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(boolean changeFlag) {
        this.changeFlag = changeFlag;
    }

    public BluetoothManager getMyBluetoothManager() {
        if(myBluetoothManager==null)
        {
            try {
                myBluetoothManager = new BluetoothManager(bluetoothDevice);
                return myBluetoothManager;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return myBluetoothManager;
        }
    }
    public boolean isListen() {
        return isListen;
    }

    public void setListen(boolean listen) {
        isListen = listen;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isConnect() {
        return getMyBluetoothManager().isConnect();
    }


    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
