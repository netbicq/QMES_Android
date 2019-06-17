package kkkj.android.revgoods.relay.wifi.view;

import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.conn.socket.PulseData;

public class SocketTestListener implements ISocketActionListener {

    TextView tv_content;
    StringBuffer sb;
    private IConnectionManager manager;
    private PulseData mPulseData = new PulseData();
    public SocketTestListener(TextView tv_content, IConnectionManager manager) {
        this.tv_content = tv_content;
        this.manager = manager;
        sb = new StringBuffer();
    }

    @Override
    public void onSocketIOThreadStart(String s) {
        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSocketIOThreadShutdown(String s, Exception e) {
        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSocketReadResponse(ConnectionInfo connectionInfo, String s, OriginalData originalData) {
        //00 00 00 00 00 04 00 02 01 00  心跳返回
        if(manager != null && BytesUtils.toHexStringForLog(originalData.getBodyBytes()).indexOf("00 02 01 00")>=0)
        {
            manager.getPulseManager().feed();
        }
        Logger.d("收到:"+ BytesUtils.toHexStringForLog(originalData.getHeadBytes()));
        Logger.d("收到:"+ BytesUtils.toHexStringForLog(originalData.getBodyBytes()));
//        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSocketWriteResponse(ConnectionInfo connectionInfo, String s, ISendable iSendable) {
        Logger.d(s);
        Logger.d("发送:"+ BytesUtils.toHexStringForLog(iSendable.parse()));
        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onPulseSend(ConnectionInfo connectionInfo, IPulseSendable iPulseSendable) {
        Logger.d("心跳："+connectionInfo.getPort());
    }

    @Override
    public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e) {
        Logger.d(s+"---------close");
        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s) {
        sb.append(s + "\n");
        manager.getPulseManager()
        .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e) {
        Logger.d(e.getMessage());
        sb.append(s + "\n");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发送显示消息，进行显示刷新
                emitter.onNext(sb.toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_content.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
