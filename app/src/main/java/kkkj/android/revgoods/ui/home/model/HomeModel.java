package kkkj.android.revgoods.ui.home.model;

import android.bluetooth.BluetoothDevice;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.core.utils.BytesUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.SwitchIcon;
import kkkj.android.revgoods.conn.classicbt.BleManager;
import kkkj.android.revgoods.conn.classicbt.Connect;
import kkkj.android.revgoods.conn.classicbt.listener.ConnectResultlistner;
import kkkj.android.revgoods.conn.classicbt.listener.TransferProgressListener;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.ui.home.DeviceCallback;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home.model
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 1:05
 * 描述:    电子秤
 */
public class HomeModel {

    /**
     * type = 0;蓝牙电子秤
     * type = 1;蓝牙继电器
     * type = 2;wifi继电器
     * type = 3;蓝牙电子秤（采样连接的）
     * type = 0;蓝牙电子秤（手动计重连接的,手动计重只有一个电子秤）
     * type = 4;蓝牙Ble显示屏
     */
    private BluetoothBean bluetoothRelay;
    private Observer<String> stateOB;

    public void connectScale(BluetoothDevice device, final DeviceCallback<String> callback) {

        DeviceBean deviceBean = new DeviceBean();
        deviceBean.setType(Constant.MAIN_SCALE);

        BleManager.getInstance().connect(device, new ConnectResultlistner() {

            @Override
            public void connectSuccess(Connect connect) {

                deviceBean.setConnected(true);
                callback.deciceInfo(deviceBean);


                connect.read(new TransferProgressListener() {
                    @Override
                    public void transfering(int progress) {

                    }

                    @Override
                    public void transferSuccess(String msg) {
                        if (msg.length() == 8) {
                            //取反
                            msg = new StringBuilder(msg).reverse().toString();

                            if (!msg.endsWith("=") && !msg.startsWith("=") && msg.substring(5, 6).equals(".")) {
                                //去掉前面的“=”号和零
                                msg = msg.replaceFirst("^0*", "");
                                if (msg.startsWith(".")) {
                                    msg = "0" + msg;
                                }

                                callback.onRead(msg);
                            }
                        }

                    }

                    @Override
                    public void transferFailed(Exception exception) {

                    }
                });
            }

            @Override
            public void connectFailed(Exception e) {

                deviceBean.setFailMsg(e.getMessage());
                callback.deciceInfo(deviceBean);
            }

            @Override
            public void disconnected() {

                deviceBean.setConnectionChanged(false);
                callback.deciceInfo(deviceBean);
            }


        });

    }

    public void connectBlueToothRelay(BluetoothDevice device, int inLine,final DeviceCallback<List<RelayBean>> callback) {

        DeviceBean deviceBean = new DeviceBean();
        deviceBean.setType(Constant.BLUETOOTH_RELAY);

        BluetoothBean bluetoothBean = new BluetoothBean();
        bluetoothBean.setBluetoothDevice(device);

        bluetoothRelay = bluetoothBean;

        List<RelayBean> relayBeanList = new ArrayList<>();
        //初始化wifi继电器实体类
        List<Integer> leftIcon = new ArrayList<>(SwitchIcon.getRedIcon());
        List<Integer> rightIcon = new ArrayList<>(SwitchIcon.getGreenIcon());

        for (int i = 0; i < leftIcon.size(); i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setLeftIamgeView(leftIcon.get(i));
            relayBean.setRightImageView(rightIcon.get(i));
            relayBeanList.add(relayBean);

        }

        stateOB = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

                bluetoothBean.getMyBluetoothManager().getReadOBModbus().subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {

                        Logger.d("----" + BytesUtils.toHexStringForLog(bytes) + "----");
                        String message = BytesUtils.toHexStringForLog(bytes);

                        if (message.indexOf("01 05 00 ") == 0) {
                            //收到状态
                            String index = message.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            String state = message.substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---" + index + "---" + state + "---");

                            //第几号开关
                            int whichSwitch = Integer.parseInt(index.substring(1, 2));

                            if (state.equals("00")) {
                                relayBeanList.get(whichSwitch).setState("0");
                            } else {
                                relayBeanList.get(whichSwitch).setState("1");
                            }

                            callback.onRead(relayBeanList);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        bluetoothBean.getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    deviceBean.setConnected(true);
                    callback.deciceInfo(deviceBean);
                    //打开某个开关
                    bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                deviceBean.setFailMsg(e.getMessage());
                callback.deciceInfo(deviceBean);
            }

            @Override
            public void onComplete() {
                //bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(stateOB);
                //manager.send(new WriteData(Order.TURN_ON_1));
            }
        });
    }

    public void sendBluetoothRelayData(byte[] bytes) {
        bluetoothRelay.getMyBluetoothManager().getWriteOB(bytes).subscribe(stateOB);
    }

}
