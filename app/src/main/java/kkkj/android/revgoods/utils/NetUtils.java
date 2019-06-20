package kkkj.android.revgoods.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import kkkj.android.revgoods.app.BaseApplication;


public class NetUtils {
    private static boolean isNetPingUsable()
    {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping -c 3 www.baidu.com");
            int ret = process.waitFor();
            if (ret == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkNetWork(){
        try{
            ConnectivityManager connectactivity = (ConnectivityManager) BaseApplication.getInstance().getAppContext().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectactivity != null){
                //6.0以上判断是否网络真正可用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        NetworkCapabilities networkCapabilities = connectactivity.getNetworkCapabilities(connectactivity.getActiveNetwork());
                        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                    }
                    else {
                        return isNetPingUsable();
                    }
                }else {
                    return isNetPingUsable();
                }
            }
            else {
                return false ;
            }
        }
        catch (Exception e) {
            return false ;
        }
    }
}
