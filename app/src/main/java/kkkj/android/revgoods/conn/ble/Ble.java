package kkkj.android.revgoods.conn.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.conn.ble
 * Author: Admin
 * Time: 2019/8/5 13:51
 * Describe: describe
 */
public class Ble {

    final UUID UUID_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //
    //  设备特征值UUID, 需固件配合同时修改
    //
    final UUID UUID_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");  // 用于发送数据到设备
    final UUID UUID_NOTIFICATION = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"); // 用于接收设备推送的数据
    private String TAG = "haha";
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mDevice;
    private Context context;
    private boolean isServiceConnected;
    private BluetoothGattCallback mGattCallback;

    private boolean isConnected = false;

    public Ble(BluetoothDevice device, Context context) {
        mDevice = device;
        this.context = context;
        initBle();
    }

    private void initBle() {
        mGattCallback = new BluetoothGattCallback() {

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.d("haha", "onConnectionStateChange: " + newState);

                if (status != BluetoothGatt.GATT_SUCCESS) {
                    String err = "Cannot connect device with error status: " + status;

                    gatt.close();
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.disconnect();
                        mBluetoothGatt.close();
                        mBluetoothGatt = null;
                    }
                    if (mDevice != null) {
                        mBluetoothGatt = mDevice.connectGatt(context, false, mGattCallback);
                    }
                    Log.e(TAG, err);
                    return;
                }


                if (newState == BluetoothProfile.STATE_CONNECTED) {//当蓝牙设备已经连接

                    isConnected = true;
                    //获取ble设备上面的服务
//                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    Log.i("haha", "Attempting to start service discovery:" +

                            mBluetoothGatt.discoverServices());

                    Log.d("haha", "onConnectionStateChange: " + "连接成功");

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//当设备无法连接
                    isConnected = false;
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.disconnect();
                        mBluetoothGatt.close();
                        mBluetoothGatt = null;
                    }
                    gatt.close();
                    if (mDevice != null) {
                        mBluetoothGatt = mDevice.connectGatt(context, false, mGattCallback);
                    }
                }


            }

            //发现服务回调。
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d("haha", "onServicesDiscovered: " + "发现服务 : " + status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    isServiceConnected = true;

                    boolean serviceFound;
                    Log.d("haha", "onServicesDiscovered: " + "发现服务 : " + status);


                    Log.d(TAG, "onServicesDiscovered: " + "读取数据0");

                    if (mBluetoothGatt != null && isServiceConnected) {

                        BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
                        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_NOTIFICATION);
                        boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                        if (b) {

                            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                            for (BluetoothGattDescriptor descriptor : descriptors) {

                                boolean b1 = descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                if (b1) {
                                    mBluetoothGatt.writeDescriptor(descriptor);
                                    Log.d(TAG, "startRead: " + "监听收数据");
                                }

                            }

                        }
                    }

                    serviceFound = true;

                }

            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.d(TAG, "read value: " + characteristic.getValue());
                Log.d(TAG, "callback characteristic read status " + status
                        + " in thread " + Thread.currentThread());
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "read value: " + characteristic.getValue());

                }


            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.d(TAG, "onDescriptorWrite: " + "设置成功");
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);

            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicWrite: " + "发送成功");

                boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                mBluetoothGatt.readCharacteristic(characteristic);
            }

            @Override
            public final void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                byte[] value = characteristic.getValue();
                Log.d(TAG, "onCharacteristicChanged: " + value);
                String s0 = Integer.toHexString(value[0] & 0xFF);
                String s = Integer.toHexString(value[1] & 0xFF);
                Log.d(TAG, "onCharacteristicChanged: " + s0 + "、" + s);
//            textView1.setText("收到: " + s0 + "、" + s);
                for (byte b : value) {
                    Log.d(TAG, "onCharacteristicChanged: " + b);
                }

            }

        };

    }



    public boolean connect() {
       // BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("D8:E0:4B:FD:31:F6");
        if (mDevice != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
            mBluetoothGatt = mDevice.connectGatt(context, false, mGattCallback);
        }
        return isConnected;
    }

    public void send(byte[] bytes) {
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);
//            byte[] bytes = new byte[2];
//            bytes[0] = 04;
//            bytes[1] = 01;
            characteristic.setValue(bytes);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            mBluetoothGatt.writeCharacteristic(characteristic);
        }

    }

    public void destory() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
    }

}
