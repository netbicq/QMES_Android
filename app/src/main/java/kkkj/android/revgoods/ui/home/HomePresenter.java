package kkkj.android.revgoods.ui.home;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import kkkj.android.revgoods.conn.ble.Ble;
import kkkj.android.revgoods.relay.bean.RelayBean;
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
            public void isConnected(boolean isConnected) {
                getView().isConnectedMainScale(isConnected);
            }

            @Override
            public void onConnectFail(String msg) {
                getView().onConnectedMainScaleFail(msg);
            }

            @Override
            public void onDisconnected() {
                getView().onDisconnectedMainScale();
            }

            @Override
            public void onRead(String data) {
                getView().readScaleData(data);
            }
        });
    }

    @Override
    public void connectBluetoothRelay(BluetoothDevice device,int inLine,int outLine) {

        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        homeModel.connectBlueToothRelay(device, inLine,outLine, new DeviceCallback<List<RelayBean>>(getView()) {


            @Override
            public void isConnected(boolean isConnected) {
                getView().isConnectedBluetoothRelay(isConnected);
            }

            @Override
            public void onConnectFail(String msg) {
                getView().onConnectedBluetoothRelayFail(msg);
            }

            @Override
            public void onDisconnected() {

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


    /**
     * 显示屏
     */
    @Override
    public void connectBleScreen(Ble ble) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        homeModel.connectBleScreen(ble, new DeviceCallback<String>(getView()) {
            @Override
            public void isConnected(boolean isConnected) {

            }

            @Override
            public void onConnectFail(String msg) {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onRead(String data) {

            }
        });
    }


}
