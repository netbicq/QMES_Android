package kkkj.android.revgoods.conn;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import io.reactivex.Observer;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.relay.wifi.model.Order;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.conn
 * 创建者:   Bpldbt
 * 创建时间: 2019/8/31 22:00
 * 描述:    控制继电器的开关，进料口和出料口
 */
public class ControlRelay {

    private IConnectionManager manager;

    private boolean[] isTurn;
    //继电器控制的开关
    private int inLine;
    private int outLine;
    private int buzzerLine;
    /**
     * 继电器连接类型
     * 1-Wifi继电器
     * 2-蓝牙继电器
     */
    private int CONNECT_TYPE;

    private Observer<String> stateOB;
    //蓝牙继电器
    private BluetoothBean bluetoothRelay;

    public ControlRelay(IConnectionManager manager, boolean[] isTurn, int inLine, int outLine, int buzzerLine, int CONNECT_TYPE,
                        Observer<String> stateOB, BluetoothBean bluetoothRelay) {
        this.manager = manager;
        this.isTurn = isTurn;
        this.inLine = inLine;
        this.outLine = outLine;
        this.buzzerLine = buzzerLine;
        this.CONNECT_TYPE = CONNECT_TYPE;
        this.stateOB = stateOB;
        this.bluetoothRelay = bluetoothRelay;
    }

    public boolean[] getIsTurn() {
        return isTurn;
    }

    public void setIsTurn(boolean[] isTurn) {
        this.isTurn = isTurn;
    }

    //超过单重之后关闭入料口，传送带
    public void turnOffInLine() {
        switch (CONNECT_TYPE) {
            case 1:

                if (!isTurn[0] && manager != null && manager.isConnect()) {
                    isTurn[0] = true;
                    manager.send(new WriteData(Order.getTurnOff().get(inLine)));

                }
                break;

            case 2:

                if (!isTurn[0] && bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    isTurn[0] = true;
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(inLine)).subscribe(stateOB);

                }

                break;

            default:
                break;
        }

    }

    //单独控制开关时使用
    public void turnOffInLineWithOutIsTurn() {
        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOff().get(inLine)));

                }
                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(inLine)).subscribe(stateOB);

                }

                break;

            default:
                break;
        }

    }

    //计重之后打开2号开关，出料口开始倾倒物料
    public void turnOnOutLine() {

        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOn().get(outLine)));
                }

                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(outLine)).subscribe(stateOB);
                }

                break;

            default:
                break;
        }
    }

    //出料口倾倒完毕，此时关闭出料口
    public void turnOffOutLine() {
        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOff().get(outLine)));
                }

                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(outLine)).subscribe(stateOB);
                }

                break;

            default:
                break;
        }
    }

    //间隔1秒之后打开入料口
    public void turnOnInLine() {
        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOn().get(inLine)));
                }

                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                }

                break;

            default:
                break;
        }
    }

    //闭合灯光，蜂鸣器开关
    public void turnOnLightLine() {
        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOn().get(buzzerLine)));
                }

                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(buzzerLine)).subscribe(stateOB);
                }

                break;

            default:
                break;
        }
    }

    //断开灯光，蜂鸣器开关
    public void turnOffLightLine() {
        switch (CONNECT_TYPE) {
            case 1:

                if (manager != null && manager.isConnect()) {
                    manager.send(new WriteData(Order.getTurnOff().get(buzzerLine)));
                }

                break;

            case 2:

                if (bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(buzzerLine)).subscribe(stateOB);
                }

                break;

            default:
                break;
        }
    }


}
