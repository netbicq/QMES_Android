package kkkj.android.revgoods.relay.wifi.view;

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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.IAction;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

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
import kkkj.android.revgoods.conn.socket.Message;
import kkkj.android.revgoods.conn.socket.ModbusProtocol;
import kkkj.android.revgoods.conn.socket.PulseData;
import kkkj.android.revgoods.conn.socket.SocketListener;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.elcscale.adapter.ElcScaleManagerAdapter;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.relay.adapter.RelayAdapter;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.relay.wifi.model.Order;

import static com.xuhao.didi.core.iocore.interfaces.IOAction.ACTION_READ_COMPLETE;
import static com.xuhao.didi.socket.client.sdk.client.action.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static kkkj.android.revgoods.relay.wifi.view.WifiRelayActivity.hexString2binaryString;
import static kkkj.android.revgoods.utils.GetMacAddress.getMacFromArpCache;

/**
 * ???????????????
 */

public class BlueToothRelayActivity extends MvpBaseActivity {
    private int REQUEST_ENABLE_BT = 101;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView_b)
    RecyclerView recyclerView_b;
    ScanBlueReceiver scanBlueReceiver;
    PinBlueReceiver pinBlueReceiver;
    List<BluetoothBean> mList_b;
    ElcScaleManagerAdapter adapter_b;
    Bluetooth mBluetooth;

    SocketListener listener;
    IConnectionManager manager;
    private PulseData mPulseData = new PulseData();
    @BindView(R.id.id_recyclerView)
    RecyclerView wifiRecyclerView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.id_tv_connect)
    TextView tv_connect;

    RelayAdapter adapter;
    List<RelayBean> mList;

    RelayAdapter wifiadapter;
    List<RelayBean> wifimList;


    @Override
    protected int getLayout() {
        return R.layout.activity_bluet_relay;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        //wifi
        wifimList = new ArrayList<>();

        wifiadapter = new RelayAdapter(R.layout.item_wifi_relay, wifimList);
        wifiadapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_menu1:
                        switch (position) { //position ???????????????
                            case 0:
                                manager.send(new WriteData(Order.TURN_ON_1));
                                break;
                            case 1:
                                manager.send(new WriteData(Order.TURN_ON_2));
                                break;
                            case 2:
                                manager.send(new WriteData(Order.TURN_ON_3));
                                break;
                            case 3:
                                manager.send(new WriteData(Order.TURN_ON_4));
                                break;
                            case 4:
                                manager.send(new WriteData(Order.TURN_ON_5));
                                break;
                            case 5:
                                manager.send(new WriteData(Order.TURN_ON_6));
                                break;
                            case 6:
                                manager.send(new WriteData(Order.TURN_ON_7));
                                break;
                            case 7:
                                manager.send(new WriteData(Order.TURN_ON_8));
                                break;
                        }
                        break;
                    case R.id.tv_menu2:
                        switch (position) {
                            case 0:
                                manager.send(new WriteData(Order.TURN_OFF_1));
                                break;
                            case 1:
                                manager.send(new WriteData(Order.TURN_OFF_2));
                                break;
                            case 2:
                                manager.send(new WriteData(Order.TURN_OFF_3));
                                break;
                            case 3:
                                manager.send(new WriteData(Order.TURN_OFF_4));
                                break;
                            case 4:
                                manager.send(new WriteData(Order.TURN_OFF_5));
                                break;
                            case 5:
                                manager.send(new WriteData(Order.TURN_OFF_6));
                                break;
                            case 6:
                                manager.send(new WriteData(Order.TURN_OFF_7));
                                break;
                            case 7:
                                manager.send(new WriteData(Order.TURN_OFF_8));
                                break;
                        }
                        break;
                }

            }
        });
        wifiRecyclerView.setAdapter(wifiadapter);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < 8; i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setName(i + 1 + "????????????");
            wifimList.add(relayBean);
        }

        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager != null) {
                    manager.connect();
                }
            }
        });

        //??????????????????(IP,?????????),????????????????????????????????????,????????????,????????????????????????????????????????????????
        ConnectionInfo info = new ConnectionInfo("192.168.123.105", 10001);
        //??????OkSocket,???????????????????????????,????????????Manager
        manager = OkSocket.open(info);
        //????????????????????????
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setPulseFrequency(10000);
        okOptionsBuilder.setReaderProtocol(new ModbusProtocol());
        //???????????????????????????????????????????????????
        manager.option(okOptionsBuilder.build());
        //??????Socket???????????????,SocketActionAdapter????????????Simple???,????????????????????????????????????
        listener = new SocketListener(new Observer<Message>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Message message) {
                Logger.d("Action:" + message.getAction());
                switch (message.getAction()) {
                    case IAction.ACTION_CONNECTION_FAILED:
                        showToast("????????????");
                        break;

                    case IAction.ACTION_CONNECTION_SUCCESS://????????????
                        showToast("????????????");
                        Logger.d("mac?????????"+getMacFromArpCache(manager.getRemoteConnectionInfo().getIp()));
                        manager.getPulseManager()
                                .setPulseSendable(mPulseData)//?????????????????????,???????????????????????????pulse()
                                .pulse();//????????????,???????????????,??????????????????????????????????????????

                        wifiRecyclerView.setVisibility(View.VISIBLE);
                        manager.send(new WriteData(Order.GET_STATE));
                        break;
                    case IAction.ACTION_DISCONNECTION:
                    case ACTION_READ_THREAD_SHUTDOWN://??????

                        wifiRecyclerView.setVisibility(View.INVISIBLE);
                        break;
                    case ACTION_READ_COMPLETE:
                        if (manager != null && message.getData().trim().equals("00 02 01 00")) {
                            manager.getPulseManager().feed();
                            return;
                        }
                        if (message.getData().indexOf("01 01 02 ") == 0) {
                            //????????????
                            String state = message.getData().substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                            String binaryState = hexString2binaryString(state);
                            char[] bin = binaryState.toCharArray();
                            if (bin.length == 8) {
                                for (int i = 0; i < bin.length; i++) {
                                    wifimList.get(i).setState(bin[i] + "");
                                }
                            }
                            wifiadapter.notifyDataSetChanged();
                        } else if (message.getData().indexOf("01 05 00 ") == 0) {
                            //????????????
                            //??????????????????
                            String index = message.getData().substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            //???????????????
                            String state = message.getData().substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---"+index+"---"+state+"---");
                            switch (index) {
                                case "00":
                                    if (!state.equals("00")) {
                                        wifimList.get(0).setState("1");
                                    } else {
                                        wifimList.get(0).setState("0");
                                    }
                                    break;
                                case "01":
                                    if (!state.equals("00")) {
                                        wifimList.get(1).setState("1");
                                    } else {
                                        wifimList.get(1).setState("0");
                                    }
                                    break;
                                case "02":
                                    if (!state.equals("00")) {
                                        wifimList.get(2).setState("1");
                                    } else {
                                        wifimList.get(2).setState("0");
                                    }
                                    break;
                                case "03":
                                    if (!state.equals("00")) {
                                        wifimList.get(3).setState("1");
                                    } else {
                                        wifimList.get(3).setState("0");
                                    }
                                    break;
                                case "04":
                                    if (!state.equals("00")) {
                                        wifimList.get(4).setState("1");
                                    } else {
                                        wifimList.get(4).setState("0");
                                    }
                                    break;
                                case "05":
                                    if (!state.equals("00")) {
                                        wifimList.get(5).setState("1");
                                    } else {
                                        wifimList.get(5).setState("0");
                                    }
                                    break;
                                case "06":
                                    if (!state.equals("00")) {
                                        wifimList.get(6).setState("1");
                                    } else {
                                        wifimList.get(6).setState("0");
                                    }
                                    break;
                                case "07":
                                    if (!state.equals("00")) {
                                        wifimList.get(7).setState("1");
                                    } else {
                                        wifimList.get(7).setState("0");
                                    }
                                    break;
                            }
                            wifiadapter.notifyDataSetChanged();
                        }
                        Logger.d("??????:" + message.getData());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (manager != null) {
                    showToast(e.getMessage());
                    wifiRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onComplete() {

            }
        });

        manager.registerReceiver(listener);


        //??????
        action_bar_title.setText("????????????");
        mBluetooth = new Bluetooth();
        mList_b = new ArrayList<>();
        adapter_b = new ElcScaleManagerAdapter(R.layout.item_elcscale, mList_b);
        adapter_b.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mBluetooth.cancelScanBule();
                switch (view.getId()) {
                    case R.id.connect:
                        Logger.d("????????????"+mList_b.get(position).isConnect());
                        if (mList_b.get(position).isConnect())//????????????
                        {
                            if(mList_b.get(position).isListen())
                            {
                                mList_b.get(position).setListen(false);
                            }
                            mList_b.get(position).getMyBluetoothManager().disConnect();
                            closeRelay();
                            adapter.notifyItemChanged(position);
                        } else {
                            //BluetoothDevice.BOND_BONDED = ???????????????
                            if (mList_b.get(position).getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                                //????????????
                                mList_b.get(position).getMyBluetoothManager().pin();
                            } else {
                                //?????????  ?????????
                                connect(position);
                            }
                        }

                        break;
                    case R.id.listen:
                        if(mList_b.get(position).isListen())
                        {
                            mList_b.get(position).setListen(false);
                        }
                        else {
                            mList_b.get(position).setListen(true);
                        }
                        adapter.notifyItemChanged(position);
                        mList_b.get(position).getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                Logger.d(s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                        break;
                }
            }
        });
        recyclerView_b.setAdapter(adapter_b);
        recyclerView_b.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //????????????????????????????????????????????????
                if(mList_b.size()>0)
                {
                    for(int i = 0 ;i<mList_b.size();i++)
                    {
                        if (mList_b.get(i).isConnect())//????????????
                        {
                            mList_b.get(i).getMyBluetoothManager().disConnect();
                        }
                        mList_b.get(i).setListen(false);
                    }
                }

                if (!mBluetooth.scanBlue()) {
                    smartRefreshLayout.finishRefresh();
                    mBluetooth.openBlueSync(mActivity, REQUEST_ENABLE_BT);
                }
                else
                {
                    mList_b.clear();
                    adapter_b.notifyDataSetChanged();
                }
            }
        });

        if (!mBluetooth.isSupportBlue()) {
            showToast("???????????????????????????");
            return;
        }
        rigistReciver();
    }
    public void closeRelay()
    {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void initRelay()
    {
        recyclerView.setVisibility(View.VISIBLE);
        Observer<String> stateOB = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                mList_b.get(0).getMyBluetoothManager().getReadOBModbus().subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        Logger.d("----"+BytesUtils.toHexStringForLog(bytes)+"----");
                        String message = BytesUtils.toHexStringForLog(bytes);
                        if (message.indexOf("01 05 00 ") == 0) {
                            //????????????
                            String index = message.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            String state = message.substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---"+index+"---"+state+"---");
                            switch (index) {
                                case "00":
                                    if (!state.equals("00")) {
                                        mList.get(0).setState("1");
                                    } else {
                                        mList.get(0).setState("0");
                                    }
                                    break;
                                case "01":
                                    if (!state.equals("00")) {
                                        mList.get(1).setState("1");
                                    } else {
                                        mList.get(1).setState("0");
                                    }
                                    break;
                                case "02":
                                    if (!state.equals("00")) {
                                        mList.get(2).setState("1");
                                    } else {
                                        mList.get(2).setState("0");
                                    }
                                    break;
                                case "03":
                                    if (!state.equals("00")) {
                                        mList.get(3).setState("1");
                                    } else {
                                        mList.get(3).setState("0");
                                    }
                                    break;
                                case "04":
                                    if (!state.equals("00")) {
                                        mList.get(4).setState("1");
                                    } else {
                                        mList.get(4).setState("0");
                                    }
                                    break;
                                case "05":
                                    if (!state.equals("00")) {
                                        mList.get(5).setState("1");
                                    } else {
                                        mList.get(5).setState("0");
                                    }
                                    break;
                                case "06":
                                    if (!state.equals("00")) {
                                        mList.get(6).setState("1");
                                    } else {
                                        mList.get(6).setState("0");
                                    }
                                    break;
                                case "07":
                                    if (!state.equals("00")) {
                                        mList.get(7).setState("1");
                                    } else {
                                        mList.get(7).setState("0");
                                    }
                                    break;
                            }
                            adapter.notifyDataSetChanged();
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

        mList = new ArrayList<>();
        adapter = new RelayAdapter(R.layout.item_relay, mList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //???????????????
                    case R.id.tv_menu1:
                        Toast.makeText(getContext(),position+"????????????",Toast.LENGTH_LONG).show();
                        switch (position) {
                            case 0:
                                Logger.d("???????????????Mac:"+mList_b.get(0).getBluetoothDevice().getAddress());
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_1).subscribe(stateOB);
                                break;
                            case 1:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_2).subscribe(stateOB);
                                break;
                            case 2:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_3).subscribe(stateOB);
                                break;
                            case 3:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_4).subscribe(stateOB);
                                break;
                            case 4:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_5).subscribe(stateOB);
                                break;
                            case 5:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_6).subscribe(stateOB);
                                break;
                            case 6:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_7).subscribe(stateOB);
                                break;
                            case 7:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_8).subscribe(stateOB);
                                break;
                        }
                        break;
                    case R.id.tv_menu2:
                        switch (position) {
                            case 0:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_1).subscribe(stateOB);
                                break;
                            case 1:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_2).subscribe(stateOB);
                                break;
                            case 2:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_3).subscribe(stateOB);
                                break;
                            case 3:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_4).subscribe(stateOB);
                                break;
                            case 4:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_5).subscribe(stateOB);
                                break;
                            case 5:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_6).subscribe(stateOB);
                                break;
                            case 6:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_7).subscribe(stateOB);
                                break;
                            case 7:
                                mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_8).subscribe(stateOB);
                                break;
                        }
                        break;
                }

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < 8; i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setName(i + 1 + "????????????");
            mList.add(relayBean);
        }
        mList_b.get(0).getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                mList_b.get(0).getMyBluetoothManager().getReadOBModbus().subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        Logger.d("----"+BytesUtils.toHexStringForLog(bytes)+"----");
                        String message = BytesUtils.toHexStringForLog(bytes);
                        if (message.indexOf("01 01 02 ") == 0) {
                            //????????????
                            String state = message.substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                            String binaryState = hexString2binaryString(state);
                            Logger.d("??????"+binaryState);
                            char[] bin = binaryState.toCharArray();
                            if (bin.length == 8) {
                                for (int i = 0; i < bin.length; i++) {
                                    mList.get(i).setState(bin[i] + "");
                                }
                            }
                            adapter.notifyDataSetChanged();
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
        });
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
                    //if(device.getName().equals("BCM9A"))

                        BluetoothBean bluetoothBean = new BluetoothBean();
                        bluetoothBean.setBluetoothDevice(device);
                        bluetoothBean.setWeight(0);
                        mList_b.add(bluetoothBean);
                        adapter_b.notifyItemInserted(mList_b.size() - 1);
                        mBluetooth.cancelScanBule();

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
        },"1234");

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
        mList_b.get(m).getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    initRelay();
                    showToast("???" + mList_b.get(m).getBluetoothDevice().getName() + "??????????????????");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast("???" + mList_b.get(m).getBluetoothDevice().getName() + "????????????");
            }

            @Override
            public void onComplete() {
                boolean flag = !mList_b.get(m).isChangeFlag();
                mList_b.get(m).setChangeFlag(flag);
                adapter_b.notifyItemChanged(m);
            }
        });
    }

    public int getModelInList(BluetoothDevice device) {
        int result = -1;
        for (int i = 0; i < mList_b.size(); i++) {
            if (device.getAddress().equals(mList_b.get(i).getBluetoothDevice().getAddress())) {
                result = i;
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unRegisterReceiver(listener);
        if(mList_b.size()>0)
        {
            if(mList_b.get(0)!=null)
            {
                if (mList_b.get(0).isConnect())//????????????
                {
                    mList_b.get(0).getMyBluetoothManager().disConnect();
                }
                mList_b.get(0).setListen(false);
            }
        }
        unregisterReceiver(scanBlueReceiver);
        unregisterReceiver(pinBlueReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (manager.isConnect()) {
            manager.disconnect();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == -1)//????????????
            {
                smartRefreshLayout.autoRefresh();
            }
            else
            {
                showToast("??????????????????");
                smartRefreshLayout.finishRefresh();
            }
        }
    }
}
