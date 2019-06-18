package kkkj.android.revgoods;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.IAction;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.MyEvent;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.conn.bluetooth.Bluetooth;
import kkkj.android.revgoods.conn.bluetooth.PinBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.PinBlueReceiver;
import kkkj.android.revgoods.conn.bluetooth.ScanBlueReceiver;
import kkkj.android.revgoods.conn.socket.Message;
import kkkj.android.revgoods.conn.socket.ModbusProtocol;
import kkkj.android.revgoods.conn.socket.PulseData;
import kkkj.android.revgoods.conn.socket.SocketListener;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.fragment.CumulativeFragment;
import kkkj.android.revgoods.fragment.DeductionFragment;
import kkkj.android.revgoods.fragment.DeviceListFragment;
import kkkj.android.revgoods.fragment.SamplingDetailsFragment;
import kkkj.android.revgoods.fragment.SamplingFragment;
import kkkj.android.revgoods.relay.adapter.RelayAdapter;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.wifi.model.Order;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

import static com.xuhao.didi.core.iocore.interfaces.IOAction.ACTION_READ_COMPLETE;
import static com.xuhao.didi.socket.client.sdk.client.action.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static kkkj.android.revgoods.utils.GetMacAddress.callCmd;
import static kkkj.android.revgoods.utils.GetMacAddress.getMacFromArpCache;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_ENABLE_BT = 101;
    private ScanBlueReceiver scanBlueReceiver;
    private PinBlueReceiver pinBlueReceiver;
    private Bluetooth mBluetooth;
    private List<BluetoothBean> mList_b;
    private List<RelayBean> mWifiList;
    SocketListener listener;
    IConnectionManager manager;
    private PulseData mPulseData = new PulseData();

    //蓝牙电子秤
    private BluetoothBean bluetoothRelay = new BluetoothBean();

    private RelayAdapter wifiAdapter;
    private boolean isFirst = true;
    private RecyclerView mWifiRelayRecyclerView;
    private ImageView mChooseMatterImageView;
    private ImageView mTakePictureImageView;
    private ImageView mChoosePrinterImageView;
    private TextView mChooseSupplierTextView;
    private TextView mChooseDeviceTextView;
    private TextView mWeightTextView;
    private TextView mPieceweightTextView;//单重
    private TextView mSamplingTextView;//采样
    private TextView mSamplingCount;//采样累计
    private TextView mSamplingNumber;
    private TextView mCumulativeTextView;//累计
    private TextView mDeductionTextView;//扣重
    private DeviceListFragment deviceListFragment;
    private SamplingFragment samplingFragment;
    private SamplingDetailsFragment samplingDetailsFragment;
    private CumulativeFragment cumulativeFragment;
    private DeductionFragment deductionFragment;
    private Observer<String> stateOB;
    private double weight = 0;
    private List<Device> mDeices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = MainActivity.this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background));
        }


        EventBus.getDefault().register(this);
        initData();
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
            deviceListFragment = new DeviceListFragment();
            FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            deviceListFragment.show(ft, "deviceFragment");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (manager != null) {
            manager.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unRegisterReceiver(listener);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(MyEvent myEvent) {
        mChooseDeviceTextView.setText(myEvent.getDevice().getName());

        Device device = myEvent.getDevice();

        switch (device.getType()) {
            case 0://蓝牙电子秤
                String address = device.getBluetoothMac();
                BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                bluetoothRelay = connectAndGetBluetoothScale(bluetoothDevice);
                break;
            case 1://蓝牙继电器
                String address1 = device.getBluetoothMac();
                BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                connectBluetoothRelay(bluetoothDevice1);
                break;
            case 2://Wifi继电器
                connectWifi(device);
                break;
        }


    }

    //蓝牙电子秤
    private BluetoothBean connectAndGetBluetoothScale(BluetoothDevice bluetoothDevice) {
        final boolean[] isConnect = {false};
        BluetoothBean bluetoothBean = new BluetoothBean();
        bluetoothBean.setBluetoothDevice(bluetoothDevice);

        if (bluetoothBean.getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
            bluetoothBean.getMyBluetoothManager().pin();
        } else {
            bluetoothBean.getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    isConnect[0] = aBoolean;
                }

                @Override
                public void onError(Throwable e) {
                    isConnect[0] = false;
                }

                @Override
                public void onComplete() {
                    Flowable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                            .takeWhile(new Predicate<Long>() {
                                @Override
                                public boolean test(Long integer) throws Exception {
                                    return true;
                                }
                            })
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(@NonNull Long aLong) throws Exception {
                                    bluetoothBean.getMyBluetoothManager().getReadOB().subscribe(new Observer<String>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                            if (!bluetoothBean.getMyBluetoothManager().isConnect()) {
                                                Logger.d("蓝牙断开");
                                                d.dispose();
                                            }
                                        }

                                        @Override
                                        public void onNext(String s) {
                                            Logger.d("读取到数据:" + s);
                                            if (s.length() == 8) {
                                                //去掉前面的零和“=”号
                                                String str = s.replaceFirst("^0*", "").replace("=", "");
                                                if (str.startsWith(".")) {
                                                    str = "0" + str;
                                                }
                                                mWeightTextView.setText(str);
                                                double weight = Double.parseDouble(str);
                                                if (weight > 3) {
                                                    manager.send(new WriteData(Order.TURN_ON_3));
                                                    manager.send(new WriteData(Order.TURN_ON_2));
                                                    Observable.timer(2,TimeUnit.SECONDS)
                                                            .subscribe(new Observer<Long>() {
                                                                @Override
                                                                public void onSubscribe(Disposable d) {

                                                                }

                                                                @Override
                                                                public void onNext(Long aLong) {
                                                                    manager.send(new WriteData(Order.TURN_OFF_3));
                                                                    manager.send(new WriteData(Order.TURN_OFF_2));

                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {

                                                                }

                                                                @Override
                                                                public void onComplete() {

                                                                }
                                                            });

                                                }


                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.d("读取错误:" + e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                                }
                            });
                }
            });
        }
        if (isConnect[0]) {
            return bluetoothBean;
        } else {
            return null;
        }

    }

    //蓝牙继电器
    private void connectBluetoothRelay(BluetoothDevice bluetoothDevice) {

        BluetoothBean bluetoothBean = new BluetoothBean();
        bluetoothBean.setBluetoothDevice(bluetoothDevice);

        if (bluetoothBean.getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
            bluetoothBean.getMyBluetoothManager().pin();
        } else {
            bluetoothBean.getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        initRelay();
                        Logger.d("与" + bluetoothDevice.getName() + "成功建立连接");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Logger.d("与" + bluetoothDevice.getName() + "连接失败");
                }

                @Override
                public void onComplete() {
                    /**  boolean flag = !mWifiList.get(m).isChangeFlag();
                     mWifiList.get(m).setChangeFlag(flag);
                     adapter.notifyItemChanged(m);
                     */
                    // bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_1).subscribe(stateOB);
                    //manager.send(new WriteData(Order.TURN_ON_1));
                }
            });
        }
    }


    //连接Wifi继电器
    private void connectWifi(Device device) {
        initWifiDevice(device);

        if (manager != null) {
            manager.connect();
            Logger.d("连接Wifi");
        }
    }

    /**
     * 初始化wifi设备
     */
    private void initWifiDevice(Device device) {
        String ip = device.getWifiIp();
        int port = device.getWifiPort();
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        ConnectionInfo info = new ConnectionInfo(ip, port);
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        manager = OkSocket.open(info);
        //设置自定义解析头
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setPulseFrequency(10000);
        okOptionsBuilder.setReaderProtocol(new ModbusProtocol());
        //将新的修改后的参配设置给连接管理器
        manager.option(okOptionsBuilder.build());
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        listener = new SocketListener(new Observer<Message>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Message message) {
                Logger.d("Action:" + message.getAction());
                switch (message.getAction()) {
                    case IAction.ACTION_CONNECTION_FAILED:
                        //showToast("连接失败");
                        break;

                    case IAction.ACTION_CONNECTION_SUCCESS://连接成功
                        //showToast("连接成功");
                        Logger.d("mac地址：" + getMacFromArpCache(manager.getRemoteConnectionInfo().getIp()));
                        manager.getPulseManager()
                                .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                                .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
                        manager.send(new WriteData(Order.GET_STATE));
                        break;
                    case IAction.ACTION_DISCONNECTION:
                    case ACTION_READ_THREAD_SHUTDOWN://断开
                        break;
                    case ACTION_READ_COMPLETE:
                        if (manager != null && message.getData().trim().equals("00 02 01 00")) {
                            manager.getPulseManager().feed();
                            return;
                        }
                        if (message.getData().indexOf("01 01 02 ") == 0) {
                            //收到状态
                            String state = message.getData().substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                            String binaryState = hexString2binaryString(state);
                            char[] bin = binaryState.toCharArray();
                            if (bin.length == 8) {
                                for (int i = 0; i < bin.length; i++) {
                                    mWifiList.get(i).setState(bin[i] + "");
                                }
                            }
                            wifiAdapter.notifyDataSetChanged();
                        } else if (message.getData().indexOf("01 05 00 ") == 0) {
                            //收到状态
                            //第几个继电器
                            String index = message.getData().substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            //继电器状态
                            String state = message.getData().substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---" + index + "---" + state + "---");
                            switch (index) {
                                case "00":
                                    if (!state.equals("00")) {
                                        mWifiList.get(0).setState("1");
                                    } else {
                                        mWifiList.get(0).setState("0");
                                    }
                                    break;
                                case "01":
                                    if (!state.equals("00")) {
                                        mWifiList.get(1).setState("1");
                                    } else {
                                        mWifiList.get(1).setState("0");
                                    }
                                    break;
                                case "02":
                                    if (!state.equals("00")) {
                                        mWifiList.get(2).setState("1");
                                    } else {
                                        mWifiList.get(2).setState("0");
                                    }
                                    break;
                                case "03":
                                    if (!state.equals("00")) {
                                        mWifiList.get(3).setState("1");
                                    } else {
                                        mWifiList.get(3).setState("0");
                                    }
                                    break;
                                case "04":
                                    if (!state.equals("00")) {
                                        mWifiList.get(4).setState("1");
                                    } else {
                                        mWifiList.get(4).setState("0");
                                    }
                                    break;
                                case "05":
                                    if (!state.equals("00")) {
                                        mWifiList.get(5).setState("1");
                                    } else {
                                        mWifiList.get(5).setState("0");
                                    }
                                    break;
                                case "06":
                                    if (!state.equals("00")) {
                                        mWifiList.get(6).setState("1");
                                    } else {
                                        mWifiList.get(6).setState("0");
                                    }
                                    break;
                                case "07":
                                    if (!state.equals("00")) {
                                        mWifiList.get(7).setState("1");
                                    } else {
                                        mWifiList.get(7).setState("0");
                                    }
                                    break;
                            }
                            wifiAdapter.notifyDataSetChanged();
                        }
                        Logger.d("收到:" + message.getData());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (manager != null) {
                    // showToast(e.getMessage());

                }
            }

            @Override
            public void onComplete() {

            }
        });
        manager.registerReceiver(listener);
    }


    private void initView() {
        mChooseMatterImageView = findViewById(R.id.id_iv_choose_matter);
        mChooseDeviceTextView = findViewById(R.id.id_tv_choose_device);
        mWeightTextView = findViewById(R.id.id_tv_weight);
        mTakePictureImageView = findViewById(R.id.id_iv_takePicture);
        mChoosePrinterImageView = findViewById(R.id.id_iv_choose_printer);
        mWifiRelayRecyclerView = findViewById(R.id.id_wifiRelay_recyclerView);
        mPieceweightTextView = findViewById(R.id.id_tv_piece_weight);
        mChooseSupplierTextView = findViewById(R.id.id_tv_choose_supplier);
        mSamplingTextView = findViewById(R.id.id_tv_sampling);
        mSamplingCount = findViewById(R.id.id_tv_sampling_count);
        mCumulativeTextView = findViewById(R.id.id_tv_cumulative);
        mDeductionTextView = findViewById(R.id.id_tv_deduction);
        mSamplingNumber = findViewById(R.id.id_tv_sampling_number);
        mDeductionTextView.setOnClickListener(this);
        mCumulativeTextView.setOnClickListener(this);
        mSamplingCount.setOnClickListener(this);
        mSamplingTextView.setOnClickListener(this);
        mChooseSupplierTextView.setOnClickListener(this);
        mPieceweightTextView.setOnClickListener(this);
        mChoosePrinterImageView.setOnClickListener(this);
        mTakePictureImageView.setOnClickListener(this);
        mChooseMatterImageView.setOnClickListener(this);
        mChooseDeviceTextView.setOnClickListener(this);

        wifiAdapter = new RelayAdapter(R.layout.item_wifi_relay,mWifiList);
        mWifiRelayRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //mWifiRelayRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
               // DividerItemDecoration.VERTICAL));
        wifiAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_open:
                        switch (position) { //position 哪个继电器
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
                    case R.id.tv_close:
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
        mWifiRelayRecyclerView.setAdapter(wifiAdapter);

    }

    private void initData() {

        mDeices = new ArrayList<>();
        isFirst = true;
        //BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);

        mBluetooth = new Bluetooth();
        //蓝牙设备集合
        mList_b = new ArrayList<>();
        //继电器实体类集合
        mWifiList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setName(i + 1 + "号继电器");
            mWifiList.add(relayBean);
        }




        if (!mBluetooth.isSupportBlue()) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_LONG).show();
            return;
        } else if (!mBluetooth.isBlueEnable()) {
            Toast.makeText(this, "请先打开蓝牙蓝牙", Toast.LENGTH_LONG).show();
            //mBluetooth.openBlueSync(MainActivity.this, REQUEST_ENABLE_BT);
            mBluetooth.openBlueAsyn();
        }
        //注册蓝牙广播
        rigistReciver();
    }

    private void rigistReciver() {

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
                switch (device.getAddress()) {
                    case "20:17:03:15:05:65"://电子秤
                        connectAndGetBluetoothScale(device);
                        break;

                    case "00:0D:1B:00:13:BA":
                        connectBluetoothRelay(device);//继电器
                        break;

                    default:
                        break;
                }

            }
        }, "1234");

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(scanBlueReceiver, filter);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(scanBlueReceiver, filter2);

        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(pinBlueReceiver, filter4);
        registerReceiver(pinBlueReceiver, filter5);

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

    public void connect(final int m) {
        mList_b.get(m).getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    initRelay();
                    // showToast("与" + mList_b.get(m).getBluetoothDevice().getName() + "成功建立连接");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //showToast("与" + mList_b.get(m).getBluetoothDevice().getName() + "连接失败");
            }

            @Override
            public void onComplete() {
                boolean flag = !mList_b.get(m).isChangeFlag();
                mList_b.get(m).setChangeFlag(flag);
                //adapter_b.notifyItemChanged(m);
            }
        });
    }

    public void initRelay() {
        //recyclerView.setVisibility(View.VISIBLE);
        stateOB = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

                 mList_b.get(0).getMyBluetoothManager().getReadOBModbus().subscribe(new Observer<byte[]>() {
                @Override public void onSubscribe(Disposable d) {

                }

                @Override public void onNext(byte[] bytes) {
                Logger.d("----" + BytesUtils.toHexStringForLog(bytes) + "----");
                String message = BytesUtils.toHexStringForLog(bytes);
                if (message.indexOf("01 05 00 ") == 0) {
                //收到状态
                String index = message.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                String state = message.substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                Logger.d("---" + index + "---" + state + "---");
                switch (index) {
                case "00":
                if (!state.equals("00")) {
                mWifiList.get(0).setState("1");
                } else {
                mWifiList.get(0).setState("0");
                }
                break;
                case "01":
                if (!state.equals("00")) {
                mWifiList.get(1).setState("1");
                } else {
                mWifiList.get(1).setState("0");
                }
                break;
                case "02":
                if (!state.equals("00")) {
                mWifiList.get(2).setState("1");
                } else {
                mWifiList.get(2).setState("0");
                }
                break;
                case "03":
                if (!state.equals("00")) {
                mWifiList.get(3).setState("1");
                } else {
                mWifiList.get(3).setState("0");
                }
                break;
                case "04":
                if (!state.equals("00")) {
                mWifiList.get(4).setState("1");
                } else {
                mWifiList.get(4).setState("0");
                }
                break;
                case "05":
                if (!state.equals("00")) {
                mWifiList.get(5).setState("1");
                } else {
                mWifiList.get(5).setState("0");
                }
                break;
                case "06":
                if (!state.equals("00")) {
                mWifiList.get(6).setState("1");
                } else {
                mWifiList.get(6).setState("0");
                }
                break;
                case "07":
                if (!state.equals("00")) {
                mWifiList.get(7).setState("1");
                } else {
                mWifiList.get(7).setState("0");
                }
                break;
                default:
                break;
                }
                wifiAdapter.notifyDataSetChanged();

                }
                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onComplete() {

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == -1)//蓝牙打开
            {
                //smartRefreshLayout.autoRefresh();
            } else {
                //showToast("蓝牙打开失败");
                //smartRefreshLayout.finishRefresh();
            }
        }
        //拍照回调
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                GetPicModel picOrMp4 = new GetPicModel();
                picOrMp4 = (GetPicModel) data.getSerializableExtra("result");

            }
        }

    }

    //沉浸式模式
   /** @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_iv_choose_matter://选择物料
                startActivity(new Intent(MainActivity.this, ChooseMatterActivity.class));
                break;

            case R.id.id_tv_choose_supplier://选择供应商
                startActivity(new Intent(MainActivity.this,ChooseSupplierActivity.class));
                break;

            case R.id.id_tv_sampling://采样
                if (samplingFragment == null) {
                    samplingFragment = new SamplingFragment();
                }
                FragmentTransaction ft1 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                samplingFragment.show(ft1,"samplingFragment");
                break;

            case R.id.id_tv_sampling_count://采样累计
                if (samplingDetailsFragment == null) {
                    samplingDetailsFragment = new SamplingDetailsFragment();
                }
                FragmentTransaction ft2 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                samplingDetailsFragment.show(ft2,"samplingDetailsFragment");
                break;

            case R.id.id_tv_cumulative://累计
                if (cumulativeFragment == null) {
                    cumulativeFragment = new CumulativeFragment();
                }
                FragmentTransaction ft3 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                cumulativeFragment.show(ft3,"cumulativeFragment");
                break;

            case R.id.id_tv_deduction:
                if (deductionFragment == null) {
                    deductionFragment = new DeductionFragment();
                }
                FragmentTransaction ft4 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                deductionFragment.show(ft4,"deductionFragment");
                break;

            case R.id.id_tv_choose_device:
            case R.id.id_iv_choose_printer:
                if (deviceListFragment == null) {
                    deviceListFragment = new DeviceListFragment();
                }
                FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                deviceListFragment.show(ft, "deviceFragment");
                break;

            case R.id.id_tv_piece_weight://单重
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder inputDialog =new AlertDialog.Builder(MainActivity.this);
                inputDialog.setTitle("请输入单重").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_PIECE_WEIGHT,editText.getText().toString().trim());

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.id_iv_takePicture:
                takePicture();
                break;

            default:
                break;
        }
    }

    //查表法，将16进制转为2进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {

            return null;
        }

        String bString = "";
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 拍照
     */
    private void takePicture() {
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        startActivityForResult(new Intent(MainActivity.this, GetPicOrMP4Activity.class), 200);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //有至少一个权限没有同意
                        //showToast("请同意全部权限");
                    } else {
                        //有至少一个权限没有同意且勾选了不在提示
                        //showToast("请在权限管理中打开相关权限");
                    }
                });
    }

}
