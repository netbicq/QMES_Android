package kkkj.android.revgoods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.IAction;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.adapter.SwitchAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.SwitchIcon;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.conn.bluetooth.Bluetooth;
import kkkj.android.revgoods.conn.bluetooth.PinBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.PinBlueReceiver;
import kkkj.android.revgoods.conn.socket.Message;
import kkkj.android.revgoods.conn.socket.ModbusProtocol;
import kkkj.android.revgoods.conn.socket.PulseData;
import kkkj.android.revgoods.conn.socket.SocketListener;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.customer.ReSpinner;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.elcscale.view.ElcScaleActivity;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.fragment.BillListFragment;
import kkkj.android.revgoods.fragment.CumulativeFragment;
import kkkj.android.revgoods.fragment.DeductionFragment;
import kkkj.android.revgoods.fragment.DeviceListFragment;
import kkkj.android.revgoods.fragment.SamplingDetailsFragment;
import kkkj.android.revgoods.fragment.SamplingFragment;
import kkkj.android.revgoods.fragment.SaveBillFragment;
import kkkj.android.revgoods.fragment.SettingFragment;
import kkkj.android.revgoods.relay.adapter.RelayAdapter;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.wifi.model.Order;
import kkkj.android.revgoods.ui.ChooseMatterActivity;
import kkkj.android.revgoods.ui.ChooseSpecsActivity;
import kkkj.android.revgoods.ui.ChooseSupplierActivity;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import kkkj.android.revgoods.utils.StringUtils;

