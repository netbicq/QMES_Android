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
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.suke.widget.SwitchButton;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kkkj.android.revgoods.adapter.SwitchAdapter;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.bean.Master;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Power;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Sapmle;
import kkkj.android.revgoods.bean.ShowOut;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.SwitchIcon;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.conn.ControlRelay;
import kkkj.android.revgoods.conn.ble.BleCallback;
import kkkj.android.revgoods.conn.ble.BleTest;
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
import kkkj.android.revgoods.customer.CircleTextView;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.elcscale.view.ElcScaleActivity;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.fragment.BillListFragment;
import kkkj.android.revgoods.fragment.CumulativeFragment;
import kkkj.android.revgoods.fragment.DeductionFragment;
import kkkj.android.revgoods.fragment.ProduceLineListFragment;
import kkkj.android.revgoods.fragment.SamplingDetailsFragment;
import kkkj.android.revgoods.fragment.SamplingFragment;
import kkkj.android.revgoods.fragment.SettingFragment;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.relay.wifi.model.Order;
import kkkj.android.revgoods.ui.ChooseMatterLevelActivity;
import kkkj.android.revgoods.ui.DeviceListActivity;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterActivity;
import kkkj.android.revgoods.ui.chooseSpecs.ChooseSpecsActivity;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierActivity;
import kkkj.android.revgoods.ui.saveBill.SaveBillDetailsActivity;
import kkkj.android.revgoods.utils.CRC16Util;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.QMUIUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import kkkj.android.revgoods.utils.StringUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.id_wifiRelay_recyclerView)
    RecyclerView mWifiRelayRecyclerView;
    @BindView(R.id.id_iv_choose_matter)
    TextView mChooseMatterImageView;
    @BindView(R.id.id_iv_takePicture)
    TextView mTakePictureImageView;
    @BindView(R.id.id_iv_print)
    TextView mPrintImageView;
    @BindView(R.id.id_tv_choose_supplier)
    TextView mChooseSupplierImageView;
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
    @BindView(R.id.id_tv_cumulative)
    TextView mCumulativeTextView;//累计
    @BindView(R.id.id_tv_deduction)
    TextView mDeductionTextView;//扣重
    @BindView(R.id.id_iv_choose_specs)
    TextView mChooseSpecsImageView;
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
    @BindView(R.id.id_tv_hand_switch)
    SwitchButton mTvHandSwitch;
    @BindView(R.id.tv_matter)
    TextView mTvMatter;
    @BindView(R.id.tv_supplier)
    TextView mTvSupplier;
    @BindView(R.id.tv_matter_level)
    TextView mTvMatterLevel;
    @BindView(R.id.id_iv_choose_matter_level)
    TextView mIvChooseMatterLevel;
    @BindView(R.id.tv_produce_line)
    TextView tvProduceLine;
    @BindView(R.id.id_tv_deductionMix)
    TextView mTvDeductionMix;
    @BindView(R.id.id_tv_deduction_cumulative)
    TextView mTvDeductionCumulative;
    @BindView(R.id.tv_out_line)
    TextView tvOutLine;
    @BindView(R.id.tv_in_line)
    TextView tvInLine;
    @BindView(R.id.tv_third)
    TextView tvThird;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.tv_first)
    TextView tvFirst;
    @BindView(R.id.id_constraintLayout)
    ConstraintLayout mConstraintLayout;

    private CompositeDisposable compositeDisposable;

    private String mSampling = "(0)";//采样累计默认数字
    private int samplingSize;//采样累计的数量

    private PinBlueReceiver pinBlueReceiver;

    private List<RelayBean> mWifiList;
    private ISocketActionListener listener;
    private IConnectionManager manager;

    private Connect bluetoothManager;
    private BluetoothPermissionHandler permissionHandler;

    //蓝牙电子秤
    private BluetoothBean bluetoothScale;

    private SwitchAdapter switchAdapter;
    //蓝牙继电器
    private BluetoothBean bluetoothRelay;
    //蓝牙显示屏
    private BluetoothAdapter mBluetoothAdapter;


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
    //蓝牙Ble设备
