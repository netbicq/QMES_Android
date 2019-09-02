package kkkj.android.revgoods.ui.home.model;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.core.utils.BytesUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.SwitchIcon;
import kkkj.android.revgoods.conn.ControlRelay;
import kkkj.android.revgoods.conn.ble.Ble;
import kkkj.android.revgoods.conn.classicbt.BleManager;
import kkkj.android.revgoods.conn.classicbt.Connect;
import kkkj.android.revgoods.conn.classicbt.listener.ConnectResultlistner;
import kkkj.android.revgoods.conn.classicbt.listener.TransferProgressListener;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.ui.home.DeviceCallback;
import kkkj.android.revgoods.utils.CRC16Util;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

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
    //蓝牙Ble显示屏的连接状态
    private boolean bleScreenConnectionState = false;
    private Ble ble;


    //继电器控制的开关
    private int inLine;
    private int outLine;

    /**
     * 收料秤
     */
    public void connectScale(BluetoothDevice device, final DeviceCallback<String> callback) {

        final boolean[] isSend = {false};
        //单重
        final double[] compareWeight = {Double.parseDouble(SharedPreferenceUtil
                .getString(SharedPreferenceUtil.SP_PIECE_WEIGHT))};

        List<String> strWeightList = new ArrayList<>();
        List<String> strLowWeightList = new ArrayList<>();

        BleManager.getInstance().connect(device, new ConnectResultlistner() {

            final boolean[] isWrite = {false};
            final boolean[] isTurn = {false};

//            ControlRelay controlRelay = new ControlRelay(manager, isTurn, inLine, outLine, CONNECT_TYPE, stateOB,
//                    bluetoothRelay);

            @Override
            public void connectSuccess(Connect connect) {

                callback.isConnected(true);

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

                                byte[] weightByte = CRC16Util.stringToByte(msg);

                                if (ble != null) {
                                    bleScreenConnectionState = ble.isConnected();
                                    if (!isSend[0] && bleScreenConnectionState) {

                                        //myToasty.showSuccess("显示屏连接成功！");

                                        //小数位数，固定2位
//                                        byte[] point = {0x01, 0x06, 0x00, 0x04, 0x00, 0x02, 0x49, (byte) 0xCA};
//                                        ble.send(point);
                                        isSend[0] = true;

                                    }
//                                    ble.send(weightByte);//ModBus方式

                                    /**   $001,2.91#   显示2.91
                                     *    $001,01&     显示YES
                                     *    ASCII码  b1,b3,b4首尾固定格式
                                     *    b2  数据
                                     */

                                    byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
                                    byte[] b3 = {0x23};
                                    byte[] b4 = {0x30, 0x31, 0x26};
                                    byte[] b2 = msg.getBytes();
                                    b2 = CRC16Util.addBytes(b1, b2);
                                    b3 = CRC16Util.addBytes(b2, b3);
                                    b4 = CRC16Util.addBytes(b1, b4);
                                    ble.send(b3);
                                }

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

                callback.onConnectFail(e.getMessage());
            }

            @Override
            public void disconnected() {

                callback.onDisconnected();
            }


        });

    }

    /**
     * 蓝牙继电器
     * @param device
     * @param inLine
     * @param callback
     */
    public void connectBlueToothRelay(BluetoothDevice device, int inLine,int outLine,final DeviceCallback<List<RelayBean>> callback) {

        this.inLine = inLine;
        this.outLine = outLine;
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
                callback.isConnected(aBoolean);
                if (aBoolean) {
                    //打开某个开关
                    bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                callback.onConnectFail(e.getMessage());
            }

            @Override
            public void onComplete() {
                //bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(stateOB);
                //manager.send(new WriteData(Order.TURN_ON_1));
            }
        });
    }

    /**
     * 蓝牙继电器发送数据
     * @param bytes
     */
    public void sendBluetoothRelayData(byte[] bytes) {
        bluetoothRelay.getMyBluetoothManager().getWriteOB(bytes).subscribe(stateOB);
    }

    /**
     * Ble显示屏
     */
    public void connectBleScreen(Ble ble,final DeviceCallback<String> callback) {
        if (bleScreenConnectionState) {
            return;
        }
        this.ble = ble;
        bleScreenConnectionState = ble.connect();

    }

}
