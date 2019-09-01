package kkkj.android.revgoods.ui.home;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.ui.home.model.DeviceBean;
import kkkj.android.revgoods.ui.home.model.HomeModel;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.ui.home
 * 创建者:   Bpldbt
 * 创建时间: 2019/9/1 1:42
 * 描述:    TODO
 */
public class HomePresenter extends HomeContract.Presenter{

    private HomeModel homeModel;

    public HomePresenter() {
        homeModel = new HomeModel();
    }

    @Override
    public void connectScaleDevice(BluetoothDevice device) {

        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        homeModel.connectScale(device, new DeviceCallback<String>(getView()) {
            @Override
            public void deciceInfo(DeviceBean deviceBean) {
                getView().deviceInfo(deviceBean);
            }

            @Override
            public void onRead(String data) {
                getView().readScaleData(data);
            }
        });
    }

    @Override
    public void connectBluetoothRelay(BluetoothDevice device,int inLine) {

        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        homeModel.connectBlueToothRelay(device, inLine, new DeviceCallback<List<RelayBean>>(getView()) {
            @Override
            public void deciceInfo(DeviceBean deviceBean) {
                getView().deviceInfo(deviceBean);
            }

            @Override
            public void onRead(List<RelayBean> data) {
                getView().readBluetoothRelayData(data);
            }
        });
    }

    @Override
    public void sendBluetoothRelayData(byte[] bytes) {
        homeModel.sendBluetoothRelayData(bytes);
    }
}
