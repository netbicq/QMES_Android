package kkkj.android.revgoods.ui.home;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import kkkj.android.revgoods.conn.ble.Ble;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.relay.bean.RelayBean;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 1:34
 * 描述:    TODO
 */
public class HomeContract {

    public interface View extends MvpView {

        /**
         *主秤，收料秤
         */
        void readScaleData(String data);
        void isConnectedMainScale(boolean isConnected);
        void onConnectedMainScaleFail(String msg);
        void onDisconnectedMainScale();

        /**
         *蓝牙继电器
         */
        void readBluetoothRelayData(List<RelayBean> relayBeanList);
        void isConnectedBluetoothRelay(boolean isConnected);
        void onConnectedBluetoothRelayFail(String msg);


    }

    public static abstract class Presenter extends MvpPresenter<HomeContract.View> {

        public abstract void connectScaleDevice(BluetoothDevice device);

        public abstract void connectBluetoothRelay(BluetoothDevice device,int inLine,int outLine);

        public abstract void connectBleScreen(Ble ble);

        public abstract void sendBluetoothRelayData(byte[] bytes);
    }

}
