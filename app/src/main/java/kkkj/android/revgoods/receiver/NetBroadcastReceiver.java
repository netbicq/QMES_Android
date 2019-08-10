package kkkj.android.revgoods.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import kkkj.android.revgoods.utils.NetUtils;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.receiver
 * 创建者:   Bpldbt
 * 创建时间: 2019/8/10 12:34
 * 描述:    TODO
 */
public class NetBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                Logger.d("WIFI已关闭！");
            }

            if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                Logger.d("WIFI已开启");

                if (NetUtils.checkNetWork()) {

                    Logger.d("WIFI连接可用");

                }else {
                    Logger.d("WIFI连接不可用");
                }
            }
        }

    }


}
