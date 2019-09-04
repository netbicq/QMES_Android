package kkkj.android.revgoods.conn.ble;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.conn.ble
 * Author: Admin
 * Time: 2019/9/4 16:25
 * Describe: describe
 */
public interface BleCallback {

    void onConnected(boolean isConnected);

    void disConnected();
}
