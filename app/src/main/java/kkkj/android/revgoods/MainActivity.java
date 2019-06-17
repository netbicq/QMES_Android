package kkkj.android.revgoods;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
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
import java.util.zip.Inflater;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
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
import kkkj.android.revgoods.fragment.DeviceListFragment;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.relay.wifi.model.Order;

import static com.xuhao.didi.core.iocore.interfaces.IOAction.ACTION_READ_COMPLETE;
import static com.xuhao.didi.socket.client.sdk.client.action.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static kkkj.android.revgoods.utils.GetMacAddress.getMacFromArpCache;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_ENABLE_BT = 101;
    private ScanBlueReceiver scanBlueReceiver;
    private PinBlueReceiver pinBlueReceiver;
    private Bluetooth mBluetooth;
    private List<BluetoothBean> mList_b;
    private List<RelayBean> mList;
    SocketListener listener;
    IConnectionManager manager;
    private PulseData mPulseData = new PulseData();

    private boolean isFirst = true;
    private ImageView mChooseMatterImageView;
    private ImageView mTakePictureImageView;
    private ImageView mZXingImageView;
    private TextView mChooseDeviceTextView;
    private TextView mWeightTextView;
    private DeviceListFragment deviceListFragment;
    private Observer<String> stateOB;
    private double weight = 0;
    private Observable<String> observableWeight;
    private List<Device> mDeices;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Device device = myEvent.getDevice();
        mDeices.add(device);
        mChooseDeviceTextView.setText(myEvent.getDevice().getName());

        if (device.getType() == 0) {
            //蓝牙设备
            String address = device.getBluetoothMac();
            BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            connectBluetooth(bluetoothDevice);
        } else {
            //Wifi设备
            connectWifi(device);

        }


    }

    //连接蓝牙设备
    private void connectBluetooth(BluetoothDevice bluetoothDevice) {

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
                    /**  boolean flag = !mList.get(m).isChangeFlag();
                     mList.get(m).setChangeFlag(flag);
                     adapter.notifyItemChanged(m);
                     */

                    // bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_1).subscribe(stateOB);
                    if (bluetoothDevice.getAddress().equals("20:17:03:15:05:65")) {
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

                                                    weight = Double.parseDouble(str);
                                                    if (weight > 10 && manager != null) {
                                                        manager.send(new WriteData(Order.TURN_ON_1));

                                                    }
                                                    observableWeight = getWeight(str);

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

                    if (observableWeight != null) {
                        observableWeight.subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                weight = Double.parseDouble(s);
                                Logger.d("重量;" + weight);
                                for (int i = 0;i<mDeices.size();i++) {
                                    if (mDeices.get(i).getName().equals("蓝牙继电器")) {
                                        String address = mDeices.get(i).getBluetoothMac();
                                        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                                        BluetoothBean bluetoothBean = new BluetoothBean();
                                        bluetoothBean.setBluetoothDevice(bluetoothDevice);

                                        if (weight > 10) {
                                            bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_2).subscribe(stateOB);
                                        }
                                    }
                                }
                                if (weight > 10) {
                                    manager.send(new WriteData(Order.TURN_ON_1));

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

                }
            });
        }
    }

    private Observable<String> getWeight(String str) {
        Observable<String> getWeight = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(str);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        return getWeight;
    }

    //连接Wifi设备
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
                                    mList.get(i).setState(bin[i] + "");
                                }
                            }

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
        mZXingImageView = findViewById(R.id.id_iv_zxing);
        mZXingImageView.setOnClickListener(this);
        mTakePictureImageView.setOnClickListener(this);
        mChooseMatterImageView.setOnClickListener(this);
        mChooseDeviceTextView.setOnClickListener(this);

    }

    private void initData() {

        mDeices = new ArrayList<>();
        isFirst = true;
        //BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);

        mBluetooth = new Bluetooth();
        //蓝牙设备集合
        mList_b = new ArrayList<>();
        //继电器实体类集合
        mList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setName(i + 1 + "号继电器");
            mList.add(relayBean);
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
                connectBluetooth(device);
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
                /**
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
                default:
                break;
                }
                // adapter.notifyDataSetChanged();
                }
                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onComplete() {

                }
                });
                 */
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

        //二维码扫描回调
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    /**
                    result = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.d(result);
//                    fragments.get(showPosition).reserve(result);
                    GetEmpTaskByQRCoderModel.Request request = new GetEmpTaskByQRCoderModel.Request();
                    request.setDangerPointID(result);
                    mPresenter.getEmpTaskByQRCoder(request);
                     */
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                   // showToast("解析二维码失败");
                }
            }
        }
    }

    //沉浸式模式
    @Override
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
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_iv_choose_matter:
                startActivity(new Intent(MainActivity.this, ChooseMatterActivity.class));
                break;

            case R.id.id_tv_choose_device:
                if (deviceListFragment == null) {
                    deviceListFragment = new DeviceListFragment();
                }
                FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                deviceListFragment.show(ft, "deviceFragment");
                break;

            case R.id.id_iv_takePicture:
                takePicture();
                break;

            case R.id.id_iv_zxing:
                zxing();
                break;

            default:
                break;
        }
    }

    //查表法，将16进制转为2进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "";
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

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

    /**
     * 获取权限
     */
    private void zxing() {
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
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
