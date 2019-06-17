package kkkj.android.revgoods.conn.socket;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SocketListener implements ISocketActionListener {
    Observer<Message> observer;

    public SocketListener(Observer<Message> observer) {
        this.observer = observer;
    }

    @Override
    public void onSocketIOThreadStart(String action) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
    }

    @Override
    public void onSocketIOThreadShutdown(String action, Exception e) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                if(e!=null)
                {
                    message.setData(e.getMessage());
                }
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                message.setData(BytesUtils.toHexStringForLog(data.getBodyBytes()));
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                message.setData(BytesUtils.toHexStringForLog(data.parse()));
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onPulseSend(ConnectionInfo info, IPulseSendable data) {

    }

    @Override
    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                if(e!=null)
                {
                    message.setData(e.getMessage());
                }

                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                Message message = new Message();
                message.setAction(action);
                emitter.onNext(message);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
