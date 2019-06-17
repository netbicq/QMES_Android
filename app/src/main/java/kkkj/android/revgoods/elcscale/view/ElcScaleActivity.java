package kkkj.android.revgoods.elcscale.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.conn.bluetooth.Bluetooth;
import kkkj.android.revgoods.conn.bluetooth.PinBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.PinBlueReceiver;
import kkkj.android.revgoods.conn.bluetooth.ScanBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.ScanBlueReceiver;
import kkkj.android.revgoods.elcscale.adapter.ElcScaleManagerAdapter;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;

/**
 * 蓝牙电子秤
 */

public class ElcScaleActivity extends MvpBaseActivity {
    private int REQUEST_ENABLE_BT = 101;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ScanBlueReceiver scanBlueReceiver;
    PinBlueReceiver pinBlueReceiver;
    List<BluetoothBean> mList;
    ElcScaleManagerAdapter adapter;
    Bluetooth mBluetooth;

    @Override
    protected int getLayout() {
        return R.layout.activity_elc_scale;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        action_bar_title.setText("附近蓝牙");
        mBluetooth = new Bluetooth();
        mList = new ArrayList<>();
        adapter = new ElcScaleManagerAdapter(R.layout.item_elcscale, mList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mBluetooth.cancelScanBule();
                switch (view.getId()) {
                    case R.id.connect:
                        Logger.d("是否连接"+mList.get(position).isConnect());
                        if (mList.get(position).isConnect())//是否连接
                        {
                            if(mList.get(position).isListen())
                            {
                                mList.get(position).setListen(false);
                            }
                            mList.get(position).getMyBluetoothManager().disConnect();
                            adapter.notifyItemChanged(position);
                        } else {
                            if (mList.get(position).getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                                mList.get(position).getMyBluetoothManager().pin();
                            } else {
                                connect(position);
                            }
                        }

                        break;
                    case R.id.listen:
                        if(mList.get(position).isListen())
                        {
                            mList.get(position).setListen(false);
                        }
                        else {
                            mList.get(position).setListen(true);
                        }
                        adapter.notifyItemChanged(position);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新前断开所有连接和停止所有监听
                if(mList.size()>0)
                {
                    for(int i = 0 ;i<mList.size();i++)
                    {
                        if (mList.get(i).isConnect())//是否连接
                        {
                            mList.get(i).getMyBluetoothManager().disConnect();
                        }
                        mList.get(i).setListen(false);
                    }
                }

                if (!mBluetooth.scanBlue()) {
                    smartRefreshLayout.finishRefresh();
                    mBluetooth.openBlueSync(mActivity, REQUEST_ENABLE_BT);
                }
                else
                {
                    mList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if (!mBluetooth.isSupportBlue()) {
            showToast("当前设备不支持蓝牙");
            return;
        }
        rigistReciver();
    }

    private void rigistReciver() {
        scanBlueReceiver = new ScanBlueReceiver(new ScanBlueCallBack() {
            @Override
            public void onScanStarted() {
            }

            @Override
            public void onScanFinished() {
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onScanning(BluetoothDevice device) {
                Logger.d(device.getName());
                if(!TextUtils.isEmpty(device.getName()))
                {
                    if(device.getName().equals("SF1308"))
                    {
                        BluetoothBean bluetoothBean = new BluetoothBean();
                        bluetoothBean.setBluetoothDevice(device);
                        Logger.d("deviceAddress:" + device.getAddress());

                        bluetoothBean.setWeight(0);
                        mList.add(bluetoothBean);
                        adapter.notifyItemInserted(mList.size() - 1);
                        mBluetooth.cancelScanBule();
                    }
                }

            }
        });

        pinBlueReceiver = new PinBlueReceiver(new PinBlueCallBack() {
            @Override
            public void onBondRequest() {

            }

            @Override
            public void onBondFail(BluetoothDevice device) {

            }

            @Override
            public void onBonding(BluetoothDevice device) {

            }

            @Override
            public void onBondSuccess(BluetoothDevice device) {
                int m = getModelInList(device);
                if (m != -1) {
                    connect(m);
                }
            }
        },"0000");

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(scanBlueReceiver, filter);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(scanBlueReceiver, filter2);

        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(pinBlueReceiver, filter4);
        registerReceiver(pinBlueReceiver, filter5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        smartRefreshLayout.autoRefresh();
    }

    public void connect(final int m) {
        mList.get(m).getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    showToast("与" + mList.get(m).getBluetoothDevice().getName() + "成功建立连接");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast("与" + mList.get(m).getBluetoothDevice().getName() + "连接失败");
            }

            @Override
            public void onComplete() {
                boolean flag = !mList.get(m).isChangeFlag();
                mList.get(m).setChangeFlag(flag);
                adapter.notifyItemChanged(m);
            }
        });
    }

    public int getModelInList(BluetoothDevice device) {
        int result = -1;
        for (int i = 0; i < mList.size(); i++) {
            if (device.getAddress().equals(mList.get(i).getBluetoothDevice().getAddress())) {
                result = i;
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mList.size()>0)
        {
            if(mList.get(0)!=null)
            {
                if (mList.get(0).isConnect())//是否连接
                {
                    mList.get(0).getMyBluetoothManager().disConnect();
                }
                mList.get(0).setListen(false);
            }
        }

        unregisterReceiver(scanBlueReceiver);
        unregisterReceiver(pinBlueReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == -1)//蓝牙打开
            {
                smartRefreshLayout.autoRefresh();
            }
            else
            {
                showToast("蓝牙打开失败");
                smartRefreshLayout.finishRefresh();
            }
        }
    }
}