//    private Ble ble;
    private BleTest bleTest;
    //是否已断开显示屏
    private boolean isDisconnectScreen;
    //间隔时间
    private int intervalTime;
    //扣重率
    private int deductionMix = 0;

    private Observable<Double> observablePieceWeight;

    //收料秤
    private BluetoothDevice masterDevice;
    //蓝牙继电器
    private BluetoothDevice bluetoothRelayDevice;
    //Wifi继电器
    private Device wifiRelayDevice;
    //采样电子秤
    private BluetoothDevice samplingDevice;
    //显示屏
    private BluetoothDevice showOutDevice;

    private Observer<String> stateOB;
    private String isUploadCount = "0/0";

    private boolean isClickable = false;
    private MyToasty myToasty;

    private Supplier supplier;
    private Matter matter;
    private MatterLevel matterLevel;
    private Specs specs;
    private int supplierId;
    private int matterId;
    private int matterLevelId;
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
    private static final String PRODUCE_LINE_LIST = "produceLineListFragment";


    //继电器控制的开关
    private int inLine;
    private int outLine;
    //生产线
    private ProduceLine produceLine;

    /**
     * 继电器连接类型
     * 1-Wifi继电器
     * 2-蓝牙继电器
     */
    private static int CONNECT_TYPE = -1;

    /**
     * 采样模式
     * 1.手动填写重量
     * 2.连接附近蓝牙电子秤
     */
    private static int SAMPLING_MODE = 0;

    /**
     * 计重模式
     * 全手动模式
     */
    private boolean isByHandOnly = false;

    private QMUITipDialog qmuiTipDialog;

    private MediaPlayer mediaPlayer;
    //播放 assets/saveweight.wav 音乐文件
    private AssetFileDescriptor fd;

    private TextToSpeech tts;

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


        CircleTextView circleTextView = findViewById(R.id.textView29);
        circleTextView.setClickListener(new CircleTextView.onClickListener() {
            @Override
            public void onClick() {
                QMUIUtils.showNormalPopup(MainActivity.this,circleTextView,QMUIPopup.DIRECTION_BOTTOM,"你说什么就是什么！");
            }
        });

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
        mIvChooseMatterLevel.setOnClickListener(this);
        mPieceweightTextView.setOnClickListener(this);
        mTakePictureImageView.setOnClickListener(this);
        mChooseMatterImageView.setOnClickListener(this);
        mChooseSpecsImageView.setOnClickListener(this);
        mSettingImageView.setOnClickListener(this);
        mTvSaveBill.setOnClickListener(this);
        mTvHand.setOnClickListener(this);
        tvProduceLine.setOnClickListener(this);
        mTvDeductionMix.setOnClickListener(this);
        mTvDeductionCumulative.setOnClickListener(this);
        mTvHandSwitch.setOnCheckedChangeListener((compoundButton, b) -> isClickable = b);

//        mShowPieceWeight.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mShowPieceWeight.setText(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

        //供应商
        if (supplier != null) {
            //单据未保存退出，重新打开时
            mTvSupplier.setText(supplier.getName());
        }

        //品类等级
        matterLevel = new MatterLevel();
        matterLevelId = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_MATTER_LEVEL, -1);
        if (matterLevelId > 0) {
            matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
            mTvMatterLevel.setText(matterLevel.getName());
        }
        //品类
        matter = new Matter();
        matterId = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_MATTER, -1);
        if (matterId > 0) {
            matter = LitePal.find(Matter.class, matterId);
            mTvMatter.setText(matter.getName());
        }

        if (LitePal.where("hasBill < ?", "0").find(Cumulative.class).size() > 0) {
            List<Cumulative> cumulatives = LitePal.where("hasBill < ?", "0").find(Cumulative.class);
            int cumulativeSize = cumulatives.size();
            String weight = "0";
            for (int i = 0; i < cumulatives.size(); i++) {
                BigDecimal b1 = new BigDecimal(weight);
                BigDecimal b2 = new BigDecimal(cumulatives.get(i).getWeight());
                weight = String.valueOf(b1.add(b2).doubleValue());
            }
            tvCumulativeCount.setText(String.valueOf(cumulativeSize));
            tvCumulativeWeight.setText(weight);
        }


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
                                myToasty.showWarning("请先打开手动开关！");
                            }

                        } else {
                            myToasty.showWarning("继电器未连接，请先连接继电器！");
                        }

                        break;

                    default:
                        break;
                }


            }
        });

        mWifiRelayRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        mWifiRelayRecyclerView.setAdapter(switchAdapter);


        //打开Wifi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            myToasty.showError(getResources().getString(R.string.Please_open_the_wifi));
            Intent it = new Intent();
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
            it.setComponent(cn);
            startActivity(it);
        }

    }

    private void initData() {

        //初始化语音合成
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                        myToasty.showWarning("当前设备不支持语音合成！");
                    }
                }

            }
        });
