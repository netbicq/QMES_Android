package kkkj.android.revgoods.conn.socket;

/**
 * Name: KotlinDemo
 * Package Name：com.quickcq.kotlindemo
 * Author: Admin
 * Time: 2019/7/22 10:58
 * Describe: describe
 */
public abstract class CallBack {

    public abstract void onReceived(String msg);

    public void onConnectionSuccess() {

    }

    //连接断开
    public void onDisconnection(Exception e) {

    }

    //连接失败
    public void onConnectionFailed(Exception e) {

    }


}
