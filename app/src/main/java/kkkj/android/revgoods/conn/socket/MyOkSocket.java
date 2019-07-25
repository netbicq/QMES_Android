package kkkj.android.revgoods.conn.socket;

import android.annotation.SuppressLint;
import android.util.Log;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Name: KotlinDemo
 * Package Name：com.quickcq.kotlindemo.okSocket
 * Author: Admin
 * Time: 2019/7/24 14:10
 * Describe: describe
 */
public class MyOkSocket {
    private CallBack callBack;
    private IConnectionManager manager;
    private PulseData mPulseData = new PulseData();
    private ISocketActionListener iSocketActionListener;

    private String TAG = "OkSocket";

    public MyOkSocket(String ip, int port, CallBack callBack) {
        this.callBack = callBack;

        initSocket(ip,port);
    }

    public IConnectionManager getManager() {
        return manager;
    }

    public ISocketActionListener getiSocketActionListener() {
        return iSocketActionListener;
    }

    private void initSocket(String ip, int port) {
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        ConnectionInfo info = new ConnectionInfo(ip,port);
        //调用OkSocket,开启这次连接的通道,拿到通道Manager

        manager = OkSocket.open(info);

        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setPulseFrequency(10000)//心跳包间隔时间，单位毫秒
                .setReaderProtocol(new ModbusProtocol());//自定义解析头

        //将新的修改后的参配设置给连接管理器
        manager.option(okOptionsBuilder.build());
        //绑定监听
        iSocketActionListener = new ISocketActionListener() {
            @Override
            public void onSocketIOThreadStart(String s) {

            }

            @Override
            public void onSocketIOThreadShutdown(String s, Exception e) {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onSocketReadResponse(ConnectionInfo connectionInfo, String s, OriginalData originalData) {
                final String text = BytesUtils.toHexStringForLog(originalData.getBodyBytes());
                if (manager != null && text.trim().equals("00 02 01 00")) { //如果是心跳返回包
                    manager.getPulseManager().feed();//喂狗
                } else {
                    //切换到主线程
                    Observable.just("")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s1 -> callBack.onReceived(text));
                }
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo connectionInfo, String s, ISendable iSendable) {

            }

            @Override
            public void onPulseSend(ConnectionInfo connectionInfo, IPulseSendable iPulseSendable) {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, final Exception e) {
                Log.d(TAG,"连接断开" + e.getMessage());
                //切换到主线程
                Observable.just("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s12 -> callBack.onDisconnection(e));

            }

            @SuppressLint("CheckResult")
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s) {
                manager.getPulseManager()
                        .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                        .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
                Log.d(TAG,"连接成功");

                Observable.just("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s13 -> callBack.onConnectionSuccess());
            }

            @SuppressLint("CheckResult")
            @Override
            public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, final Exception e) {
                Log.d(TAG,"连接失败" + e.getMessage());

                Observable.just("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s14 -> callBack.onConnectionFailed(e));
            }
        };
        manager.registerReceiver(iSocketActionListener);
    }


}