//        tts.setSpeechRate(1.0f); 语速
//        tts.setPitch(1.0f);语调


        try {
            fd = getAssets().openFd("saveweight.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //间隔时间,默认2秒
        intervalTime = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_INTERVAL_TIME, 2);

        compositeDisposable = new CompositeDisposable();

        myToasty = new MyToasty(this);
        samplingSize = LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size();
        if (samplingSize > 0) {
            mSampling = "(" + samplingSize + ")";
            SamplingDetails samplingDetails = LitePal.findAll(SamplingDetails.class, true).get(0);
            supplierId = samplingDetails.getSupplierId();
            supplier = LitePal.find(Supplier.class, supplierId);
        }


        Bluetooth mBluetooth = new Bluetooth();

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


    //EventBus
    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(DeviceEvent deviceEvent) {
        Device device = deviceEvent.getDevice();

        //更新间隔时间
        if (deviceEvent.getIntervalTime() > 0) {
            intervalTime = deviceEvent.getIntervalTime();
        }

        //更新累计数+1
        if (deviceEvent.isAdd()) {
            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
            count = count + 1;
            tvCumulativeCount.setText(String.valueOf(count));
        }

        //更新供应商
        if (deviceEvent.getSupplierId() >= 0) {
            supplierId = deviceEvent.getSupplierId();
            supplier = LitePal.find(Supplier.class, supplierId);
            mTvSupplier.setText(supplier.getName());
            //刷新生产线
//            produceLineList.clear();
//            produceLines.clear();
//
//            produceLineList.add(getResources().getString(R.string.choose_produce_line));
//            produceLineList.add("移动称重");
//
//            produceLines = LitePal.findAll(ProduceLine.class);
//            for (int i = 0; i < produceLines.size(); i++) {
//                produceLineList.add(produceLines.get(i).getName());
//            }
//            spinnerAdapter.notifyDataSetChanged();

        }
        //更新品类（物料）
        if (deviceEvent.getMatterId() >= 0) {

            matterId = deviceEvent.getMatterId();
            matter = LitePal.find(Matter.class, matterId);
            mTvMatter.setText(matter.getName());

            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER, matterId);
        }
        //更新品类等级
        if (deviceEvent.getMatterLevelId() >= 0) {

            matterLevelId = deviceEvent.getMatterLevelId();
            matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
            mTvMatterLevel.setText(matterLevel.getName());

            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER_LEVEL, matterLevelId);
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
            tvCumulativeCount.setText("0");
            tvCumulativeWeight.setText("0");


        }

        //断开设备连接
        if (deviceEvent.getDisConnectType() > 0) {
            switch (deviceEvent.getDisConnectType()) {

                case 1://断开收料秤
                    if (BleManager.getInstance() != null && bluetoothManager != null) {
                        BleManager.getInstance().destory();
                        myToasty.showWarning(getResources().getString(R.string.bluetooth_diconnected));
                        produceLine.setMasterConnectionState(false);
                        blueMainScaleConnectionState = false;

                        //延迟1秒之后断开
                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        mWeightTextView.setText("0.00");
                                    }
                                });

                    }
                    break;

                case 2://断开继电器
                    //wifi继电器
                    if (manager != null) {
                        manager.send(new WriteData(Order.TURN_OFF_ALL));
                        manager.unRegisterReceiver(listener);
                        produceLine.setRelayConnectionState(false);
                        wifiConnectionState = false;
                        myToasty.showWarning("继电器已断开");
                    }

                    //蓝牙继电器
                    if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(inLine)).subscribe(stateOB);
                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(outLine)).subscribe(stateOB);

                        //延迟2秒之后断开
                        Observable.timer(2000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        bluetoothRelay.getMyBluetoothManager().disConnect();
//                                        for (int i=0;i<mWifiList.size();i++) {
//                                            mWifiList.get(i).setState("0");
//                                        }
//                                        switchAdapter.notifyDataSetChanged();
                                        produceLine.setRelayConnectionState(false);
                                        blueRelayConnectionState = false;
                                        myToasty.showWarning("继电器已断开");
                                    }
                                });

                    }

                    break;

                case 3://断开采样秤
                    //采样电子秤
                    if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) {
                        bluetoothScale.getMyBluetoothManager().disConnect();
                        produceLine.setSamplingConnectionState(false);
                        blueSamplingScaleConnectionState = false;
                        myToasty.showWarning("采样秤已断开");
                    }
                    break;

                case 4://断开显示屏
                    if (bleTest != null) {
                        isDisconnectScreen = true;
                        /**
                         * 置零0.00
                         */
                        byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
                        byte[] b3 = {0x23};
                        byte[] b2 = "0.00".getBytes();
                        b2 = CRC16Util.addBytes(b1, b2);
                        b3 = CRC16Util.addBytes(b2, b3);
                        bleTest.sendData(b3);


                        //延迟1秒之后断开
                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        bleTest.diconnect();
                                        produceLine.setShowOutConnectionState(false);
                                        bleScreenConnectionState = false;
                                        myToasty.showWarning("显示屏已断开");
                                    }
                                });

                    }
                    break;

                default:
                    break;
            }
        }

        //连接设备

        if (deviceEvent.getConnectType() > 0) {
            switch (deviceEvent.getConnectType()) {

                case 1://收料秤
                    connect(masterDevice);
                    break;

                case 2://继电器
                    if (CONNECT_TYPE == 1) {//wifi
                        connectWifi(wifiRelayDevice);
                    } else if (CONNECT_TYPE == 2) { //蓝牙
                        connectBluetoothRelay(bluetoothRelayDevice);
                    }
                    break;

                case 3://采样秤
                    connectAndGetBluetoothScale(samplingDevice);
                    break;

                case 4://显示屏
                    connectBle(showOutDevice);
                    break;

                default:
                    break;
            }
        }

        //生产线
        if (deviceEvent.getProduceLine() != null) {

            if (SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT).length() == 0) {
                myToasty.showInfo("请先配置单重！");
                return;
            }

            produceLine = deviceEvent.getProduceLine();
            tvProduceLine.setText(produceLine.getName());
            Logger.d(produceLine.toString());

            if (produceLine.getName().equals("移动称重") || produceLine.getName().equals("手动")) {
                if (produceLine.getName().equals("手动")) {
                    isByHandOnly = true;
                }
                //主称
                if (BleManager.getInstance() != null && bluetoothManager != null) {
                    BleManager.getInstance().destory();
                }
                //wifi继电器
                if (manager != null) {
                    manager.send(new WriteData(Order.TURN_OFF_ALL));
                    manager.unRegisterReceiver(listener);
                }
                //蓝牙继电器
                if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(inLine)).subscribe(stateOB);
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(outLine)).subscribe(stateOB);
                    bluetoothRelay.getMyBluetoothManager().disConnect();
                }

                mWifiList.clear();
                //初始化wifi继电器实体类
                List<Integer> leftIcon = new ArrayList<>(SwitchIcon.getRedIcon());
                List<Integer> rightIcon = new ArrayList<>(SwitchIcon.getGreenIcon());

                for (int i = 0; i < leftIcon.size(); i++) {
                    RelayBean relayBean = new RelayBean();
                    relayBean.setLeftIamgeView(leftIcon.get(i));
                    relayBean.setRightImageView(rightIcon.get(i));
                    mWifiList.add(relayBean);
                }
                switchAdapter.notifyDataSetChanged();

                mTvHand.setVisibility(View.VISIBLE);
                Intent intent = ElcScaleActivity.newIntent(MainActivity.this, 0);
                startActivity(intent);
            } else {
//                mTvHand.setVisibility(View.GONE);

                //主秤
                String masterString = produceLine.getMaster();
                Master master = new Gson().fromJson(masterString, Master.class);
                String address = master.getDeviceAddr();
                if (address == null) {
                    myToasty.showInfo("当前未配置收料秤，请前往网页端配置！");
                    return;
                }
                address = address.trim();
                Logger.d("------------>" + address);

                try {
                    masterDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (masterDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        //connectAndGetBluetoothScale(bluetoothDevice);
                        if (!blueMainScaleConnectionState) {
                            connect(masterDevice);
                        }

                    } else {
                        BleManager.getInstance().pin(masterDevice, new PinResultListener() {
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
//                    return;
                } else {

                    inLine = power.getInLine() - 1;
                    outLine = power.getOutLine() - 1;

                    switch (power.getDeviceType()) {
                        case 1://蓝牙继电器
                            CONNECT_TYPE = 2;
                            String address1 = power.getDeviceAddr().trim();
                            try {
                                bluetoothRelayDevice =
                                        BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                                connectBluetoothRelay(bluetoothRelayDevice);

                            } catch (Exception e) {
                                e.printStackTrace();
                                myToasty.showInfo(e.getMessage());
                            }
                            break;

                        case 2://Wifi继电器
                            CONNECT_TYPE = 1;
                            //分割 ：
                            String[] strarr = power.getDeviceAddr().split(":");
                            if (strarr.length < 2) {
                                myToasty.showWarning("Wifi继电器参数格式配置异常，请前往网页端检查！");
                                return;
                            }
                            String ip = strarr[0].trim();
                            int port = Integer.valueOf(strarr[1]);
                            wifiRelayDevice = new Device();
                            wifiRelayDevice.setWifiIp(ip);
                            wifiRelayDevice.setWifiPort(port);
                            connectWifi(wifiRelayDevice);
                            break;

                        default:
                            break;
                    }

                }


                //采样秤
                String sapmleString = produceLine.getSapmle();
                Sapmle sapmle = new Gson().fromJson(sapmleString, Sapmle.class);
                //是否配置了采样的秤
                isSetSapmle = (sapmle.getDeviceAddr() != null);
                if (isSetSapmle) {
                    sampleMacAddress = sapmle.getDeviceAddr().trim();
                }

                //显示屏 蓝牙Ble设备
                String showOutString = produceLine.getShowOut();
                ShowOut showOut = new Gson().fromJson(showOutString, ShowOut.class);
                if (!(showOut.getDeviceAddr() == null)) {
                    String addressBle = showOut.getDeviceAddr().trim();//"D8:E0:4B:FD:31:F6"
                    try {
                        showOutDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
                        connectBle(showOutDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myToasty.showInfo(e.getMessage());
                    }
                } else {
                    myToasty.showInfo("当前未配置显示屏！");
                }
            }

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
        isDisconnectScreen = false;
        if (bleScreenConnectionState) {
            produceLine.setShowOutConnectionState(true);
            return;
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        bleTest = new BleTest(MainActivity.this, bleDevice.getAddress(), new BleCallback() {
            @Override
            public void onConnected(boolean isConnected) {
                if (isConnected) {
                    myToasty.showSuccess("显示屏连接成功！");
                    bleScreenConnectionState = true;
                    produceLine.setShowOutConnectionState(true);
                } else {
                    myToasty.showError("显示屏连接失败！");
                    bleScreenConnectionState = false;
                    produceLine.setShowOutConnectionState(false);
                }
            }

            @Override
            public void disConnected() {
//                myToasty.showWarning("显示屏连接已断开！");
                bleScreenConnectionState = false;
                produceLine.setShowOutConnectionState(false);
            }
        });

        bleTest.connectBle();

    }

    //蓝牙电子秤
    private void connect(BluetoothDevice device) {

        initBlue();

        qmuiTipDialog.show();
        final boolean[] isSend = {false};
        //单重
        final double[] compareWeight = {Double.parseDouble(SharedPreferenceUtil
                .getString(SharedPreferenceUtil.SP_PIECE_WEIGHT))};

        List<String> strWeightList = new ArrayList<>();
        List<String> strLowWeightList = new ArrayList<>();

        if (blueMainScaleConnectionState) {
            return;
        }

        BleManager.getInstance().connect(device, new ConnectResultlistner() {
            @Override
            public void connectSuccess(Connect connect) {
                bluetoothManager = connect;
                blueMainScaleConnectionState = true;
                produceLine.setMasterConnectionState(true);
                qmuiTipDialog.dismiss();
                myToasty.showSuccess(getResources().getString(R.string.Electronic_scale_is_connected));

                final boolean[] isWrite = {false};
                final boolean[] isTurn = {false};

                ControlRelay controlRelay = new ControlRelay(manager, isTurn, inLine, outLine, CONNECT_TYPE, stateOB,
                        bluetoothRelay);

                bluetoothManager.read(new TransferProgressListener() {
                    @Override
                    public void transfering(int progress) {

                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void transferSuccess(String str) {

                        if (observablePieceWeight != null) {
                            observablePieceWeight.subscribe(new Consumer<Double>() {
                                @Override
                                public void accept(Double aDouble) throws Exception {
                                    compareWeight[0] = aDouble;
                                }
                            });
                        }

                        if (str.length() == 8) {
                            //取反
                            str = new StringBuilder(str).reverse().toString();

                            if (!str.endsWith("=") && !str.startsWith("=") && str.substring(5, 6).equals(".")) {
                                //去掉前面的“=”号和零
                                str = str.replaceFirst("^0*", "");
                                if (str.startsWith(".")) {
                                    str = "0" + str;
                                }

                                mWeightTextView.setText(str);

                                byte[] weightByte = CRC16Util.stringToByte(str);

                                if (bleTest != null && !isDisconnectScreen) {
//                                    bleScreenConnectionState = ble.isConnected();
                                    if (!isSend[0] && bleScreenConnectionState) {

//                                        myToasty.showSuccess("显示屏连接成功！");
//                                        produceLine.setShowOutConnectionState(true);
                                        //小数位数，固定2位
//                                        byte[] point = {0x01, 0x06, 0x00, 0x04, 0x00, 0x02, 0x49, (byte) 0xCA};
//                                        ble.send(point);
                                        isSend[0] = true;

                                    }
//                                    ble.send(weightByte);//ModBus方式

                                    /**   $001,2.91#   显示2.91
                                     *    $001,01&     显示YES
                                     *    ASCII码  b1,b3,b4首尾固定格式
                                     *    b2  数据
                                     */

                                    byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
                                    byte[] b3 = {0x23};
                                    byte[] b4 = {0x30, 0x31, 0x26};
                                    byte[] b2 = str.getBytes();
                                    b2 = CRC16Util.addBytes(b1, b2);
                                    b3 = CRC16Util.addBytes(b2, b3);
                                    b4 = CRC16Util.addBytes(b1, b4);
                                    bleTest.sendData(b3);
                                }

                                double weight = Double.parseDouble(str);

                                if (weight > compareWeight[0]) {

                                    //超过单重之后关闭入料口，传送带
                                    controlRelay.turnOffInLine();
//                                            if (!isTurn[0] && manager != null && manager.isConnect()) {
//                                                isTurn[0] = true;
//                                                manager.send(new WriteData(Order.getTurnOff().get(inLine)));
//
//                                            }

                                    strWeightList.add(str);
                                    int size = strWeightList.size();

                                    if (StringUtils.isStable(strWeightList, 10)) { //倒数连续10个数相等，则说明电子秤读数稳定


                                        if (!isWrite[0]) {

                                            //语音播报,第二个参数代表清空消息队列
                                            tts.speak(strWeightList.get(size - 1), TextToSpeech.QUEUE_FLUSH, null);

                                            if (!isByHandOnly) { //自动 + 半自动

                                                if (manager == null && bluetoothRelay == null) { //当前未连接继电器，说明此时是半自动模式
                                                    openAssetMusics();
                                                }

                                                isWrite[0] = true;

                                                //计重
                                                saveWeight(strWeightList.get(size - 1));

                                                tvFirst.setText(tvSecond.getText());
                                                tvSecond.setText(tvThird.getText());
                                                tvThird.setText(strWeightList.get(size - 1));
//
                                                //计重之后打开2号开关，出料口开始倾倒物料
                                                controlRelay.turnOnOutLine();
//                                                if (manager != null && manager.isConnect()) {
//                                                    manager.send(new WriteData(Order.getTurnOn().get(outLine)));
//                                                }
                                            } else {
                                                //手动模式
                                                openAssetMusics();
                                                isWrite[0] = true;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setTitle("确定上传数据？")
                                                        .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                //计重
                                                                saveWeight(strWeightList.get(size - 1));

                                                            }
                                                        })
                                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Logger.d("取消");
                                                                isWrite[0] = false;
                                                            }
                                                        }).show();


                                            }

                                        }

                                    }

                                } else {

                                    if (isWrite[0]) {

                                        if (strWeightList.size() > 0) {
                                            strWeightList.clear();
                                        }

                                        strLowWeightList.add(str);

                                        //连续10个数相等，说明电子秤读数稳定，也就是说秤上的物料已经倒完（有可能不为0）
                                        if (StringUtils.isStable(strLowWeightList, 10)) {

                                            //出料口倾倒完毕，此时关闭出料口
                                            controlRelay.turnOffOutLine();
//                                                if (manager != null && manager.isConnect()) {
//                                                    manager.send(new WriteData(Order.getTurnOff().get(outLine)));
//                                                }

                                            //间隔1秒之后打开入料口开关，传送带
                                            Observable.timer(1000, TimeUnit.MILLISECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {

                                                            //间隔1秒之后打开入料口
                                                            controlRelay.turnOnInLine();
//                                                                if (manager != null && manager.isConnect()) {
//                                                                    manager.send(new WriteData(Order.getTurnOn()
//                                                                    .get(inLine)));
//                                                                }
                                                            isWrite[0] = false;
                                                            isTurn[0] = false;
                                                            controlRelay.setIsTurn(isTurn);
                                                            strLowWeightList.clear();
                                                        }
                                                    });


                                        }

                                    }

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
                myToasty.showError(getResources().getString(R.string.connect_failed));
//                blueMainScaleConnectionState = false;
//                produceLine.setMasterConnectionState(false);
            }

            @Override
            public void disconnected() {
//                myToasty.showWarning(getResources().getString(R.string.bluetooth_diconnected));
//                blueMainScaleConnectionState = false;
//                produceLine.setMasterConnectionState(false);
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
                        .setTipWord(getResources().getString(R.string.is_connecting))
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
                    produceLine.setSamplingConnectionState(true);
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
                produceLine.setRelayConnectionState(true);
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
                        produceLine.setRelayConnectionState(true);
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

                            //第几号开关
                            int whichSwitch = Integer.parseInt(index.substring(1, 2));

                            if (whichSwitch == inLine) {
                                if (state.equals("00")) {
                                    tvInLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                                    tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                                } else {
                                    tvInLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                                    tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                                }
                            }

                            if (whichSwitch == outLine) {
                                if (state.equals("00")) {
                                    tvOutLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                                    tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                                } else {
                                    tvOutLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                                    tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                                }
                            }

                            if (state.equals("00")) {
                                mWifiList.get(whichSwitch).setState("0");//断开
                            } else {
                                mWifiList.get(whichSwitch).setState("1");//吸和
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
                produceLine.setRelayConnectionState(true);
                manager.send(new WriteData(Order.GET_STATE));
                //连接成功之后就打开某个开关
                manager.send(new WriteData(Order.getTurnOn().get(inLine)));

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

                    if (whichSwitch == inLine) {
                        if (state.equals("00")) {
                            tvInLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                            tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                        } else {
                            tvInLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                            tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                        }
                    }

                    if (whichSwitch == outLine) {
                        if (state.equals("00")) {
                            tvOutLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                            tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                        } else {
                            tvOutLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                            tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                        }
                    }

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
                produceLine.setRelayConnectionState(false);
            }

            @Override
            public void onConnectionFailed(Exception e) {
                myToasty.showError("Wifi继电器连接失败！");
            }
        });

        manager = okSocket.getManager();
        listener = okSocket.getiSocketActionListener();

    }


    //注册蓝牙配对广播接收器
    private void registerReceiver() {

        pinBlueReceiver = new PinBlueReceiver(new PinBlueCallBack() {
            @Override
            public void onBondRequest() {

            }

            @Override
            public void onBondFail(BluetoothDevice device) {
                myToasty.showError("配对失败，请重试！");
            }

            @Override
            public void onBonding(BluetoothDevice device) {

            }

            @Override
            public void onBondSuccess(BluetoothDevice device) {

                myToasty.showInfo("配对成功，请点击重连！");

            }
        }, "1234");

        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(pinBlueReceiver, filter4);
        registerReceiver(pinBlueReceiver, filter5);

    }


    @Override
    public void onClick(View view) {
        samplingSize = LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size();
        switch (view.getId()) {
            case R.id.id_iv_choose_matter://选择品类
                Intent matterIntent = new Intent(MainActivity.this, ChooseMatterActivity.class);

                if (samplingSize > 0) {
                    isContinue(matterIntent);
                } else {
                    startActivity(matterIntent);
                }

                break;

            case R.id.id_iv_choose_matter_level://选择品类等级
                Intent matterLevelIntent = new Intent(MainActivity.this, ChooseMatterLevelActivity.class);

                if (samplingSize > 0) {
                    isContinue(matterLevelIntent);
                } else {
                    startActivity(matterLevelIntent);
                }

                break;

            case R.id.id_tv_choose_supplier://选择供应商
                Intent supplierIntent = new Intent(MainActivity.this, ChooseSupplierActivity.class);

                if (samplingSize > 0) {
                    isContinue(supplierIntent);
                } else {
                    startActivity(supplierIntent);
                }

                break;

            case R.id.id_iv_choose_specs://选择规格
                startActivity(new Intent(MainActivity.this, ChooseSpecsActivity.class));
                break;

            case R.id.tv_produce_line://选择生产线

                if (supplier != null && matterId > 0 && matterLevelId > 0) {
                    ProduceLineListFragment produceLineListFragment = new ProduceLineListFragment();
                    showDialogFragment(produceLineListFragment, PRODUCE_LINE_LIST);
                } else {
                    myToasty.showWarning("请先选择供应商，品类，品类等级！");
                }

                break;

            case R.id.id_tv_sampling://采样

                if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) { //已连接
                    //已连接
                    if (supplier != null && matterId > 0 && matterLevelId > 0) {
                        SamplingFragment samplingFragment = SamplingFragment.newInstance(samplingWeight, supplierId,
                                matterId, matterLevelId);
                        showDialogFragment(samplingFragment, SAMPLING);
                    } else {
                        myToasty.showWarning("请先选择供应商，品类，品类等级！");
                    }

                } else {//未连接

                    if (isSetSapmle) {//是否配置了采样秤
                        try {
                            samplingDevice =
                                    BluetoothAdapter.getDefaultAdapter().getRemoteDevice(sampleMacAddress);
                            connectAndGetBluetoothScale(samplingDevice);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myToasty.showInfo(e.getMessage());
                        }
                    } else {
//                        myToasty.showInfo("未配置采样秤，请选择要进行的操作！");

                        if (SAMPLING_MODE == 0) {
                            String[] items = new String[2];
                            items[0] = "手动填写重量";
                            items[1] = "扫描连接附近电子秤";
                            QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(MainActivity.this);
                            builder.addItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:

                                            if (supplier != null && matterId > 0 && matterLevelId > 0) {

                                                SAMPLING_MODE = 1;

                                                SamplingFragment samplingFragment = SamplingFragment.newInstance("0",
                                                        supplierId,
                                                        matterId, matterLevelId);
                                                showDialogFragment(samplingFragment, SAMPLING);
                                            } else {
                                                myToasty.showWarning("请先选择供应商，品类，品类等级！");
                                            }

                                            break;

                                        case 1:

                                            SAMPLING_MODE = 2;

                                            Intent intent2 = ElcScaleActivity.newIntent(MainActivity.this, 3);
                                            startActivity(intent2);
                                            break;
                                        default:
                                            break;
                                    }

                                    dialogInterface.dismiss();
                                }
                            }).show();
                        } else {

                            switch (SAMPLING_MODE) {
                                case 1:
                                    if (supplier != null && matterId > 0 && matterLevelId > 0) {

                                        SAMPLING_MODE = 1;

                                        SamplingFragment samplingFragment = SamplingFragment.newInstance("0",
                                                supplierId,
                                                matterId, matterLevelId);
                                        showDialogFragment(samplingFragment, SAMPLING);
                                    } else {
                                        myToasty.showWarning("请先选择供应商，品类，品类等级！");
                                    }
                                    break;

                                case 2:
                                    Intent intent2 = ElcScaleActivity.newIntent(MainActivity.this, 3);
                                    startActivity(intent2);
                                    break;
                                default:
                                    break;
                            }


                        }
                    }
                }

                break;


            case R.id.id_tv_sampling_count://采样累计

                SamplingDetailsFragment samplingDetailsFragment = new SamplingDetailsFragment();

                showDialogFragment(samplingDetailsFragment, SAMPLING_DETAILS);
                break;

            case R.id.id_tv_deduction_cumulative://扣重累计

                CumulativeFragment deductionCumulativeFragment = CumulativeFragment.newInstance(1);

                showDialogFragment(deductionCumulativeFragment, CUMULATIVE);
                break;

            case R.id.id_tv_cumulative://计重累计

                CumulativeFragment cumulativeFragment = CumulativeFragment.newInstance(2);

                showDialogFragment(cumulativeFragment, CUMULATIVE);
                break;

            case R.id.id_tv_deduction://扣重
                DeductionFragment deductionFragment =
                        DeductionFragment.newInstance(mWeightTextView.getText().toString());
                showDialogFragment(deductionFragment, DEDUCTION);
                break;

            case R.id.id_tv_deductionMix://扣重率
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
                                deductionMix = Integer.valueOf(editText1.getText().toString().trim());

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.id_iv_print://打印(单据列表)

                BillListFragment billListFragment = new BillListFragment();

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
                                String pieceWeight = editText.getText().toString().trim();

                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_PIECE_WEIGHT,
                                        pieceWeight);
                                mShowPieceWeight.setText(pieceWeight);

                                observablePieceWeight = Observable.create(new ObservableOnSubscribe<Double>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Double> emitter) throws Exception {
                                        emitter.onNext(Double.valueOf(pieceWeight));
                                        emitter.onComplete();
                                    }
                                });

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.id_iv_setting://左侧设置界面

                SettingFragment settingFragment = new SettingFragment();

                showDialogFragment(settingFragment, SETTING);
                break;

            case R.id.id_iv_takePicture://设备列表
