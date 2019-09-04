package kkkj.android.revgoods.conn.ble;

import android.content.Context;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.conn.ble
 * Author: Admin
 * Time: 2019/9/4 16:13
 * Describe: describe
 */
public class BleTest {

    final UUID UUID_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //
    //  设备特征值UUID, 需固件配合同时修改
    //
    final UUID UUID_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");  // 用于发送数据到设备
    final UUID UUID_NOTIFICATION = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"); // 用于接收设备推送的数据

    private String macAddress;
    private Context context;
    private BluetoothClient mClient;
    private BleConnectOptions options;
    private BleCallback callback;

    public BleTest(Context context,String macAddress,BleCallback callback) {
        this.context = context;
        this.macAddress = macAddress;
        this.callback = callback;

        init();

    }

    private void init() {
         options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();

        mClient = new BluetoothClient(context);

        mClient.registerConnectStatusListener(macAddress, new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {
                if (status == STATUS_CONNECTED) {
                    callback.onConnected(true);
                } else if (status == STATUS_DISCONNECTED) {
                    callback.disConnected();
                }

            }
        });

    }

    public void connectBle() {

        mClient.connect(macAddress, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    callback.onConnected(true);
                }else if (code == REQUEST_FAILED) {
                    callback.onConnected(false);
                }
            }
        });
    }

    public void sendData(byte[] bytes) {

        mClient.write(macAddress, UUID_SERVICE, UUID_WRITE, bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {

                }
            }
        });

    }

    public void diconnect() {
        if (mClient != null) {
            mClient.refreshCache(macAddress);
            mClient.disconnect(macAddress);
        }


    }

}
