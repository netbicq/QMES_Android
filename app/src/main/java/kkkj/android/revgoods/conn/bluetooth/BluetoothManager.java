package kkkj.android.revgoods.conn.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.core.utils.BytesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.conn.socket.SocketListener;

public class BluetoothManager {
    BluetoothDevice mDevice;
    BluetoothSocket mSocket;

    public BluetoothManager(BluetoothDevice mDevice) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.mDevice = mDevice;
//        mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        this.mSocket = (BluetoothSocket) mDevice.getClass().
                getDeclaredMethod("createRfcommSocket", new Class[]{int.class}).invoke(mDevice, 1);

    }

    /**
     * 配对（配对成功与失败通过广播返回）
     */
    public void pin() {
        if (mDevice == null) {
            Logger.e("bond device null");
            return;
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
            Logger.d("attemp to bond:" + mDevice.getName());
            try {
                Method createBondMethod = mDevice.getClass().getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(mDevice);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Logger.e("attemp to bond fail!");
            }
        }
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     */
    public void cancelPinBule() {
        if (mDevice == null) {
            Logger.d("cancel bond device null");
            return;
        }

        //判断设备是否配对，没有配对就不用取消了
        if (mDevice.getBondState() != BluetoothDevice.BOND_NONE) {
            Logger.d("attemp to cancel bond:" + mDevice.getName());
            try {
                Method removeBondMethod = mDevice.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(mDevice);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Logger.e("attemp to cancel bond fail!");
            }
        }
    }

    /**
     * 获取 连接 观察者
     *
     * @return
     */
    public Observable<Boolean> getConnectOB() {
        Observable<Boolean> connectOb = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if (mSocket == null) {
//                    mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                    //另外，借鉴网上方法和建议，在获得socket的时候 ，尽量不使用uuid方式；因为这样虽然能够获取到socket 但是不能进行自动，
                    //所以使用的前提是已经配对了的设备连接；
                    //使用反射的方式，能够自动提示配对，也适合手机间通信。
                    mSocket = (BluetoothSocket) mDevice.getClass().
                            getDeclaredMethod("createRfcommSocket", new Class[]{int.class}).invoke(mDevice, 1);
                }

                if (isConnect()) {
                    emitter.onNext(true);
                } else {
                    mSocket.connect();
                    emitter.onNext(true);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return connectOb;
    }

    /**
     * 蓝牙是否连接
     *
     * @return
     */
    public boolean isConnect() {
        return mSocket != null && mSocket.isConnected();
    }

    /**
     * 断开连接
     *
     * @return
     */
    public boolean disConnect() {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        mSocket = null;
        return true;
    }

    /**
     * 获取 读取 观察者
     * 蓝牙电子秤
     * @return
     */
    public Observable<String> getReadOB() {
        Observable<String> readOb = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                InputStream inputStream = null;
                int num = 0;
                byte[] buffer = new byte[8];
                byte[] buffer_new = new byte[8];
                int i = 0;
                int n = 0;
                String smsg = "";
                inputStream = mSocket.getInputStream();
                while (true) {
                    smsg = "";
                    num = inputStream.read(buffer);         //读入数据
                    n = 0;
                    for (i = 0; i < num; i++) {
                        if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                            buffer_new[n] = 0x0a;
                            i++;
                        } else {
                            buffer_new[n] = buffer[i];
                        }
                        n++;
                    }
                    String s = new String(buffer_new, 0, n);
//                    smsg += s;   //写入接收缓存
                    smsg = s;   //写入接收缓存
                    if (inputStream.available() == 0)
                        break;  //短时间没有数据才跳出进行显示
                }
                //发送显示消息，进行显示刷新
                emitter.onNext(new StringBuilder(smsg).reverse().toString());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return readOb;
    }



    /**
     * 获取 读取 观察者
     * 蓝牙继电器
     * @return
     */
    public Observable<byte[]> getReadOBModbus() {
        Observable<byte[]> readOb = Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                InputStream inputStream = null;
                int num = 0;
                byte[] buffer = new byte[8];
                byte[] buffer_new = new byte[8];
                int i = 0;
                int n = 0;
                String smsg = "";
                inputStream = mSocket.getInputStream();
                while (true) {
                    num = inputStream.read(buffer);         //读入数据
                    n = 0;
                    for (i = 0; i < num; i++) {
                        if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                            buffer_new[n] = 0x0a;
                            i++;
                        } else {
                            buffer_new[n] = buffer[i];
                        }
                        n++;
                    }
                    String s = new String(buffer_new, 0, n);
//                    smsg += s;   //写入接收缓存
                    smsg = s;   //写入接收缓存
                    if (inputStream.available() == 0)
                        break;  //短时间没有数据才跳出进行显示
                }
                //发送显示消息，进行显示刷新
                emitter.onNext(buffer_new);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return readOb;
    }

    /**
     * 获取 写入 观察者
     *
     * @return
     */
    public Observable<String> getWriteOB(byte[] write) {
        Observable<String> writeOb = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                byte[] bos_first = {0x02, 0x41, 0x42, 0x30, 0x33, 0x03};
                Logger.d(BytesUtils.toHexStringForLog(write));
                OutputStream outputStream = null;
                outputStream = mSocket.getOutputStream();
                outputStream.write(write);
                emitter.onNext("发送成功");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return writeOb;
    }


    public Observable<String> getReadObservable() {
        Observable<String> readObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                InputStream inputStream = null;
                int num = 0;
                byte[] buffer = new byte[1024];
                String smsg = "";
                inputStream = mSocket.getInputStream();
                while (num != -1) {
                    num = inputStream.read(buffer);         //读入数据
                    if (num != -1) {
                        smsg = new String(buffer, 0, num);
                    }
                    //if (inputStream.available() == 0)
                       // break;  //短时间没有数据才跳出进行显示
                }
                //发送显示消息
                emitter.onNext(smsg);
                emitter.onComplete();
            }
        }).repeat().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return readObservable;
    }
}