//                takePicture();
                if (produceLine == null) {
                    myToasty.showWarning("当前未连接过任何设备，无法查看！");
                    return;
                }

                Intent it = new Intent(MainActivity.this, DeviceListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("produceLine", produceLine);
                it.putExtras(bundle);
                startActivity(it);

                break;

            case R.id.id_tv_save_bill://保存单据

                if (Double.valueOf(tvCumulativeWeight.getText().toString().trim()) <= 0) {
                    myToasty.showWarning("当前还未称重！");
                    return;
                }
                List<SamplingDetails> list = LitePal.where("hasBill < ?", "0").find(SamplingDetails.class);
                int samplingSize = list.size();

                if (samplingSize <= 0) {
                    //未采样
                    myToasty.showWarning("请先采样！");

//                    //未采样，则必须选择供应商，品类，品类等级，规格
//                    if (supplier == null || matterId <= 0 || matterLevelId <= 0 || specs == null) { //未选择，直接返回
//                        myToasty.showWarning("请先选择供应商，品类，品类等级，规格！");
//                        return;
//                    }
//
//                    //已选择供应商，品类，品类等级，规格
//
//                    Intent intent1 = SaveBillWithoutSamplingActivity.newInstance(MainActivity.this,
//                            deductionMix, tvCumulativeWeight.getText().toString().trim(), supplierId, matterId,
//                            matterLevelId, specsId);
//
//                    startActivity(intent1);

                } else {
                    //已采样
                    int id = list.get(0).getMatterId();
                    Matter matter = LitePal.find(Matter.class, id);
                    int type = matter.getValuationType();
                    /**
                     * 计价方式
                     * ValuationType = 1;根据规格计算
                     * ValuationType = 2;根据规格占比计算
                     */
                    if (type == 1) {
                        //根据规格计算
                        //判断采样明细里面是否确认了规格和单价
                        List<SamplingBySpecs> samplingBySpecsList =
                                LitePal.where("hasBill < ?", "0").find(SamplingBySpecs.class);
                        if (samplingBySpecsList.size() <= 0) {
                            //未确认
                            myToasty.showWarning("请先点击采样累计，确认规格和单价！");
                            return;
                        }
                    }

                    Intent intent2 = SaveBillDetailsActivity.newInstance(MainActivity.this,
                            deductionMix, tvCumulativeWeight.getText().toString().trim());
                    startActivity(intent2);
                }

                break;

            case R.id.id_tv_hand://手动计重
                double weight = Double.parseDouble(mWeightTextView.getText().toString());
                if (weight > 0d) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("确定上传数据？")
                            .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Cumulative cumulative = new Cumulative();
                                    cumulative.setCategory("净重");
                                    cumulative.setWeight(mWeightTextView.getText().toString());

                                    //获取当前时间 HH:mm:ss
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                    Date date = new Date(System.currentTimeMillis());
                                    String time = simpleDateFormat.format(date);
                                    cumulative.setTime(time);

                                    cumulative.save();

                                    int count = Integer.parseInt(tvCumulativeCount.getText().toString());
                                    double cWeight = Double.parseDouble(tvCumulativeWeight.getText().toString());

                                    cWeight = new DoubleCountUtils(cWeight, Double.valueOf(mWeightTextView.getText().toString())).add();
                                    count = count + 1;

                                    tvCumulativeCount.setText(String.valueOf(count));
                                    tvCumulativeWeight.setText(String.valueOf(cWeight));

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Logger.d("取消");
                                }
                            }).show();

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
        //wifi继电器
        if (manager != null) {
            //manager.send(new WriteData(Order.TURN_OFF_ALL));
            manager.unRegisterReceiver(listener);
            manager = null;
        }
        //蓝牙继电器
        if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
            //bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);
            bluetoothRelay.getMyBluetoothManager().disConnect();
            bluetoothRelay = null;
        }
        //采样电子秤
        if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) {
            bluetoothScale.getMyBluetoothManager().disConnect();
            bluetoothScale = null;
        }

        if (pinBlueReceiver != null) {
            unregisterReceiver(pinBlueReceiver);
        }

        if (compositeDisposable != null && compositeDisposable.size() > 0) {
            compositeDisposable.dispose();
        }
        //主称
        if (BleManager.getInstance() != null && bluetoothManager != null) {
            BleManager.getInstance().destory();
        }

        if (bleTest != null) {
            bleTest.diconnect();
            bleTest = null;
        }

        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (permissionHandler != null) {
            permissionHandler.onActivityResult(requestCode, resultCode, data);
        }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase,
                SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG, 0)));
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

    //播放称重成功提示音
    private void openAssetMusics() {

        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计重
    private void saveWeight(String weight) {

        Cumulative cumulative = new Cumulative();
        cumulative.setCategory("净重");
        cumulative.setWeight(weight);

        //获取当前时间 HH:mm:ss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        cumulative.setTime(time);

        cumulative.save();

        int count = Integer.parseInt(tvCumulativeCount.getText().toString());
        double cWeight = Double.parseDouble(tvCumulativeWeight.getText().toString());

        cWeight = new DoubleCountUtils(cWeight, Double.valueOf(weight)).add();
        count = count + 1;

        tvCumulativeCount.setText(String.valueOf(count));
        tvCumulativeWeight.setText(String.valueOf(cWeight));

    }

    //当已有采样数据时，是否继续选择供应商，品类，品类等级
    //继续，则删除所有采样数据
    private void isContinue(Intent intent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("当前已采样，请勿重复选择！")
                .setMessage("继续则将删除所有采样数据？！")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LitePal.deleteAll(SamplingDetails.class, "hasBill < ?", "0");
                        LitePal.deleteAll(SamplingBySpecs.class, "hasBill < ?", "0");
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Logger.d("取消");
                    }
                }).show();


    }


}
