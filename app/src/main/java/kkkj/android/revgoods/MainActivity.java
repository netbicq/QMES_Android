package kkkj.android.revgoods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuhao.didi.core.utils.BytesUtils;

import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kkkj.android.revgoods.adapter.SwitchAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.Master;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.Power;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Sapmle;
import kkkj.android.revgoods.bean.ShowOut;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.SwitchIcon;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.conn.ble.Ble;
import kkkj.android.revgoods.conn.bluetooth.Bluetooth;
import kkkj.android.revgoods.conn.bluetooth.PinBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.PinBlueReceiver;
import kkkj.android.revgoods.conn.classicbt.BleManager;
import kkkj.android.revgoods.conn.classicbt.BluetoothPermissionHandler;
import kkkj.android.revgoods.conn.classicbt.Connect;
import kkkj.android.revgoods.conn.classicbt.listener.BluetoothPermissionCallBack;
import kkkj.android.revgoods.conn.classicbt.listener.ConnectResultlistner;
import kkkj.android.revgoods.conn.classicbt.listener.PinResultListener;
import kkkj.android.revgoods.conn.classicbt.listener.TransferProgressListener;
import kkkj.android.revgoods.conn.socket.CallBack;
import kkkj.android.revgoods.conn.socket.MyOkSocket;
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
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.relay.wifi.model.Order;
import kkkj.android.revgoods.ui.saveBill.SaveBillDetailsActivity;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterActivity;
import kkkj.android.revgoods.ui.chooseSpecs.ChooseSpecsActivity;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierActivity;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import kkkj.android.revgoods.utils.StringUtils;


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
    @BindView(R.id.tv_matter)
    TextView mTvMatter;
    @BindView(R.id.tv_supplier)
    TextView mTvSupplier;

    private CompositeDisposable compositeDisposable;

    private String mSampling = "(0)";//采样累计默认数字

    private PinBlueReceiver pinBlueReceiver;
    private List<BluetoothBean> mList_b;
    private List<RelayBean> mWifiList;
    private ISocketActionListener listener;
    private IConnectionManager manager;

    private Connect bluetoothManager;
    private BluetoothPermissionHandler permissionHandler;

    //蓝牙电子秤
    private BluetoothBean bluetoothScale;
    private RelayAdapter wifiAdapter;
    private SwitchAdapter switchAdapter;
    //蓝牙继电器
    private BluetoothBean bluetoothRelay;

    private BluetoothAdapter mBluetoothAdapter;

    //生产线
    private List<String> produceLineList;
    private List<ProduceLine> produceLines;
    //是否配置了采样的秤
    private boolean isSetSapmle = false;
    //采样秤Mac地址
    private String sampleMacAddress;
    private ArrayAdapter spinnerAdapter;

    //Wifi继电器的连接状态
    private boolean wifiConnectionState = false;
    //蓝牙继电器的连接状态
    private boolean blueRelayConnectionState = false;
    //蓝牙主电子秤的连接状态
    private boolean blueMainScaleConnectionState = false;
    //蓝牙采样电子秤的连接状态
    private boolean blueSamplingScaleConnectionState = false;
    //蓝牙Ble显示屏的连接状态
    private boolean bleScreenConnectionState = false;

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

    private Supplier supplier;
    private Matter matter;
    private Specs specs;
    private int supplierId;
    private int matterId;
    private int specsId;

    private String samplingWeight = "";

    private static final String SAMPLING = "samplingFragment";
    private static final String SAMPLING_DETAILS = "samplingDetailsFragment";
    private static final String CUMULATIVE = "cumulativeFragment";
    private static final String DEDUCTION = "deductionFragment";
    private static final String DEVICE_LIST = "deviceFragment";
    private static final String BILL_LIST = "billListFragment";
    private static final String SETTING = "settingFragment";
    private static final String SAVE_BILL = "saveBillFragment";

    //继电器控制的开关
    private int inLine;
    private int outLine;

    /**
     * 继电器连接类型
     * 1-Wifi继电器
     * 2-蓝牙继电器
     */
    private static int CONNECT_TYPE = -1;

    private QMUITipDialog qmuiTipDialog;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //打开Wifi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            myToasty.showError(getResources().getString(R.string.Please_open_the_wifi));
            Intent it = new Intent();
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
            it.setComponent(cn);
            startActivity(it);
            return;
        }

        initBlue();
        initData();
        initView();

    }

    private void initBlue() {

        permissionHandler = new BluetoothPermissionHandler(this, new BluetoothPermissionCallBack() {
            @Override
            public void onBlueToothEnabled() {
                BleManager.getInstance().init(getApplicationContext());
                //前台服务
                BleManager.getInstance().setForegroundService(true);
            }

            @Override
            public void permissionFailed() {

            }
        });
        permissionHandler.start();
    }


    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(DeviceEvent deviceEvent) {
        Device device = deviceEvent.getDevice();
        //更新累计数+1
        if (deviceEvent.isAdd()) {
            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
            count = count + 1;
            tvCumulativeCount.setText(String.valueOf(count));
        }
        //更新采样数
        if (deviceEvent.getSamplingNumber() >= 0) {
            mSamplingNumber.setText("(" + deviceEvent.getSamplingNumber() + ")");
        }
        //更新供应商
        if (deviceEvent.getSupplierId() >= 0) {
            supplierId = deviceEvent.getSupplierId();
            supplier = LitePal.find(Supplier.class, supplierId);
            mTvSupplier.setText(supplier.getName());
            //刷新生产线
            produceLineList.clear();
            produceLines.clear();

            produceLineList.add(getResources().getString(R.string.choose_produce_line));
            produceLineList.add("移动称重");

            produceLines = LitePal.findAll(ProduceLine.class);
            for (int i = 0; i < produceLines.size(); i++) {
                produceLineList.add(produceLines.get(i).getName());
            }
            spinnerAdapter.notifyDataSetChanged();

        }
        //更新品类（物料）
        if (deviceEvent.getMatterId() >= 0) {
            matter = new Matter();
            matterId = deviceEvent.getMatterId();
            matter = LitePal.find(Matter.class, matterId);
            mTvMatter.setText(matter.getName());
        }
        //更新规格
        if (deviceEvent.getSpecsId() >= 0) {
            specs = new Specs();
            specsId = deviceEvent.getSpecsId();
            specs = LitePal.find(Specs.class, specsId);
            mTvSpecs.setText(specs.getName());
        }

        //保存单据后数据重置
        if (deviceEvent.isReset()) {
            mSamplingNumber.setText("(0)");
            tvCumulativeCount.setText("0");
            tvCumulativeWeight.setText("0");

            total = total + 1;
            //大于等于0，已上传
            isUpload = LitePal.where("isUpload >= ?", "0").find(Bill.class).size();

            isUploadCount = isUpload + "/" + total;
            mTvIsUpload.setText(isUploadCount);

        }
        //删除单据后更新
        if (deviceEvent.isResetUploadCount()) {
            total = total - 1;
            //大于等于0，已上传
            isUpload = LitePal.where("isUpload >= ?", "0").find(Bill.class).size();
            isUploadCount = isUpload + "/" + total;
            mTvIsUpload.setText(isUploadCount);
        }

        if (device.getType() >= 0) {
            switch (device.getType()) {
                case 0://蓝牙电子秤
                    String address = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        //connectAndGetBluetoothScale(bluetoothDevice);
                        connect(bluetoothDevice);
                    } else {
                        BleManager.getInstance().pin(bluetoothDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
                                connect(device);
                                //connectAndGetBluetoothScale(device);
                            }
                        });
                    }

                    break;
                case 1://蓝牙继电器
                    CONNECT_TYPE = 2;
                    String address1 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                    connectBluetoothRelay(bluetoothDevice1);
                    break;
                case 2://Wifi继电器
                    CONNECT_TYPE = 1;
                    connectWifi(device);
                    break;

                case 3://采样连接的电子秤
                    String address2 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice2 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address2);
                    connectAndGetBluetoothScale(bluetoothDevice2);
                    break;

                case 4://蓝牙Ble显示屏 "D8:E0:4B:FD:31:F6"
                    String addressBle = device.getBluetoothMac();
                    BluetoothDevice bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
                    connectBle(bleDevice);

                default:
                    break;
            }
        }

    }

    //蓝牙Ble显示屏
    private void connectBle(BluetoothDevice bleDevice) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Ble ble = new Ble(bleDevice, this);
        bleScreenConnectionState = ble.connect();
    }

    //蓝牙电子秤
    private void connect(BluetoothDevice device) {
        qmuiTipDialog.show();

        BleManager.getInstance().connect(device, new ConnectResultlistner() {
            @Override
            public void connectSuccess(Connect connect) {
                bluetoothManager = connect;
                blueMainScaleConnectionState = true;
                qmuiTipDialog.dismiss();
                //myToasty.showSuccess(getResources().getString(R.string.Electronic_scale_is_connected));
                SmartToast.successLong(R.string.Electronic_scale_is_connected);
                final boolean[] isWrite = {false};
                bluetoothManager.read(new TransferProgressListener() {
                    @Override
                    public void transfering(int progress) {

                    }

                    @Override
                    public void transferSuccess(String str) {
                        //String s = new String(bytes);
                        Logger.d("原始数据：" + str);
                        //String str = new String(s.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
                        if (str.length() == 8) {
                            //取反
                            str = new StringBuilder(str).reverse().toString();
                            Logger.d("处理之前的数据：" + str);
                            if (!str.endsWith("=") && !str.startsWith("=") && str.substring(5, 6).equals(".")) {
                                //去掉前面的“=”号和零
                                str = str.replaceFirst("^0*", "");
                                if (str.startsWith(".")) {
                                    str = "0" + str;
                                }
                                Logger.d("读取到的数据：" + str);
                                mWeightTextView.setText(str);
                                double weight = Double.parseDouble(str);
                                double compareWeight = Double.parseDouble(SharedPreferenceUtil
                                        .getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

                                switch (CONNECT_TYPE) {
                                    //Wifi继电器
                                    case 1:
                                        if (weight > compareWeight && manager != null && manager.isConnect()) {

                                            if (!isWrite[0]) {
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
                                                BigDecimal b2 = new BigDecimal(str);
                                                cWeight = b1.add(b2).doubleValue();
                                                count = count + 1;

                                                tvCumulativeCount.setText(String.valueOf(count));
                                                tvCumulativeWeight.setText(String.valueOf(cWeight));
                                            }

                                            manager.send(new WriteData(Order.getTurnOff().get(inLine)));
                                            isWrite[0] = true;
                                            //100毫秒之后打开2号开关
                                            Observable.timer(100, TimeUnit.MILLISECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            manager.send(new WriteData(Order.getTurnOn().get(outLine)));
                                                        }
                                                    });

                                            //两秒之后开关置反
                                            Observable.timer(2, TimeUnit.SECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            manager.send(new WriteData(Order.getTurnOn().get(inLine)));
                                                            isWrite[0] = false;

                                                            //间隔100毫秒之后关掉2号开关
                                                            Observable.timer(100, TimeUnit.MILLISECONDS)
                                                                    .subscribe(new Consumer<Long>() {
                                                                        @Override
                                                                        public void accept(Long aLong) throws Exception {
                                                                            manager.send(new WriteData(Order.getTurnOff().get(outLine)));
                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }

                                        break;
                                    //蓝牙继电器
                                    case 2:

                                        if (weight > compareWeight && bluetoothRelay.getMyBluetoothManager().isConnect()) {

                                            if (!isWrite[0]) {
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
                                                BigDecimal b2 = new BigDecimal(str);
                                                cWeight = b1.add(b2).doubleValue();
                                                count = count + 1;

                                                tvCumulativeCount.setText(String.valueOf(count));
                                                tvCumulativeWeight.setText(String.valueOf(cWeight));
                                            }

                                            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(inLine)).subscribe(stateOB);
                                            isWrite[0] = true;
                                            //100毫秒之后打开2号开关
                                            Observable.timer(100, TimeUnit.MILLISECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(outLine)).subscribe(stateOB);
                                                        }
                                                    });

                                            //两秒之后开关置反
                                            Observable.timer(2, TimeUnit.SECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                                                            isWrite[0] = false;

                                                            //间隔100毫秒之后关掉2号开关
                                                            Observable.timer(100, TimeUnit.MILLISECONDS)
                                                                    .subscribe(new Consumer<Long>() {
                                                                        @Override
                                                                        public void accept(Long aLong) throws Exception {
                                                                            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(outLine)).subscribe(stateOB);
                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }

                                        break;

                                }


                            }
                        }
                    }

                    @Override
                    public void transferFailed(Exception exception) {
                        if (!"socket closed".equals(exception.getMessage())) {
                            myToasty.showError("蓝牙电子秤读取数据失败,请尝试重连！");
                        }
                    }
                });

            }

            @Override
            public void connectFailed(Exception e) {
                qmuiTipDialog.dismiss();
                //myToasty.showError(getResources().getString(R.string.connect_failed));
                SmartToast.errorLong(R.string.connect_failed);
            }

            @Override
            public void disconnected() {
                //myToasty.showWarning(getResources().getString(R.string.bluetooth_diconnected));
                SmartToast.errorLong(R.string.bluetooth_diconnected);
            }
        });
    }


    //蓝牙采样电子秤
    private void connectAndGetBluetoothScale(BluetoothDevice bluetoothDevice) {
        BluetoothBean bluetoothBean = new BluetoothBean();
        bluetoothBean.setBluetoothDevice(bluetoothDevice);

        if (bluetoothBean.getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
            bluetoothBean.getMyBluetoothManager().pin();
        } else {
            bluetoothScale = bluetoothBean;
            bluetoothBean.getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
                QMUITipDialog qmuiTipDialog = new QMUITipDialog.Builder(MainActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在链接...")
                        .create();

                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                    qmuiTipDialog.show();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    myToasty.showSuccess(getResources().getString(R.string.Electronic_scale_is_connected));
                    blueSamplingScaleConnectionState = true;
                }

                @Override
                public void onError(Throwable e) {
                    myToasty.showError("蓝牙电子秤连接失败,请尝试重连！");
                    qmuiTipDialog.dismiss();
                }

                @Override
                public void onComplete() {
                    qmuiTipDialog.dismiss();
                    /**
                     * 连接完成之后每隔100毫秒取一次电子秤的数据
                     */
                    getWeight(bluetoothBean);
                }
            });
        }
    }

    @SuppressLint("CheckResult")
    private void getWeight(BluetoothBean bluetoothBean) {
        /**
         * 连接完成之后每隔100毫秒取一次电子秤的数据
         */
        Flowable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return bluetoothBean.getMyBluetoothManager().isConnect();
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        bluetoothBean.getMyBluetoothManager().getReadOB().subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                if (!bluetoothBean.getMyBluetoothManager().isConnect()) {
                                    Logger.d("蓝牙断开");
                                    compositeDisposable.add(d);
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
                                    samplingWeight = str;

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
            if (blueRelayConnectionState) {
                return;
            }
            qmuiTipDialog.show();
            bluetoothRelay = bluetoothBean;
            bluetoothBean.getMyBluetoothManager().getConnectOB().subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        qmuiTipDialog.dismiss();
                        initBluetoothRelay();
                        myToasty.showSuccess("蓝牙继电器连接成功！");
                        blueRelayConnectionState = true;
                        //打开某个开关
                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    myToasty.showError("蓝牙继电器连接失败！");
                    qmuiTipDialog.dismiss();
                }

                @Override
                public void onComplete() {
                    //bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(stateOB);
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
        }
    }

    /**
     * 初始化wifi继电器
     */
    private void initWifiDevice(Device device) {
        String ip = device.getWifiIp();
        int port = device.getWifiPort();
        MyOkSocket okSocket = new MyOkSocket(ip, port, new CallBack() {
            @Override
            public void onConnectionSuccess() {
                myToasty.showSuccess("Wifi继电器连接成功！");
                wifiConnectionState = true;
                manager.send(new WriteData(Order.GET_STATE));
                //连接成功之后就打开某个开关
                manager.send(new WriteData(Order.getTurnOn().get(inLine)));
//                manager.send(new WriteData(Order.getTurnOn().get(1)));
//                manager.send(new WriteData(Order.getTurnOn().get(2)));
//                manager.send(new WriteData(Order.getTurnOn().get(3)));


            }

            @Override
            public void onReceived(String msg) {
                if (msg.indexOf("01 01 02 ") == 0) {
                    //收到状态
                    String state = msg.substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                    String binaryState = StringUtils.hexString2binaryString(state);
                    char[] bin = binaryState.toCharArray();
                    if (bin.length == 8) {
                        for (int i = 0; i < bin.length; i++) {
                            mWifiList.get(i).setState(bin[i] + "");
                        }
                    }
                } else if (msg.indexOf("01 05 00 ") == 0) {
                    //收到状态
                    /**
                     * 01 05 00 0X XX 00
                     * 1.01 05 00 固定值
                     * 2.0X  X:代表几号开关
                     * 3.XX  两种取值：00代表断开  FF代表吸和
                     */
                    // 0X 第几个开关
                    String index = msg.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                    //继电器状态
                    String state = msg.substring("01 05 00 ".length() + 3,
                            "01 05 00 ".length() + 5);
                    Logger.d("---" + index + "---" + state + "---");
                    //第几号开关
                    int whichSwitch = Integer.parseInt(index.substring(1, 2));
                    /*
                     * 设置继电器各个开关的状态
                     */
                    if (state.equals("00")) {
                        mWifiList.get(whichSwitch).setState("0");
                    } else {
                        mWifiList.get(whichSwitch).setState("1");
                    }
                }
                switchAdapter.notifyDataSetChanged();
                Logger.d("收到:" + msg);
            }

            @Override
            public void onDisconnection(Exception e) {
                myToasty.showWarning("Wifi继电器连接已断开！");
            }

            @Override
            public void onConnectionFailed(Exception e) {
                myToasty.showError("Wifi继电器连接失败！");
            }
        });

        manager = okSocket.getManager();
        listener = okSocket.getiSocketActionListener();

    }


    private void initView() {

        qmuiTipDialog = new QMUITipDialog.Builder(MainActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getResources().getString(R.string.is_connecting))
                .create();

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
        mTvHandSwitch.setOnCheckedChangeListener((compoundButton, b) -> isClickable = b);

        mTvIsUpload.setText(isUploadCount);
        mSamplingNumber.setText(mSampling);
        mShowPieceWeight.setText(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

        if (LitePal.isExist(Cumulative.class) || LitePal.isExist(Deduction.class)) {
            int cumulativeSize = LitePal.where("hasBill < ?", "0").find(Cumulative.class).size();
            int deductionSize = LitePal.where("hasBill < ?", "0").find(Deduction.class).size();
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

        spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_style, produceLineList);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner);
        mChooseProductionLine.setAdapter(spinnerAdapter);
        mChooseProductionLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mTvHand.setVisibility(View.GONE);
                    return;
                }
                if (position == 1) {//移动计重
                    mTvHand.setVisibility(View.VISIBLE);
                    startActivity(new Intent(MainActivity.this, ElcScaleActivity.class));
                    return;
                }

                if (SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT).length() == 0) {
                    myToasty.showInfo("请先配置单重！");
                    return;
                }

                mTvHand.setVisibility(View.GONE);

                ProduceLine produceLine = produceLines.get(position - 2);

                //主秤
                String masterString = produceLine.getMaster();
                Master master = new Gson().fromJson(masterString, Master.class);
                String address = master.getDeviceAddr();
                if (address == null) {
                    myToasty.showInfo("当前未配置收料秤，请前往网页端配置！");
                    return;
                }
                address = address.trim();
                Logger.d( "------------>"  + address);

                try {
                    BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        //connectAndGetBluetoothScale(bluetoothDevice);
                        if (!blueMainScaleConnectionState) {
                            connect(bluetoothDevice);
                        }

                    } else {
                        BleManager.getInstance().pin(bluetoothDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
                                connect(device);
                                //connectAndGetBluetoothScale(device);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    myToasty.showInfo(e.getMessage());
                }

                //继电器
                String powerString = produceLine.getPower();
                Power power = new Gson().fromJson(powerString, Power.class);
                if (power.getDeviceAddr() == null) {
                    myToasty.showInfo("当前未配置继电器，请前往网页端配置！");
                    return;
                }

                inLine = power.getInLine() - 1;
                outLine = power.getOutLine() - 1;

                Logger.d("inLine:" + inLine );
                Logger.d("outLine:" + outLine );

                switch (power.getDeviceType()) {
                    case 1://蓝牙继电器
                        CONNECT_TYPE = 2;
                        String address1 = power.getDeviceAddr().trim();
                        try {
                            BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                            connectBluetoothRelay(bluetoothDevice1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myToasty.showInfo(e.getMessage());
                        }
                        break;

                    case 2://Wifi继电器
                        CONNECT_TYPE = 1;
                        //分割 ：
                        String[] strarr = power.getDeviceAddr().split(":");
                        String ip = strarr[0].trim();
                        int port = Integer.valueOf(strarr[1]);
                        Device device = new Device();
                        device.setWifiIp(ip);
                        device.setWifiPort(port);
                        connectWifi(device);
                        break;

                    default:
                        break;
                }

                //采样秤
                String sapmleString = produceLine.getSapmle();
                Sapmle sapmle = new Gson().fromJson(sapmleString,Sapmle.class);
                //是否配置了采样的秤
                isSetSapmle = (sapmle.getDeviceAddr() != null);
                if (isSetSapmle) {
                    sampleMacAddress = sapmle.getDeviceAddr().trim();
                }

                //显示屏 蓝牙Ble设备
                String showOutString = produceLine.getShowOut();
                ShowOut showOut = new Gson().fromJson(showOutString,ShowOut.class);
                if (!(showOut.getDeviceAddr() == null)){
                    String addressBle = showOut.getDeviceAddr().trim();
                    try {
                        BluetoothDevice bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
                        connectBle(bleDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myToasty.showInfo(e.getMessage());
                    }
                }else {
                    myToasty.showInfo("当前未配置显示屏！");
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

                switch (CONNECT_TYPE) {
                    /**
                     * Wifi继电器
                     */
                    case 1:
                        if (manager != null && manager.isConnect()) {
                            if (isClickable) {
                                switch (view.getId()) {
                                    case R.id.iv_switch_left:
                                        manager.send(new WriteData((Order.getTurnOn().get(position))));
                                        break;

                                    case R.id.iv_switch_right:
                                        manager.send(new WriteData(Order.getTurnOff().get(position)));
                                        break;
                                }
                            } else {
                                myToasty.showInfo("请先打开手动开关！");
                            }
                        } else {
                            myToasty.showInfo("继电器未连接，请先连接继电器！");
                        }
                        break;

                    case 2:
                        /**
                         * 蓝牙继电器
                         */
                        if (bluetoothRelay != null && bluetoothRelay.isConnect()) {

                            if (isClickable) {

                                switch (view.getId()) {
                                    case R.id.iv_switch_left:
                                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(position)).subscribe(stateOB);

                                        break;

                                    case R.id.iv_switch_right:
                                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(position)).subscribe(stateOB);

                                        break;

                                    default:
                                        break;
                                }
                            } else {
                                myToasty.showInfo("请先打开手动开关！");
                            }

                        } else {
                            myToasty.showInfo("继电器未连接，请先连接继电器！");
                        }

                        break;

                    default:
                        break;
                }


            }
        });

        mWifiRelayRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        //mWifiRelayRecyclerView.setAdapter(wifiAdapter);
        mWifiRelayRecyclerView.setAdapter(switchAdapter);

    }

    private void initData() {
        //生产线
        produceLineList = new ArrayList<>();
        produceLineList.add(getResources().getString(R.string.choose_produce_line));
        produceLineList.add("移动称重");

        produceLines = LitePal.findAll(ProduceLine.class);
        for (int i = 0; i < produceLines.size(); i++) {
            produceLineList.add(produceLines.get(i).getName());
        }

        compositeDisposable = new CompositeDisposable();

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

        for (int i = 0; i < leftIcon.size(); i++) {
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
            //直接打开
            mBluetooth.openBlueAsyn();
        }
        //注册蓝牙广播
        registerReceiver();
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
//                switch (device.getAddress()) {
//                    case "20:17:03:15:05:65"://电子秤
//                        //connectAndGetBluetoothScale(device);
//                        break;
//
//                    case "00:0D:1B:00:13:BA":
//                        connectBluetoothRelay(device);//继电器
//                        break;
//
//                    default:
//                        break;
//                }

                myToasty.showInfo("绑定成功，请点击重连！");

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
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {

                bluetoothRelay.getMyBluetoothManager().getReadOBModbus().subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
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
                            switchAdapter.notifyDataSetChanged();

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

        //bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.GET_STATE).subscribe(stateOB);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_iv_choose_matter://选择品类
                if (supplier != null) {
                    Intent intent = new Intent(MainActivity.this, ChooseMatterActivity.class);
                    startActivity(intent);
                } else {
                    myToasty.showWarning("请先选择供应商！");
                }

                break;

            case R.id.id_tv_choose_supplier://选择供应商
                startActivity(new Intent(MainActivity.this, ChooseSupplierActivity.class));
                break;

            case R.id.id_iv_choose_specs://选择规格
                if (matter != null) {
                    Intent intent = ChooseSpecsActivity.newIntent(MainActivity.this, String.valueOf(matter.getId()));
                    startActivity(intent);
                } else {
                    myToasty.showWarning("请先选择品类！");
                }
                break;

            case R.id.id_tv_sampling://采样

                if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) { //已连接
                //已连接
                if (supplier != null && matter != null) {
                    samplingFragment = SamplingFragment.newInstance(samplingWeight, supplierId, matterId);
                    showDialogFragment(samplingFragment, SAMPLING);
                } else {
                    myToasty.showWarning("请先选择供应商，品类，规格！");
                }

                } else {//未连接

                    if (isSetSapmle) {//是否配置了采样秤
                        try {
                            BluetoothDevice bluetoothDevice2 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(sampleMacAddress);
                            connectAndGetBluetoothScale(bluetoothDevice2);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myToasty.showInfo(e.getMessage());
                        }
                    } else {
                        myToasty.showInfo("未配置采样秤，请手动连接！");
                        startActivity(new Intent(MainActivity.this, ElcScaleActivity.class));
                    }
                }

                break;


            case R.id.id_tv_sampling_count://采样累计
                if (samplingDetailsFragment == null) {
                    samplingDetailsFragment = new SamplingDetailsFragment();
                }
                showDialogFragment(samplingDetailsFragment, SAMPLING_DETAILS);
                break;

            case R.id.id_tv_cumulative://累计
                if (cumulativeFragment == null) {
                    cumulativeFragment = new CumulativeFragment();
                }
                showDialogFragment(cumulativeFragment, CUMULATIVE);
                break;

            case R.id.id_tv_deduction://扣重
                deductionFragment = DeductionFragment.newInstance(mWeightTextView.getText().toString());
                showDialogFragment(deductionFragment, DEDUCTION);
                break;

            case R.id.id_iv_choose_printer://选择打印机
//                if (deviceListFragment == null) {
//                    deviceListFragment = new DeviceListFragment();
//                }
//                showDialogFragment(deviceListFragment, DEVICE_LIST);
                break;

            case R.id.id_iv_print://打印
                if (billListFragment == null) {
                    billListFragment = new BillListFragment();
                }
                showDialogFragment(billListFragment, BILL_LIST);
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
                showDialogFragment(settingFragment, SETTING);
                break;

            case R.id.id_iv_takePicture:
                takePicture();
                break;

            case R.id.id_tv_save_bill://保存单据

                if (LitePal.where("hasBill < ?", "0")
                        .find(SamplingDetails.class, true).size() <= 0) {

                    myToasty.showWarning("请先先采样确定单价！");
                    return;
                }

//                if (Double.valueOf(tvCumulativeWeight.getText().toString().trim()) <= 0) {
//                    myToasty.showWarning("当前未称重！");
//                    return;
//                }

                final EditText editText1 = new EditText(MainActivity.this);
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(MainActivity.this);
                inputDialog1.setTitle("请输入扣重率（%）").setView(editText1);
                inputDialog1.setPositiveButton(R.string.enter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText1.getText().toString().trim().length() == 0) {
                                    myToasty.showInfo("请输入扣重率");
                                    return;
                                }
                                //扣重率
                                int deduction = Integer.valueOf(editText1.getText().toString().trim());
                                Intent intent = SaveBillDetailsActivity.newInstance(MainActivity.this,
                                        deduction,tvCumulativeWeight.getText().toString().trim());

                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

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

    private void showDialogFragment(DialogFragment dialogFragment, final String Tag) {
        //清除已经存在的，相同的fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //动画，淡入淡出
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Tag);
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, Tag);
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
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.send(new WriteData(Order.TURN_OFF_ALL));
            manager.unRegisterReceiver(listener);
        }

        if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);
            bluetoothRelay.getMyBluetoothManager().disConnect();
        }

        if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) {
            bluetoothScale.getMyBluetoothManager().disConnect();
        }

        if (pinBlueReceiver != null) {
            unregisterReceiver(pinBlueReceiver);
        }

        if (compositeDisposable != null && compositeDisposable.size() > 0) {
            compositeDisposable.dispose();
        }

        if (BleManager.getInstance() != null && bluetoothManager != null) {
            BleManager.getInstance().destory();
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHandler.onActivityResult(requestCode, resultCode, data);

//        if (samplingFragment != null) {
//            samplingFragment.onActivityResult(requestCode, resultCode, data);//在DialogFragment中获取回调结果
//        }

        //拍照回调
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                GetPicModel picOrMp4 = new GetPicModel();
                picOrMp4 = (GetPicModel) data.getSerializableExtra("result");

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