import static com.xuhao.didi.core.iocore.interfaces.IOAction.ACTION_READ_COMPLETE;
import static com.xuhao.didi.socket.client.sdk.client.action.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static kkkj.android.revgoods.utils.GetMacAddress.getMacFromArpCache;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.id_wifiRelay_recyclerView)
    RecyclerView mWifiRelayRecyclerView;
    @BindView(R.id.id_iv_choose_matter)
    ImageView mChooseMatterImageView;
    @BindView(R.id.id_iv_takePicture)
    ImageView mTakePictureImageView;
    @BindView(R.id.id_iv_choose_printer)
    ImageView mChoosePrinterImageView;
    @BindView(R.id.id_iv_print)
    ImageView mPrintImageView;
    @BindView(R.id.id_tv_choose_supplier)
    ImageView mChooseSupplierImageView;
    @BindView(R.id.id_tv_choose_device)
    ReSpinner mChooseProductionLine;
    @BindView(R.id.id_tv_weight)
    TextView mWeightTextView;
    @BindView(R.id.id_tv_show_piece_weight)
    TextView mShowPieceWeight;
    @BindView(R.id.id_tv_piece_weight)
    TextView mPieceweightTextView;//单重
    @BindView(R.id.id_tv_sampling)
    TextView mSamplingTextView;//采样
    @BindView(R.id.id_tv_sampling_count)
    TextView mSamplingCount;//采样累计
    @BindView(R.id.id_tv_sampling_number)
    TextView mSamplingNumber;//采样累计数字
    @BindView(R.id.id_tv_cumulative)
    TextView mCumulativeTextView;//累计
    @BindView(R.id.id_tv_deduction)
    TextView mDeductionTextView;//扣重
    @BindView(R.id.id_iv_choose_specs)
    ImageView mChooseSpecsImageView;
    @BindView(R.id.id_iv_setting)
    ImageView mSettingImageView;
    @BindView(R.id.tv_specs)
    TextView mTvSpecs;
    @BindView(R.id.tv_cumulative_weight)
    TextView tvCumulativeWeight;
    @BindView(R.id.tv_cumulative_count)
    TextView tvCumulativeCount;
    @BindView(R.id.id_tv_save_bill)
    TextView mTvSaveBill;
    @BindView(R.id.id_tv_hand)
    TextView mTvHand;//手动计重
    @BindView(R.id.id_tv_is_upload)
    TextView mTvIsUpload;
    @BindView(R.id.id_tv_hand_switch)
    Switch mTvHandSwitch;


    private String mSampling = "(0)";//采样累计默认数字

    private PinBlueReceiver pinBlueReceiver;
    private List<BluetoothBean> mList_b;
    private List<RelayBean> mWifiList;
    SocketListener listener;
    IConnectionManager manager;
    private PulseData mPulseData = new PulseData();
    //蓝牙电子秤
    private BluetoothBean bluetoothScale = new BluetoothBean();
    private RelayAdapter wifiAdapter;
    private SwitchAdapter switchAdapter;


    private BillListFragment billListFragment;
    private DeviceListFragment deviceListFragment;
    private SamplingFragment samplingFragment;
    private SamplingDetailsFragment samplingDetailsFragment;
    private CumulativeFragment cumulativeFragment;
    private DeductionFragment deductionFragment;
    private SettingFragment settingFragment;
    private SaveBillFragment saveBillFragment;

    private Observer<String> stateOB;
    private String isUploadCount = "0/0";
    private int total = 0;
    private int isUpload = 0;
    private boolean isClickable = false;
    private MyToasty myToasty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        initData();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.send(new WriteData(Order.TURN_OFF_ALL));
            manager.unRegisterReceiver(listener);
        }

        if (pinBlueReceiver != null) {
            unregisterReceiver(pinBlueReceiver);
        }

        EventBus.getDefault().unregister(this);
    }

    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(DeviceEvent deviceEvent) {
        Device device = deviceEvent.getDevice();

        if (deviceEvent.isAdd()) {
            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
            count = count + 1;
            tvCumulativeCount.setText(String.valueOf(count));
        }

        if (deviceEvent.getSamplingNumber() >= 0) {
            mSamplingNumber.setText("(" + deviceEvent.getSamplingNumber() + ")");
        }

        if (deviceEvent.getSpecsId() >= 0) {
            Specs specs = new Specs();
            specs = LitePal.find(Specs.class, deviceEvent.getSpecsId());
            mTvSpecs.setText(specs.getSpecs());
        }

        if (deviceEvent.isReset()) {
            mSamplingNumber.setText("(0)");
            tvCumulativeCount.setText("0");
            tvCumulativeWeight.setText("0");

            total = total + 1;
            isUploadCount = isUpload + "/" + total;
            mTvIsUpload.setText(isUploadCount);

        }

        switch (device.getType()) {
            case 0://蓝牙电子秤
                String address = device.getBluetoothMac();
                BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                bluetoothScale = connectAndGetBluetoothScale(bluetoothDevice);
                break;
            case 1://蓝牙继电器
                String address1 = device.getBluetoothMac();
                BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                connectBluetoothRelay(bluetoothDevice1);
                break;
            case 2://Wifi继电器
                connectWifi(device);
                break;

            default:
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
                LoadingPopupView xPopup = new XPopup.Builder(MainActivity.this)
                        .dismissOnTouchOutside(false)
                        .asLoading("正在链接中！");
                @Override
                public void onSubscribe(Disposable d) {
                    xPopup.show();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    isConnect[0] = aBoolean;
                    myToasty.showSuccess(getResources().getString(R.string.Electronic_scale_is_connected));
                }

                @Override
                public void onError(Throwable e) {
                    isConnect[0] = false;
                    myToasty.showError("蓝牙电子秤连接失败！");
                    xPopup.dismiss();
                }

                @Override
                public void onComplete() {
                    xPopup.dismiss();
                    /**
                     * 连接完成之后每隔100毫秒取一次电子秤的数据
                     */
                    getWeight(bluetoothBean);
                }
            });
        }
        if (isConnect[0]) {
            return bluetoothBean;
        } else {
            return null;
        }

    }

    @SuppressLint("CheckResult")
    private void getWeight(BluetoothBean bluetoothBean) {
        /**
         * 连接完成之后每隔100毫秒取一次电子秤的数据
         */
        Flowable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
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
                                    //去掉前面的“=”号和零
                                    String str1 = s.replace("=", "");
                                    String str = str1.replaceFirst("^0*", "");
                                    if (str.startsWith(".")) {
                                        str = "0" + str;
                                    }
                                    mWeightTextView.setText(str);
                                    double weight = Double.parseDouble(str);
                                    double compareWeight = Double.parseDouble(SharedPreferenceUtil
                                            .getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

                                    if (weight > compareWeight && manager != null && manager.isConnect()) {

                                        if (mWifiList.get(0).getState().equals("1")) { //当1号继电器吸和时
                                            Cumulative cumulative = new Cumulative();
                                            cumulative.setCategory("净重");
                                            cumulative.setWeight(str);

                                            if (LitePal.where("hasBill < ?", "0").find(Cumulative.class).size() > 0) {
                                                Cumulative cumulativeLast = LitePal.where("hasBill < ?", "0").findLast(Cumulative.class);
                                                cumulative.setCount(cumulativeLast.getCount() + 1);
                                            } else {
                                                cumulative.setCount(1);
                                            }

                                            cumulative.save();

                                            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
                                            double cWeight = Double.parseDouble(tvCumulativeWeight.getText().toString());

                                            /**
                                             * 相加：b1.add(b2).doubleValue();
                                             * 相减：b1.subtract(b2).doubleValue();
                                             * 相乘：b1.multiply(b2).doubleValue();
                                             */
                                            BigDecimal b1 = new BigDecimal(Double.toString(cWeight));
                                            BigDecimal b2 = new BigDecimal(Double.toString(weight));
                                            cWeight = b1.add(b2).doubleValue();
                                            count = count + 1;

                                            tvCumulativeCount.setText(String.valueOf(count));
                                            tvCumulativeWeight.setText(String.valueOf(cWeight));
                                        }

                                        manager.send(new WriteData(Order.getTurnOff().get(0)));
                                        manager.send(new WriteData(Order.getTurnOn().get(1)));

                                        //两秒之后开关置反
                                        Observable.timer(2, TimeUnit.SECONDS)
                                                .subscribe(new Observer<Long>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onNext(Long aLong) {
                                                        manager.send(new WriteData(Order.getTurnOn().get(0)));
                                                        manager.send(new WriteData(Order.getTurnOff().get(1)));

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
                        initBluetoothRelay();
                        myToasty.showSuccess("蓝牙继电器连接成功！");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    myToasty.showError("蓝牙继电器连接失败！");
                }

                @Override
                public void onComplete() {
                     //bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.TURN_ON_1).subscribe(stateOB);
                    //manager.send(new WriteData(Order.TURN_ON_1));
                }
            });
        }
    }


    //连接Wifi继电器
    private void connectWifi(Device device) {
        //打开Wifi
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            myToasty.showError(getResources().getString(R.string.Please_open_the_wifi));
            Intent it = new Intent();
            ComponentName cn = new ComponentName("com.android.settings","com.android.settings.wifi.WifiSettings");
            it.setComponent(cn);
            startActivity(it);
            return;
        }
        initWifiDevice(device);

        if (manager != null) {
            manager.connect();
        }
    }

    /**
     * 初始化wifi继电器
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
                        myToasty.showError("Wifi继电器连接失败！");

                        break;

                    case IAction.ACTION_CONNECTION_SUCCESS://连接成功
                        myToasty.showSuccess("Wifi继电器连接成功！");
                        Logger.d("mac地址：" + getMacFromArpCache(manager.getRemoteConnectionInfo().getIp()));
                        manager.getPulseManager()
                                .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                                .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
                        manager.send(new WriteData(Order.GET_STATE));
                        //连接成功之后就打开某个开关
                        manager.send(new WriteData(Order.getTurnOn().get(0)));
                        break;
                    case IAction.ACTION_DISCONNECTION:
                        myToasty.showWarning("Wifi继电器连接已断开！");
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
                            String binaryState = StringUtils.hexString2binaryString(state);
                            char[] bin = binaryState.toCharArray();
                            if (bin.length == 8) {
                                for (int i = 0; i < bin.length; i++) {
                                    mWifiList.get(i).setState(bin[i] + "");
                                }
                            }
                            //wifiAdapter.notifyDataSetChanged();
                            switchAdapter.notifyDataSetChanged();
                        } else if (message.getData().indexOf("01 05 00 ") == 0) {
                            //收到状态
                            //第几个继电器
                            String index = message.getData().substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            //继电器状态
                            String state = message.getData().substring("01 05 00 ".length() + 3,
                                    "01 05 00 ".length() + 5);
                            Logger.d("---" + index + "---" + state + "---");

                            /*
                             * 获取继电器各个开关的状态
                             */
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
                            //wifiAdapter.notifyDataSetChanged();
                            switchAdapter.notifyDataSetChanged();
                        }
                        Logger.d("收到:" + message.getData());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (manager != null) {
                    // showToast(e.getMessage());
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onComplete() {

            }
        });
        manager.registerReceiver(listener);
    }


    private void initView() {

        mPrintImageView.setOnClickListener(this);
        mDeductionTextView.setOnClickListener(this);
        mCumulativeTextView.setOnClickListener(this);
        mSamplingCount.setOnClickListener(this);
        mSamplingTextView.setOnClickListener(this);
        mChooseSupplierImageView.setOnClickListener(this);
        mPieceweightTextView.setOnClickListener(this);
        mChoosePrinterImageView.setOnClickListener(this);
        mTakePictureImageView.setOnClickListener(this);
        mChooseMatterImageView.setOnClickListener(this);
        mChooseSpecsImageView.setOnClickListener(this);
        mSettingImageView.setOnClickListener(this);
        mTvSaveBill.setOnClickListener(this);
        mTvHand.setOnClickListener(this);
        mTvHandSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isClickable = b;
            }
        });

        mTvIsUpload.setText(isUploadCount);
        mSamplingNumber.setText(mSampling);
        mShowPieceWeight.setText(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

        if (LitePal.isExist(Cumulative.class) || LitePal.isExist(Deduction.class)) {
            int cumulativeSize = LitePal.where("hasBill < ?", "0").find(Cumulative.class).size();
            int deductionSize = LitePal.where("hasBill < ?","0").find(Deduction.class).size();
            tvCumulativeCount.setText(String.valueOf(cumulativeSize + deductionSize));
        }
        if (LitePal.where("hasBill < ?", "0").find(Cumulative.class).size() > 0) {
            List<Cumulative> cumulatives = LitePal.where("hasBill < ?", "0").find(Cumulative.class);
            String weight = "0";
            for (int i = 0; i < cumulatives.size(); i++) {
                BigDecimal b1 = new BigDecimal(weight);
                BigDecimal b2 = new BigDecimal(cumulatives.get(i).getWeight());
                weight = String.valueOf(b1.add(b2).doubleValue());
            }
            tvCumulativeWeight.setText(weight);
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_style, getDataSource());
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner);
        mChooseProductionLine.setAdapter(spinnerAdapter);
        mChooseProductionLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mTvHand.setVisibility(View.GONE);
                        break;

                    case 1://移动计重

                        mTvHand.setVisibility(View.VISIBLE);
                        startActivity(new Intent(MainActivity.this, ElcScaleActivity.class));
                        break;

                    case 2:
                        mTvHand.setVisibility(View.GONE);
                        if (deviceListFragment == null) {
                            deviceListFragment = new DeviceListFragment();
                        }
                        FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        deviceListFragment.show(ft, "deviceFragment");

                        break;

                    case 3:
                        mTvHand.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        wifiAdapter = new RelayAdapter(R.layout.item_wifi_relay, mWifiList);
        switchAdapter = new SwitchAdapter(R.layout.item_switch, mWifiList);
        switchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (manager != null && manager.isConnect()) {
                    if (isClickable) {
                        switch (view.getId()) {
                            case R.id.iv_switch_left:
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
                            case R.id.iv_switch_right:
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
                    } else {
                        myToasty.showInfo("请先打开手动开关！");
                    }
                } else {
                    myToasty.showInfo("继电器未连接，请先连接继电器！");
                }
            }
        });

        mWifiRelayRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        //mWifiRelayRecyclerView.setAdapter(wifiAdapter);
        mWifiRelayRecyclerView.setAdapter(switchAdapter);

    }

    private void initData() {

        myToasty = new MyToasty(this);
        if (LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size() > 0) {
            mSampling = "(" + LitePal.where("hasBill < ?", "0").findLast(SamplingDetails.class).getCount() + ")";
        }

        if (LitePal.isExist(Bill.class)) {
            total = LitePal.findAll(Bill.class).size();
            //大于等于0，已上传
            isUpload = LitePal.where("isUpload >= ?", "0").find(Bill.class).size();
            isUploadCount = isUpload + "/" + total;
        }


        Bluetooth mBluetooth = new Bluetooth();
        //蓝牙继电器
        mList_b = new ArrayList<>();
        //Wifi继电器
        mWifiList = new ArrayList<>();


        //初始化wifi继电器实体类
        List<Integer> leftIcon = new ArrayList<>(SwitchIcon.getRedIcon());
        List<Integer> rightIcon = new ArrayList<>(SwitchIcon.getGreenIcon());

        for (int i=0;i<leftIcon.size();i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setLeftIamgeView(leftIcon.get(i));
            relayBean.setRightImageView(rightIcon.get(i));
            mWifiList.add(relayBean);

        }

        //打开蓝牙
        if (!mBluetooth.isSupportBlue()) {
            myToasty.showError("当前设备不支持蓝牙！");
            return;
        } else if (!mBluetooth.isBlueEnable()) {
            myToasty.showInfo("请先打开蓝牙！");
            //直接打开
            mBluetooth.openBlueAsyn();
        }
        //注册蓝牙广播
        registerReceiver();
    }


    public List<String> getDataSource() {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.choose_produce_line));
        list.add("移动称重");
        list.add("一号生产线");
        list.add("二号生产线");
        list.add("三号生产线");
        return list;
    }

    //注册蓝牙配对广播接收器
    private void registerReceiver() {

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

        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(pinBlueReceiver, filter4);
        registerReceiver(pinBlueReceiver, filter5);

    }

    //初始化蓝牙继电器
    public void initBluetoothRelay() {
        stateOB = new Observer<String>() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (samplingFragment != null) {
            samplingFragment.onActivityResult(requestCode, resultCode, data);//在DialogFragment中获取回调结果
        }

        //拍照回调
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                GetPicModel picOrMp4 = new GetPicModel();
                picOrMp4 = (GetPicModel) data.getSerializableExtra("result");

            }
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_iv_choose_matter://选择品类
                startActivity(new Intent(MainActivity.this, ChooseMatterActivity.class));
                break;

            case R.id.id_tv_choose_supplier://选择供应商
                startActivity(new Intent(MainActivity.this, ChooseSupplierActivity.class));
                break;

            case R.id.id_iv_choose_specs://选择规格
                startActivity(new Intent(MainActivity.this, ChooseSpecsActivity.class));
                break;

            case R.id.id_tv_sampling://采样

                samplingFragment = SamplingFragment.newInstance(mWeightTextView.getText().toString());
                FragmentTransaction ft1 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//动画 淡入淡出
                samplingFragment.show(ft1, "samplingFragment");
                break;

            case R.id.id_tv_sampling_count://采样累计
                if (samplingDetailsFragment == null) {
                    samplingDetailsFragment = new SamplingDetailsFragment();
                }
                FragmentTransaction ft2 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                samplingDetailsFragment.show(ft2, "samplingDetailsFragment");
                break;

            case R.id.id_tv_cumulative://累计
                if (cumulativeFragment == null) {
                    cumulativeFragment = new CumulativeFragment();
                }
                FragmentTransaction ft3 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                cumulativeFragment.show(ft3, "cumulativeFragment");
                break;

            case R.id.id_tv_deduction://扣重
                deductionFragment = DeductionFragment.newInstance(mWeightTextView.getText().toString());
                FragmentTransaction ft4 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                deductionFragment.show(ft4, "deductionFragment");
                break;

            case R.id.id_iv_choose_printer://选择打印机
                if (deviceListFragment == null) {
                    deviceListFragment = new DeviceListFragment();
                }
                FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                deviceListFragment.show(ft, "deviceFragment");
                break;

            case R.id.id_iv_print://打印
                if (billListFragment == null) {
                    billListFragment = new BillListFragment();
                }
                FragmentTransaction ft5 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                billListFragment.show(ft5, "billListFragment");
                break;

            case R.id.id_tv_piece_weight://单重
                final EditText editText = new EditText(MainActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
                inputDialog.setTitle(R.string.input_piece_weight).setView(editText);
                inputDialog.setPositiveButton(R.string.enter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_PIECE_WEIGHT,
                                        editText.getText().toString().trim());
                                mShowPieceWeight.setText(editText.getText().toString().trim());

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.id_iv_setting://左侧设置界面
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                FragmentTransaction ft7 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft7.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                settingFragment.show(ft7, "settingFragment");
                break;

            case R.id.id_iv_takePicture:
                takePicture();
                break;

            case R.id.id_tv_save_bill://保存单据
                if (saveBillFragment == null) {
                    saveBillFragment = new SaveBillFragment();
                }
                FragmentTransaction ft8 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                ft8.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                saveBillFragment.show(ft8, "saveBillFragment");

                break;

            case R.id.id_tv_hand://手动计重
                double weight = Double.parseDouble(mWeightTextView.getText().toString());
                if (weight > 0d) {
                    Cumulative cumulative = new Cumulative();
                    cumulative.setCategory("净重");
                    cumulative.setWeight(mWeightTextView.getText().toString());

                    if (LitePal.where("hasBill < ?", "0").find(Cumulative.class).size() > 0) {
                        Cumulative cumulativeLast = LitePal.where("hasBill < ?", "0").findLast(Cumulative.class);
                        cumulative.setCount(cumulativeLast.getCount() + 1);

                    } else {
                        cumulative.setCount(1);
                    }

                    cumulative.save();

                    int count = Integer.parseInt(tvCumulativeCount.getText().toString());
                    double cWeight = Double.parseDouble(tvCumulativeWeight.getText().toString());

                    /**浮点数
                     * 相加：b1.add(b2).doubleValue();
                     * 相减：b1.subtract(b2).doubleValue();
                     * 相乘：b1.multiply(b2).doubleValue();
                     */
                    BigDecimal b1 = new BigDecimal(Double.toString(cWeight));
                    BigDecimal b2 = new BigDecimal(mWeightTextView.getText().toString());
                    cWeight = b1.add(b2).doubleValue();
                    count = count + 1;

                    tvCumulativeCount.setText(String.valueOf(count));
                    tvCumulativeWeight.setText(String.valueOf(cWeight));
                } else {
                    myToasty.showWarning("当前重量为零，请勿计重！");
                }


                break;

            default:
                break;
        }
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


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG)));
    }

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


}
