package kkkj.android.revgoods.ui.home;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierContract;
import kkkj.android.revgoods.ui.home.model.DeviceBean;
import kkkj.android.revgoods.ui.saveBill.BillModel;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 1:34
 * 描述:    TODO
 */
public class HomeContract {

    public interface View extends MvpView {

        void readScaleData(String data);

        void readBluetoothRelayData(List<RelayBean> relayBeanList);

        void deviceInfo(DeviceBean deviceBean);

    }

    public static abstract class Presenter extends MvpPresenter<HomeContract.View> {

        public abstract void connectScaleDevice(BluetoothDevice device);

        public abstract void connectBluetoothRelay(BluetoothDevice device,int inLine);

        public abstract void sendBluetoothRelayData(byte[] bytes);
    }

}
