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
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import android.support.v7.app.SkinAppCompatDelegateImpl;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
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
import io.reactivex.schedulers.Schedulers;
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
    TextView mPieceweightTextView;//??????
    @BindView(R.id.id_tv_sampling)
    TextView mSamplingTextView;//??????
    @BindView(R.id.id_tv_sampling_count)
    TextView mSamplingCount;//????????????
    @BindView(R.id.id_tv_cumulative)
    TextView mCumulativeTextView;//??????
    @BindView(R.id.id_tv_deduction)
    TextView mDeductionTextView;//??????
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
    TextView mTvHand;//????????????
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
    @BindView(R.id.iv_relay_model)
    ImageView ivRelayModel;
    @BindView(R.id.iv_save_model)
    ImageView ivSaveModel;
    @BindView(R.id.tv_relay_hand)
    TextView tvRelayHand;
    @BindView(R.id.tv_relay_automatic)
    TextView tvRelayAutomatic;
    @BindView(R.id.tv_save_automatic)
    TextView tvSaveAutomatic;
    @BindView(R.id.tv_save_hand)
    TextView tvSaveHand;
    private CircleTextView circleTextView;

    private CompositeDisposable compositeDisposable;

    private int samplingSize;//?????????????????????

    private PinBlueReceiver pinBlueReceiver;

    private List<RelayBean> mWifiList;
    private ISocketActionListener listener;
    private IConnectionManager manager;

    private Connect bluetoothManager;
    private BluetoothPermissionHandler permissionHandler;

    //???????????????
    private BluetoothBean bluetoothScale;

    private SwitchAdapter switchAdapter;
    //???????????????
    private BluetoothBean bluetoothRelay;
    //???????????????
    private BluetoothAdapter mBluetoothAdapter;


    //???????????????????????????
    private boolean isSetSapmle = false;
    //?????????Mac??????
    private String sampleMacAddress;
    private ArrayAdapter spinnerAdapter;

    //Wifi????????????????????????
    private boolean wifiConnectionState = false;
    //??????????????????????????????
    private boolean blueRelayConnectionState = false;
    //?????????????????????????????????
    private boolean blueMainScaleConnectionState = false;
    //????????????????????????????????????
    private boolean blueSamplingScaleConnectionState = false;
    //??????Ble????????????????????????
    private boolean bleScreenConnectionState = false;
    //??????Ble??????
//    private Ble ble;
    private BleTest bleTest;
    //????????????????????????
    private boolean isDisconnectScreen;
    //????????????
    private int intervalTime;
    //?????????
    private int deductionMix = 0;

    private Observable<Double> observablePieceWeight;

    //?????????
    private BluetoothDevice masterDevice;
    //???????????????
    private BluetoothDevice bluetoothRelayDevice;
    //Wifi?????????
    private Device wifiRelayDevice;
    //???????????????
    private BluetoothDevice samplingDevice;
    //?????????
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


    //????????????????????????
    private int inLine;
    private int outLine;
    private int buzzerLine;
    //?????????????????? true????????????
    private boolean inLineState;
    private boolean outLineState = false;
    private boolean buzzerLineState;
    private ControlRelay controlRelay;

    private final boolean[] isWrite = {false};
    private final boolean[] isSpeak = {false};

    //????????????
    private long currentTimeMillis;

    //????????????
    private int zero;
    //?????????????????????
    private int save;
    //?????????????????????
    private int out;

    //?????????
    private ProduceLine produceLine;

    /**
     * ?????????????????????
     * 1-Wifi?????????
     * 2-???????????????
     */
    private static int CONNECT_TYPE = -1;

    /**
     * ????????????
     * 1.??????????????????
     * 2.???????????????????????????
     */
    private static int SAMPLING_MODE = 0;

    /**
     * ????????????
     */
    private boolean hasRelayAuto = false;
    private boolean hasRelayByHand = false;
    private boolean withoutRelayAuto = false;
    private boolean withoutRelayByHand = false;
    //??????????????????
    private boolean isZeroStart;

    private Disposable beginDisposable;
    private Disposable finalDisposable;

    private QMUITipDialog qmuiTipDialog;

    private MediaPlayer mediaPlayer;
    //?????? assets/saveweight.wav ????????????
    private AssetFileDescriptor fd;

    private TextToSpeech tts;

    private byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
    private byte[] b3 = {0x23};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);


        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initData();
        initView();

    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    private void initBlue() {

        permissionHandler = new BluetoothPermissionHandler(this, new BluetoothPermissionCallBack() {
            @Override
            public void onBlueToothEnabled() {
                BleManager.getInstance().init(getApplicationContext());
                //????????????
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
        ivRelayModel.setOnClickListener(this);
        ivSaveModel.setOnClickListener(this);
        tvRelayAutomatic.setOnClickListener(this);
        tvRelayHand.setOnClickListener(this);
        tvSaveAutomatic.setOnClickListener(this);
        tvSaveHand.setOnClickListener(this);
        tvInLine.setOnClickListener(this);
        tvOutLine.setOnClickListener(this);

        tvRelayAutomatic.setClickable(false);
        tvRelayHand.setClickable(false);
        tvSaveAutomatic.setClickable(false);
        tvSaveHand.setClickable(false);


        //????????????
        circleTextView = findViewById(R.id.textView29);

        final boolean[] isChecked = {false};

        circleTextView.setClickListener(new CircleTextView.onClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick() {

                double weight = Double.parseDouble(mWeightTextView.getText().toString());
                if (weight > 0d) {

                    if (!isChecked[0]) {
                        QMUIDialog.CheckBoxMessageDialogBuilder builder = new QMUIDialog.CheckBoxMessageDialogBuilder(MainActivity.this);
                        QMUIDialog qmuiDialog = builder.setMessage("??????????????????")
                                .setChecked(true)
                                .addAction("??????", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
//                                        isWrite[0] = false;
//                                        isSpeak[0] = false;
                                        dialog.dismiss();

                                    }
                                })
                                .addAction("??????", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        isChecked[0] = builder.isChecked();
                                        isWrite[0] = true;

                                        saveWeight(String.valueOf(weight));
                                        openAssetMusics();

                                        if (controlRelay != null) {
                                            //??????3?????????
                                            controlRelay.turnOnLightLine();
                                            //3????????????????????????save,??????3???
                                            Observable.timer(save, TimeUnit.SECONDS)
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            controlRelay.turnOffLightLine();
                                                        }
                                                    });
                                        }

                                        dialog.dismiss();
                                    }
                                })
                                .create(R.style.QMUI_Dialog);
                        qmuiDialog.show();
                    }

                    if (isChecked[0]) {

                        //??????
                        isWrite[0] = true;

                        saveWeight(String.valueOf(weight));
                        openAssetMusics();

                        if (controlRelay != null) {
                            //??????3?????????
                            controlRelay.turnOnLightLine();
                            //3????????????????????????save,??????3???
                            Observable.timer(save, TimeUnit.SECONDS)
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long aLong) throws Exception {
                                            controlRelay.turnOffLightLine();
                                        }
                                    });
                        }

                    }


                } else {
                    myToasty.showWarning("????????????????????????????????????");
                }

            }
        });


        mTvHandSwitch.setOnCheckedChangeListener((compoundButton, b) -> isClickable = b);

//        mShowPieceWeight.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //?????????
        mShowPieceWeight.setText(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT));

        //?????????
        if (supplier != null) {
            //???????????????????????????????????????
            mTvSupplier.setText(supplier.getName());
        }

        //????????????
        matterLevel = new MatterLevel();
        matterLevelId = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_MATTER_LEVEL, -1);
        if (matterLevelId > 0) {
            matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
            mTvMatterLevel.setText(matterLevel.getName());
        }
        //??????
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
                     * Wifi?????????
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
                                myToasty.showInfo("???????????????????????????");
                            }
                        } else {
                            myToasty.showInfo("?????????????????????????????????????????????");
                        }
                        break;

                    case 2:
                        /**
                         * ???????????????
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
                                myToasty.showWarning("???????????????????????????");
                            }

                        } else {
                            myToasty.showWarning("?????????????????????????????????????????????");
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


        //??????Wifi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            myToasty.showError(getResources().getString(R.string.Please_open_the_wifi));
            Intent it =  new Intent(Settings.ACTION_WIFI_SETTINGS);;
//            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
//            it.setComponent(cn);
            startActivity(it);
        }

    }

    private void initData() {

        //????????????
        zero = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_ZERO_TIME, 0);
        //?????????????????????
        save = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAVE_TIME, 0);
        //?????????????????????
        out = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_OUT_TIME, 0);

        //?????????????????????
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                        myToasty.showWarning("????????????????????????????????????");
                    }
                }

            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                //????????????????????????????????????????????????????????????????????????
                if (s != null && s.length() > 0 && (hasRelayAuto || withoutRelayAuto)) {
                    openAssetMusics();
                }
            }

            @Override
            public void onError(String s) {
                myToasty.showError(s);
            }
        });
//        tts.setSpeechRate(1.0f); ??????
//        tts.setPitch(1.0f);??????


        try {
            fd = getAssets().openFd("saveweight.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }


        compositeDisposable = new CompositeDisposable();

        myToasty = new MyToasty(this);

        samplingSize = LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size();
        if (samplingSize > 0) {
            SamplingDetails samplingDetails = LitePal.findAll(SamplingDetails.class, true).get(0);
            supplierId = samplingDetails.getSupplierId();
            supplier = LitePal.find(Supplier.class, supplierId);
        }


        Bluetooth mBluetooth = new Bluetooth();

        //Wifi?????????
        mWifiList = new ArrayList<>();


        //?????????wifi??????????????????
        List<Integer> leftIcon = new ArrayList<>(SwitchIcon.getRedIcon());
        List<Integer> rightIcon = new ArrayList<>(SwitchIcon.getGreenIcon());

        for (int i = 0; i < leftIcon.size(); i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setLeftIamgeView(leftIcon.get(i));
            relayBean.setRightImageView(rightIcon.get(i));
            mWifiList.add(relayBean);
        }

        //????????????
        if (!mBluetooth.isSupportBlue()) {
            myToasty.showError("??????????????????????????????");
            return;
        } else if (!mBluetooth.isBlueEnable()) {
            //????????????
            mBluetooth.openBlueAsyn();
        }
        //??????????????????
        registerReceiver();
    }


    //EventBus
    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(DeviceEvent deviceEvent) {
        Device device = deviceEvent.getDevice();

        //??????????????????
        if (deviceEvent.getIntervalTime() > 0) {
            intervalTime = deviceEvent.getIntervalTime();
        }

        //???????????????+1
        if (deviceEvent.isAdd()) {
            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
            count = count + 1;
            tvCumulativeCount.setText(String.valueOf(count));
        }

        //???????????????
        if (deviceEvent.getSupplierId() >= 0) {
            supplierId = deviceEvent.getSupplierId();
            supplier = LitePal.find(Supplier.class, supplierId);
            mTvSupplier.setText(supplier.getName());

        }
        //????????????????????????
        if (deviceEvent.getMatterId() >= 0) {

            matterId = deviceEvent.getMatterId();
            matter = LitePal.find(Matter.class, matterId);
            mTvMatter.setText(matter.getName());

            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER, matterId);
        }
        //??????????????????
        if (deviceEvent.getMatterLevelId() >= 0) {

            matterLevelId = deviceEvent.getMatterLevelId();
            matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
            mTvMatterLevel.setText(matterLevel.getName());

            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER_LEVEL, matterLevelId);
        }
        //????????????
        if (deviceEvent.getSpecsId() >= 0) {
            specs = new Specs();
            specsId = deviceEvent.getSpecsId();
            specs = LitePal.find(Specs.class, specsId);
            mTvSpecs.setText(specs.getName());
        }

        //???????????????????????????
        if (deviceEvent.isReset()) {
            tvCumulativeCount.setText("0");
            tvCumulativeWeight.setText("0");


        }

        //??????????????????
        if (deviceEvent.getDisConnectType() > 0) {
            switch (deviceEvent.getDisConnectType()) {

                case 1://???????????????
                    if (BleManager.getInstance() != null && bluetoothManager != null) {
                        BleManager.getInstance().destory();
                        myToasty.showWarning(getResources().getString(R.string.bluetooth_diconnected));
                        produceLine.setMasterConnectionState(false);
                        blueMainScaleConnectionState = false;

                        //??????1???????????????
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

                case 2://???????????????
                    //wifi?????????
                    if (manager != null) {
                        manager.send(new WriteData(Order.TURN_OFF_ALL));
                        manager.unRegisterReceiver(listener);
                        produceLine.setRelayConnectionState(false);
                        wifiConnectionState = false;
                        myToasty.showWarning("??????????????????");
                    }

                    //???????????????
                    if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);

                        //??????2???????????????
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
                                        myToasty.showWarning("??????????????????");
                                    }
                                });

                    }

                    break;

                case 3://???????????????
                    //???????????????
                    if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) {
                        bluetoothScale.getMyBluetoothManager().disConnect();
                        produceLine.setSamplingConnectionState(false);
                        blueSamplingScaleConnectionState = false;
                        myToasty.showWarning("??????????????????");
                    }
                    break;

                case 4://???????????????
                    if (bleTest != null) {
                        isDisconnectScreen = true;
                        /**
                         * ??????0.00
                         */
                        byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
                        byte[] b3 = {0x23};
                        byte[] b2 = "0.00".getBytes();
                        b2 = CRC16Util.addBytes(b1, b2);
                        b3 = CRC16Util.addBytes(b2, b3);
                        bleTest.sendData(b3);


                        //??????1???????????????
                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        bleTest.diconnect();
                                        produceLine.setShowOutConnectionState(false);
                                        bleScreenConnectionState = false;
                                        myToasty.showWarning("??????????????????");
                                    }
                                });

                    }
                    break;

                default:
                    break;
            }
        }

        //????????????

        if (deviceEvent.getConnectType() > 0) {
            switch (deviceEvent.getConnectType()) {

                case 1://?????????
                    connect(masterDevice);
                    break;

                case 2://?????????
                    if (CONNECT_TYPE == 1) {//wifi
                        connectWifi(wifiRelayDevice);
                    } else if (CONNECT_TYPE == 2) { //??????
                        connectBluetoothRelay(bluetoothRelayDevice);
                    }
                    break;

                case 3://?????????
                    connectAndGetBluetoothScale(samplingDevice);
                    break;

                case 4://?????????
                    connectBle(showOutDevice);
                    break;

                default:
                    break;
            }
        }

        //?????????
        if (deviceEvent.getProduceLine() != null) {

            if (SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT).length() == 0) {
                myToasty.showInfo("?????????????????????");
                return;
            }

            produceLine = deviceEvent.getProduceLine();
            tvProduceLine.setText(produceLine.getName());
            Logger.d(produceLine.toString());

            if (produceLine.getName().equals("????????????")) {

                //??????
                if (BleManager.getInstance() != null && bluetoothManager != null) {
                    BleManager.getInstance().destory();
                }
                //wifi?????????
                if (manager != null) {
                    manager.send(new WriteData(Order.TURN_OFF_ALL));
                    manager.unRegisterReceiver(listener);
                }
                //???????????????
                if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);

                    bluetoothRelay.getMyBluetoothManager().disConnect();
                }

                mWifiList.clear();
                //?????????wifi??????????????????
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

                //??????
                String masterString = produceLine.getMaster();
                Master master = new Gson().fromJson(masterString, Master.class);
                String address = master.getDeviceAddr();
                if (address == null) {
                    myToasty.showInfo("??????????????????????????????????????????????????????");
                    return;
                }
                address = address.trim();
                Logger.d("------------>" + address);

                try {
                    masterDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (masterDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        if (!blueMainScaleConnectionState) {
                            connect(masterDevice);
                        }

                    } else {
                        BleManager.getInstance().pin(masterDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
                                connect(device);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    myToasty.showInfo(e.getMessage());
                }

                //?????????
                String powerString = produceLine.getPower();
                Power power = new Gson().fromJson(powerString, Power.class);
                if (power.getDeviceAddr() == null) {
                    myToasty.showError("????????????????????????,???????????????????????????");
                    return;
                }


                if ((power.getInLine() == 0 && power.getOutLine() != 0) || (power.getInLine() != 0 && power.getOutLine() == 0)) {
                    //?????????0???????????????0?????????????????????
                    myToasty.showError("???????????????????????????????????????????????????????????????");
                    return;
                }
                if (power.getInLine() == 0) { //?????????????????????0???????????????0
                    //??????????????????????????????????????????????????????????????????4?????????
                    //????????????????????????????????????
                    tvRelayAutomatic.setClickable(false);
                    tvRelayHand.setClickable(false);
                    tvInLine.setClickable(false);
                    tvOutLine.setClickable(false);

                    tvSaveAutomatic.setClickable(true);
                    tvSaveHand.setClickable(true);

                } else {

                    //??????????????????????????????????????????????????????
                    tvRelayHand.callOnClick();

                }

                inLine = power.getInLine() - 1;
                outLine = power.getOutLine() - 1;
                buzzerLine = power.getBuzzer() - 1;

                switch (power.getDeviceType()) {
                    case 1://???????????????
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

                    case 2://Wifi?????????
                        CONNECT_TYPE = 1;
                        //?????? ???
                        String[] strarr = power.getDeviceAddr().split(":");
                        if (strarr.length < 2) {
                            myToasty.showWarning("Wifi???????????????????????????????????????????????????????????????");
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


                //?????????
                String sapmleString = produceLine.getSapmle();
                Sapmle sapmle = new Gson().fromJson(sapmleString, Sapmle.class);
                //???????????????????????????
                isSetSapmle = (sapmle.getDeviceAddr() != null);
                if (isSetSapmle) {
                    sampleMacAddress = sapmle.getDeviceAddr().trim();
                }

                //????????? ??????Ble??????
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
                    myToasty.showInfo("???????????????????????????");
                }
            }

        }

        if (device.getType() >= 0) {
            switch (device.getType()) {
                case 0://???????????????

                    String address = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        connect(bluetoothDevice);
                    } else {
                        BleManager.getInstance().pin(bluetoothDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
                                connect(device);
                            }
                        });
                    }

                    break;
                case 1://???????????????
                    CONNECT_TYPE = 2;
                    String address1 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                    connectBluetoothRelay(bluetoothDevice1);
                    break;
                case 2://Wifi?????????
                    CONNECT_TYPE = 1;
                    connectWifi(device);
                    break;

                case 3://????????????????????????
                    String address2 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice2 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address2);
                    connectAndGetBluetoothScale(bluetoothDevice2);
                    break;

                case 4://??????Ble????????? "D8:E0:4B:FD:31:F6"
                    String addressBle = device.getBluetoothMac();
                    BluetoothDevice bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
                    connectBle(bleDevice);

                default:
                    break;
            }
        }

    }

    //??????Ble?????????
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
                    myToasty.showSuccess("????????????????????????");
                    bleScreenConnectionState = true;
                    produceLine.setShowOutConnectionState(true);
                } else {
                    myToasty.showError("????????????????????????");
                    bleScreenConnectionState = false;
                    produceLine.setShowOutConnectionState(false);
                }
            }

            @Override
            public void disConnected() {
//                myToasty.showWarning("???????????????????????????");
                bleScreenConnectionState = false;
                produceLine.setShowOutConnectionState(false);
            }
        });

        bleTest.connectBle();

    }

    //???????????????
    private void connect(BluetoothDevice device) {

        initBlue();

        qmuiTipDialog.show();
        final boolean[] isSend = {false};
        //??????
        final double[] compareWeight = {Double.parseDouble(SharedPreferenceUtil
                .getString(SharedPreferenceUtil.SP_PIECE_WEIGHT))};
        //???????????????????????????false
        isZeroStart = SharedPreferenceUtil.getBoolean(SharedPreferenceUtil.SP_ZERO_START);

        //????????????
        zero = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_ZERO_TIME, 0);
        //?????????????????????
        save = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAVE_TIME, 0);
        //?????????????????????
        out = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_OUT_TIME, 0);
        int tempSave = save;
        //???????????????
        if (save == out) {
            out = out * 1000 + 200; //??????200??????
        } else {
            out = out * 1000;
        }

        List<String> strWeightList = new ArrayList<>();
        List<String> strLowWeightList = new ArrayList<>();
        List<String> weightList = new ArrayList<>();

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


                final boolean[] isTurn = {false};
                final boolean[] isZero = {false};
                final boolean[] isBegin = {true};
                final boolean[] isBeginOutLine = {true};
                final boolean[] isTipsResetZero = {false};
                final boolean[] isTipsOutLine = {false};
                final boolean[] isTipsLightLine = {false};
                final boolean[] isCount = {false, false};
                final boolean[] isStable = {false};
                final long[] time = {0};
                //?????????????????????
                final boolean[] isNegative = {false};

                controlRelay = new ControlRelay(manager, isTurn, inLine, outLine, buzzerLine, CONNECT_TYPE, stateOB,
                        bluetoothRelay);

                bluetoothManager.read(new TransferProgressListener() {
                    @Override
                    public void transfering(int progress) {

                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void transferSuccess(String str) {
                        Logger.d("??????????????????" + str);
                        if (observablePieceWeight != null) {
                            observablePieceWeight.subscribe(new Consumer<Double>() {
                                @Override
                                public void accept(Double aDouble) throws Exception {
                                    compareWeight[0] = aDouble;
                                }
                            });
                        }

                        if (str.length() == 8) {
                            //??????
                            str = new StringBuilder(str).reverse().toString();
                            Logger.d("------------> 0???" + str);
                            //??????????????????=???????????????=???????????????6????????????.????????????
                            if (!str.endsWith("=") && !str.startsWith("=") && str.substring(5, 6).equals(".")) {
                                Logger.d("------------> 1" + str);

                                if (str.startsWith("-")) { //????????????????????????
                                    isNegative[0] = true;
                                    //?????????????????????
                                    str = str.replaceFirst("^-*", "");
                                    //??????????????????
                                    str = str.replaceFirst("^0*", "");
                                    //?????????????????????0?????????????????????0????????????
                                    if (str.startsWith(".")) {
                                        str = "0" + str;
                                    }
                                    //???????????????-???
                                    str = "-" + str;

                                } else { //???????????????
                                    isNegative[0] = false;
                                    //??????????????????
                                    str = str.replaceFirst("^0*", "");
                                    Logger.d("------------> 2" + str);
                                    if (str.startsWith(".")) {
                                        str = "0" + str;
                                    }
                                    Logger.d("------------> 3" + str);
                                }


                                mWeightTextView.setText(str);

//                                byte[] weightByte = CRC16Util.stringToByte(str);

                                if (bleTest != null && !isDisconnectScreen && !isStable[0]) {
//                                    bleScreenConnectionState = ble.isConnected();

                                    if (!isSend[0] && bleScreenConnectionState) {

//                                        myToasty.showSuccess("????????????????????????");
//                                        produceLine.setShowOutConnectionState(true);
                                        //?????????????????????2???
//                                        byte[] point = {0x01, 0x06, 0x00, 0x04, 0x00, 0x02, 0x49, (byte) 0xCA};
//                                        ble.send(point);
                                        isSend[0] = true;

                                    }
//                                    ble.send(weightByte);//ModBus??????

                                    /**   $001,2.91#   ??????2.91
                                     *    $001,01&     ??????YES
                                     *    ASCII???  b1,b3,b4??????????????????
                                     *    b2  ??????
                                     */

//                                    byte[] b1 = {0x24, 0x30, 0x30, 0x31, 0x2C};
//                                    byte[] b3 = {0x23};
                                    byte[] b4 = {0x30, 0x31, 0x26};
                                    byte[] b2 = new byte[0];
                                    try {
                                        b2 = str.getBytes();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    b2 = CRC16Util.addBytes(b1, b2);
                                    b3 = CRC16Util.addBytes(b2, b3);
                                    b4 = CRC16Util.addBytes(b1, b4);
                                    bleTest.sendData(b3);
                                }


                                //?????????????????????????????????????????????????????????????????????????????????
                                if (isZeroStart && isBegin[0]) {
                                    weightList.add(str);
                                    int weightSize = weightList.size();
                                    if (StringUtils.isStable(weightList, 10)) { //????????????10?????????????????????????????????????????????

                                        try {
                                            if (Double.valueOf(weightList.get(weightSize - 1)) != 0d) {

                                                if (!isTipsResetZero[0]) { //?????????????????????
                                                    //?????????????????????5?????????????????????
                                                    myToasty.showWarning("????????????????????????");
                                                    beginDisposable = tips("????????????????????????");
                                                    isTipsResetZero[0] = true;
                                                    circleTextView.setClickable(false);
                                                }

                                            } else {
                                                //???????????????0,??????????????????
                                                isBegin[0] = false;
                                                circleTextView.setClickable(true);
                                                if (beginDisposable != null && !beginDisposable.isDisposed()) {
                                                    beginDisposable.dispose();
                                                    beginDisposable = null;
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return;
                                }

                                if (hasRelayAuto) {
                                    /**
                                     * ??????????????????????????????????????????????????????,??????????????????????????????
                                     */

                                    if (isBeginOutLine[0] && ((manager != null && manager.isConnect()) || (bluetoothRelay != null && bluetoothRelay.isConnect()))) {
                                        //?????????????????????????????????????????????????????????????????????
                                        Observable.timer(zero, TimeUnit.SECONDS)
                                                .subscribe(new Consumer<Long>() {
                                                    @Override
                                                    public void accept(Long aLong) throws Exception {
                                                        controlRelay.turnOnInLine();
                                                    }
                                                });
                                        isBeginOutLine[0] = false;
                                    }
                                }

                                double weight;
                                try {
                                    if (isNegative[0]) {
                                        //?????????????????????
                                        str = str.replaceFirst("^-*", "");
                                        weight = -Double.parseDouble(str);
                                    } else {
                                        weight = Double.parseDouble(str);
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    return;
                                }

                                if (weight > compareWeight[0]) {

                                    //?????????????????????????????????(?????????),??????????????????????????????????????????
                                    if (hasRelayAuto) {
                                        controlRelay.turnOffInLine();
                                    }

                                    strWeightList.add(str);
                                    int size = strWeightList.size();

                                    if (StringUtils.isStable(strWeightList, 10)) { //????????????10?????????????????????????????????????????????
                                        isStable[0] = true;
                                        if (!isWrite[0]) {

                                            if (hasRelayAuto || withoutRelayAuto) { //??????

                                                //??????????????????????????????????????????????????????,????????????????????????
                                                if (!isSpeak[0]) {
                                                    //????????????,???????????????????????????????????????
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        tts.speak(strWeightList.get(size - 1), TextToSpeech.QUEUE_FLUSH, null, "UniqueID");
                                                    }
                                                    isSpeak[0] = true;
                                                    isWrite[0] = true;
                                                    //??????
                                                    saveWeight(strWeightList.get(size - 1));
                                                    //openAssetMusics();//????????????????????????
                                                    /**
                                                     * ???????????????
                                                     * ???????????????????????????????????????
                                                     * ??????B???save???????????????3?????????
                                                     * ??????3????????????????????????????????????2????????????
                                                     * ??????????????????C???out???????????????2????????????
                                                     */
                                                    //??????3?????????
                                                    controlRelay.turnOnLightLine();
                                                }

                                                //3????????????????????????save,??????3???
                                                Observable.timer(save, TimeUnit.SECONDS)
                                                        .subscribe(new Consumer<Long>() {
                                                            @Override
                                                            public void accept(Long aLong) throws Exception {
                                                                controlRelay.turnOffLightLine();
                                                            }
                                                        });

                                                //TODO ??????3???????????????
                                                if (buzzerLineState) {
                                                    isWrite[0] = false;
                                                    save = 0;
                                                    if (!isTipsLightLine[0]) {
                                                        isTipsLightLine[0] = true;
                                                        myToasty.showWarning("?????????????????????????????????");
                                                        finalDisposable = tips("?????????????????????????????????");
                                                    }
                                                    return;
                                                }
                                                save = tempSave;
                                                isWrite[0] = true;
                                                isTipsLightLine[0] = false;
                                                if (finalDisposable != null && !finalDisposable.isDisposed()) {
                                                    finalDisposable.dispose();
                                                    finalDisposable = null;
                                                }

                                                if (hasRelayAuto) {
                                                    //3??????????????????????????????100??????(??????????????????????????????????????????????????????????????????)???
                                                    //??????2???????????????????????????????????????
                                                    Observable.timer(200, TimeUnit.MILLISECONDS)
                                                            .subscribe(new Consumer<Long>() {
                                                                @Override
                                                                public void accept(Long aLong) throws Exception {
                                                                    controlRelay.turnOnOutLine();
                                                                }
                                                            });

                                                    //????????????
                                                    currentTimeMillis = System.currentTimeMillis();
                                                    //2?????????????????????out???????????????2???

                                                    //??????save???out????????????????????????????????????????????????????????????????????????out??????200??????
                                                    Logger.d("?????????????????????" + "????????????1");
                                                    Observable.timer(out, TimeUnit.MILLISECONDS)
                                                            .subscribe(new Consumer<Long>() {
                                                                @Override
                                                                public void accept(Long aLong) throws Exception {
                                                                    controlRelay.turnOffOutLine();
                                                                }
                                                            });
                                                }


                                            } else if (hasRelayByHand || withoutRelayByHand) {
                                                //?????????????????????????????????????????????
                                                if (!isSpeak[0]) {
                                                    //????????????,???????????????????????????????????????
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        tts.speak(strWeightList.get(size - 1), TextToSpeech.QUEUE_FLUSH, null, "UniqueID");
                                                    }
                                                    isSpeak[0] = true;
                                                }

                                            }

                                        }

                                    }

                                } else if (weight < compareWeight[0]) {
                                    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????0???
                                    if (isWrite[0]) {
                                        isStable[0] = false;
                                        if (strWeightList.size() > 0) {
                                            strWeightList.clear();
                                        }

                                        strLowWeightList.add(str);

                                        //??????10??????????????????????????????????????????????????????????????????????????????????????????????????????0???
                                        if (StringUtils.isStable(strLowWeightList, 10)) {
                                            isStable[0] = true;
                                            if (hasRelayAuto) { //????????????????????????
                                                Logger.d("?????????????????????" + "????????????1");

                                                if (!isCount[0]) {
                                                    isCount[0] = true;
                                                    // ????????????out - (System.currentTimeMillis() - currentTimeMillis) / 1000?????????????????????????????????
                                                    time[0] = out  - (System.currentTimeMillis() - currentTimeMillis);
                                                    if (time[0] < 0) { //????????????????????????2??????????????????????????????2????????????????????????
                                                        time[0] = 0;
                                                    }
                                                }

                                                if (!isCount[1]) {
                                                    isCount[1] = true;
                                                    Logger.d("?????????????????????" + "????????????2" + time[0]);
                                                    Observable.timer(time[0], TimeUnit.MILLISECONDS)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Consumer<Long>() {
                                                                @Override
                                                                public void accept(Long aLong) throws Exception {
                                                                    time[0] = 0;
                                                                    Logger.d("?????????????????????" + "????????????3" + time[0]);
                                                                    if (isZeroStart) { //????????????
                                                                        Logger.d("?????????????????????" + "????????????4" + time[0]);
                                                                        if (Double.valueOf(strLowWeightList.get(strLowWeightList.size() - 1)) != 0d) {
                                                                            circleTextView.setClickable(false);
                                                                            isCount[1] = false;

                                                                            //?????????????????????????????????????????????
                                                                            if (!isZero[0]) {
                                                                                isZero[0] = true;
                                                                                Logger.d("?????????????????????" + "????????????3");
                                                                                myToasty.showWarning("????????????????????????");
                                                                                finalDisposable = tips("????????????????????????");
                                                                            }
                                                                            return;
                                                                        }

                                                                        //?????????
                                                                        isCount[1] = true;
                                                                        if (finalDisposable != null && !finalDisposable.isDisposed()) {
                                                                            finalDisposable.dispose();
                                                                            finalDisposable = null;
                                                                        }
                                                                        circleTextView.setClickable(true);

                                                                        //??????2??????????????????????????????,2??????????????????out????????????
                                                                        if (outLineState) {
                                                                            isCount[1] = false;
                                                                            if (!isTipsOutLine[0]) { //?????????????????????
                                                                                //?????????????????????5?????????????????????
                                                                                myToasty.showWarning("?????????????????????????????????");
                                                                                beginDisposable = tips("?????????????????????????????????");
                                                                                isTipsOutLine[0] = true;

                                                                            }
                                                                            return;
                                                                        }
                                                                        isTipsOutLine[0] = false;
                                                                        isCount[1] = true;
                                                                        if (beginDisposable != null && !beginDisposable.isDisposed()) {
                                                                            beginDisposable.dispose();
                                                                            beginDisposable = null;
                                                                        }

                                                                        //??????zero???????????????1???????????????????????????????????????
                                                                        Observable.timer(zero, TimeUnit.SECONDS)
                                                                                .subscribe(new Consumer<Long>() {
                                                                                    @Override
                                                                                    public void accept(Long aLong) throws Exception {
                                                                                        controlRelay.turnOnInLine();
                                                                                        Logger.d("?????????????????????" + "?????????7");
                                                                                        isWrite[0] = false;
                                                                                        isTurn[0] = false;
                                                                                        isZero[0] = false;
                                                                                        isSpeak[0] = false;
                                                                                        isCount[0] = false;
                                                                                        isCount[1] = false;
                                                                                        isStable[0] = false;
                                                                                        strLowWeightList.clear();
                                                                                    }
                                                                                });


                                                                    } else {  //???????????????????????????

                                                                        circleTextView.setClickable(true);

                                                                        //??????2??????????????????????????????,2??????????????????out?????????????????????????????????time?????????????????????????????????
                                                                        if (outLineState) {
                                                                            isCount[1] = false;
                                                                            if (!isTipsOutLine[0]) { //?????????????????????
                                                                                //??????5?????????
                                                                                myToasty.showWarning("?????????????????????????????????");
                                                                                beginDisposable = tips("?????????????????????????????????");
                                                                                isTipsOutLine[0] = true;
                                                                            }
                                                                            return;
                                                                        }
                                                                        isCount[1] = true;
                                                                        isTipsOutLine[0] = false;
                                                                        if (beginDisposable != null && !beginDisposable.isDisposed()) {
                                                                            beginDisposable.dispose();
                                                                            beginDisposable = null;
                                                                        }

                                                                        //??????zero???????????????1???????????????????????????????????????
                                                                        Observable.timer(zero, TimeUnit.SECONDS)
                                                                                .subscribe(new Consumer<Long>() {
                                                                                    @Override
                                                                                    public void accept(Long aLong) throws Exception {
                                                                                        controlRelay.turnOnInLine();
                                                                                        isWrite[0] = false;
                                                                                        isTurn[0] = false;
                                                                                        isZero[0] = false;
                                                                                        isSpeak[0] = false;
                                                                                        isCount[0] = false;
                                                                                        isCount[1] = false;
                                                                                        isStable[0] = false;
                                                                                        strLowWeightList.clear();
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });

                                                }


                                            } else { //????????????

                                                //????????????????????????????????????????????????????????????????????????????????????
                                                if (isZeroStart) { //????????????

                                                    if (Double.valueOf(strLowWeightList.get(strLowWeightList.size() - 1)) != 0d) {
                                                        circleTextView.setClickable(false);
                                                        //?????????????????????????????????????????????
                                                        if (!isZero[0]) {
                                                            isZero[0] = true;

                                                            myToasty.showWarning("????????????????????????");
                                                            finalDisposable = tips("????????????????????????");
                                                        }
                                                        return;
                                                    }

                                                    //?????????
                                                    if (finalDisposable != null && !finalDisposable.isDisposed()) {
                                                        finalDisposable.dispose();
                                                        finalDisposable = null;
                                                    }

                                                    circleTextView.setClickable(true);
                                                    isWrite[0] = false;
                                                    isZero[0] = false;
                                                    isSpeak[0] = false;
                                                    isStable[0] = false;
                                                    strLowWeightList.clear();

                                                } else {

                                                    circleTextView.setClickable(true);
                                                    isWrite[0] = false;
                                                    isZero[0] = false;
                                                    isSpeak[0] = false;
                                                    isStable[0] = false;
                                                    strLowWeightList.clear();

                                                }

                                            }


                                        }

                                    }

                                }

                            }
                        }
                    }

                    @Override
                    public void transferFailed(Exception exception) {
                        if (!"socket closed".equals(exception.getMessage())) {
                            myToasty.showError("?????????????????????????????????,??????????????????");
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


    //?????????????????????
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
                    myToasty.showError("???????????????????????????,??????????????????");
                    qmuiTipDialog.dismiss();
                }

                @Override
                public void onComplete() {
                    qmuiTipDialog.dismiss();
                    /**
                     * ????????????????????????100?????????????????????????????????
                     */
                    getWeight(bluetoothBean);
                }
            });
        }
    }

    @SuppressLint("CheckResult")
    private void getWeight(BluetoothBean bluetoothBean) {
        /**
         * ????????????????????????100?????????????????????????????????
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
                                    Logger.d("????????????");
                                    compositeDisposable.add(d);
                                }
                            }

                            @Override
                            public void onNext(String s) {

                                if (s.length() == 8) {
                                    //??????????????????=????????????
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
                                Logger.d("????????????:" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                    }
                });

    }

    //???????????????
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
                        myToasty.showSuccess("??????????????????????????????");
                        produceLine.setRelayConnectionState(true);
                        blueRelayConnectionState = true;
                        bluetoothBean.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);
                        //??????????????????
//                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(inLine)).subscribe(stateOB);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    myToasty.showError("??????????????????????????????");
                    qmuiTipDialog.dismiss();
                }

                @Override
                public void onComplete() {
                    //manager.send(new WriteData(Order.TURN_ON_1));
                }
            });
        }
    }

    //????????????????????????
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
                        Logger.d("toHexStringForLog" + BytesUtils.toHexStringForLog(bytes) + "----");
                        String message = BytesUtils.toHexStringForLog(bytes);

                        if (message.indexOf("01 05 00 ") == 0) {
                            //????????????
                            String index = message.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            String state = message.substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---" + index + "---" + state + "---");

                            //???????????????
                            int whichSwitch = Integer.parseInt(index.substring(1, 2));

                            if (whichSwitch == inLine) {
                                if (state.equals("00")) {
                                    tvInLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                                    tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                                    inLineState = false;//??????
                                } else {
                                    tvInLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                                    tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                                    inLineState = true;//??????
                                }

                            }

                            if (whichSwitch == outLine) {
                                if (state.equals("00")) {
                                    tvOutLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                                    tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                                    outLineState = false;//??????
                                } else {
                                    tvOutLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                                    tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                                    outLineState = true;//??????
                                }
                                Logger.d("toHexStringForLog" + outLineState + "----");

                            }

                            if (whichSwitch == buzzerLine) {
                                if (state.equals("00")) {
                                    buzzerLineState = false;
                                } else {
                                    buzzerLineState = true;
                                }
                            }

                            if (state.equals("00")) {
                                mWifiList.get(whichSwitch).setState("0");//??????
                            } else {
                                mWifiList.get(whichSwitch).setState("1");//??????
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


    //??????Wifi?????????
    private void connectWifi(Device device) {

        initWifiDevice(device);

        if (manager != null) {
            manager.connect();
        }
    }

    /**
     * ?????????wifi?????????
     */
    private void initWifiDevice(Device device) {
        String ip = device.getWifiIp();
        int port = device.getWifiPort();
        MyOkSocket okSocket = new MyOkSocket(ip, port, new CallBack() {
            @Override
            public void onConnectionSuccess() {
                myToasty.showSuccess("Wifi????????????????????????");
                wifiConnectionState = true;
                produceLine.setRelayConnectionState(true);
                manager.send(new WriteData(Order.GET_STATE));
                manager.send(new WriteData(Order.TURN_OFF_ALL));
                //???????????????????????????????????????
//                manager.send(new WriteData(Order.getTurnOn().get(inLine)));

            }

            @Override
            public void onReceived(String msg) {
                if (msg.indexOf("01 01 02 ") == 0) {
                    //????????????
                    String state = msg.substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                    String binaryState = StringUtils.hexString2binaryString(state);
                    char[] bin = binaryState.toCharArray();
                    if (bin.length == 8) {
                        for (int i = 0; i < bin.length; i++) {
                            mWifiList.get(i).setState(bin[i] + "");
                        }
                    }
                } else if (msg.indexOf("01 05 00 ") == 0) {
                    //????????????
                    /**
                     * 01 05 00 0X XX 00
                     * 1.01 05 00 ?????????
                     * 2.0X  X:??????????????????
                     * 3.XX  ???????????????00????????????  FF????????????
                     */
                    // 0X ???????????????
                    String index = msg.substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                    //???????????????
                    String state = msg.substring("01 05 00 ".length() + 3,
                            "01 05 00 ".length() + 5);
                    Logger.d("---" + index + "---" + state + "---");
                    //???????????????
                    int whichSwitch = Integer.parseInt(index.substring(1, 2));

                    if (whichSwitch == inLine) {
                        if (state.equals("00")) {
                            tvInLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                            tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                            inLineState = false;//??????
                        } else {
                            tvInLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                            tvInLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                            inLineState = true;//??????
                        }
                    }

                    if (whichSwitch == outLine) {
                        if (state.equals("00")) {
                            tvOutLine.setBackground(getResources().getDrawable(R.drawable.red_background));
                            tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                            outLineState = false;//??????
                        } else {
                            tvOutLine.setBackground(getResources().getDrawable(R.drawable.green_background));
                            tvOutLine.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                            outLineState = true;//??????
                        }
                    }

                    if (whichSwitch == buzzerLine) {
                        if (state.equals("00")) {
                            buzzerLineState = false;
                        } else {
                            buzzerLineState = true;
                        }
                    }

                    /*
                     * ????????????????????????????????????
                     */
                    if (state.equals("00")) {
                        mWifiList.get(whichSwitch).setState("0");
                    } else {
                        mWifiList.get(whichSwitch).setState("1");
                    }
                }
                switchAdapter.notifyDataSetChanged();
                Logger.d("??????:" + msg);
            }

            @Override
            public void onDisconnection(Exception e) {
                myToasty.showWarning("Wifi???????????????????????????");
                produceLine.setRelayConnectionState(false);
            }

            @Override
            public void onConnectionFailed(Exception e) {
                myToasty.showError("Wifi????????????????????????");
            }
        });

        manager = okSocket.getManager();
        listener = okSocket.getiSocketActionListener();

    }


    //?????????????????????????????????
    private void registerReceiver() {

        pinBlueReceiver = new PinBlueReceiver(new PinBlueCallBack() {
            @Override
            public void onBondRequest() {

            }

            @Override
            public void onBondFail(BluetoothDevice device) {
                myToasty.showError("???????????????????????????");
            }

            @Override
            public void onBonding(BluetoothDevice device) {

            }

            @Override
            public void onBondSuccess(BluetoothDevice device) {

                myToasty.showInfo("?????????????????????????????????");

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
            case R.id.id_iv_choose_matter://????????????
                Intent matterIntent = new Intent(MainActivity.this, ChooseMatterActivity.class);

                if (samplingSize > 0) {
                    isContinue(matterIntent);
                } else {
                    startActivity(matterIntent);
                }

                break;

            case R.id.id_iv_choose_matter_level://??????????????????
                Intent matterLevelIntent = new Intent(MainActivity.this, ChooseMatterLevelActivity.class);

                if (samplingSize > 0) {
                    isContinue(matterLevelIntent);
                } else {
                    startActivity(matterLevelIntent);
                }

                break;

            case R.id.id_tv_choose_supplier://???????????????
                Intent supplierIntent = new Intent(MainActivity.this, ChooseSupplierActivity.class);

                if (samplingSize > 0) {
                    isContinue(supplierIntent);
                } else {
                    startActivity(supplierIntent);
                }

                break;

            case R.id.id_iv_choose_specs://????????????
                startActivity(new Intent(MainActivity.this, ChooseSpecsActivity.class));
                break;

            case R.id.tv_produce_line://???????????????

                if (supplier != null && matterId > 0 && matterLevelId > 0) {
                    ProduceLineListFragment produceLineListFragment = new ProduceLineListFragment();
                    showDialogFragment(produceLineListFragment, PRODUCE_LINE_LIST);
                } else {
                    myToasty.showWarning("????????????????????????????????????????????????");
                }

                break;

            case R.id.iv_relay_model://?????????????????????
                QMUIUtils.showNormalPopup(MainActivity.this, ivRelayModel,
                        QMUIPopup.DIRECTION_BOTTOM, "???????????????", -100);

                break;

            case R.id.iv_save_model://??????????????????
                QMUIUtils.showNormalPopup(MainActivity.this, ivSaveModel,
                        QMUIPopup.DIRECTION_TOP, "????????????", -100);
                break;

            case R.id.tv_relay_automatic://?????????????????????
                //??????????????????????????????????????????????????????
                hasRelayAuto = true;
                hasRelayByHand = false;
                withoutRelayAuto = false;
                withoutRelayByHand = false;

                tvRelayAutomatic.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvRelayAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                tvRelayHand.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvRelayHand.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                tvSaveAutomatic.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvSaveAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                tvSaveHand.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvSaveHand.setTextColor(getResources().getColor(R.color.qmui_config_color_white));

                tvInLine.setClickable(false);
                tvOutLine.setClickable(false);
                tvSaveAutomatic.setClickable(false);
                tvSaveHand.setClickable(false);

                /**
                 * ???????????????
                 * ????????????????????????
                 */


                break;

            case R.id.tv_relay_hand://?????????????????????
                //??????????????????????????????????????????????????????

                hasRelayAuto = false;
                hasRelayByHand = true;
                withoutRelayAuto = false;
                withoutRelayByHand = false;

                tvRelayAutomatic.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvRelayAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                tvRelayHand.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvRelayHand.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                tvSaveAutomatic.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvSaveAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                tvSaveHand.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvSaveHand.setTextColor(getResources().getColor(R.color.qmui_config_color_black));

                tvRelayAutomatic.setClickable(true);
                tvRelayHand.setClickable(true);
                tvInLine.setClickable(true);
                tvOutLine.setClickable(true);
                tvSaveAutomatic.setClickable(false);
                tvSaveHand.setClickable(false);

                /**
                 * ???????????????
                 * ?????????????????????
                 * ?????????????????????????????????????????????????????????
                 */

                break;

            case R.id.tv_in_line://?????????

                if (controlRelay != null) {
                    if (inLineState) {
                        controlRelay.turnOffInLineWithOutIsTurn();
                    } else {
                        controlRelay.turnOnInLine();
                    }
                }
                break;

            case R.id.tv_out_line://?????????

                if (controlRelay != null) {
                    if (outLineState) {
                        controlRelay.turnOffOutLine();
                    } else {
                        controlRelay.turnOnOutLine();
                    }
                }
                break;

            case R.id.tv_save_automatic://??????????????????
                hasRelayAuto = false;
                hasRelayByHand = false;
                withoutRelayAuto = true;
                withoutRelayByHand = false;

                tvSaveAutomatic.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvSaveAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_black));
                tvSaveHand.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvSaveHand.setTextColor(getResources().getColor(R.color.qmui_config_color_white));

                break;

            case R.id.tv_save_hand://??????????????????
                hasRelayAuto = false;
                hasRelayByHand = false;
                withoutRelayAuto = false;
                withoutRelayByHand = true;

                tvSaveAutomatic.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvSaveAutomatic.setTextColor(getResources().getColor(R.color.qmui_config_color_white));
                tvSaveHand.setBackground(getResources().getDrawable(R.drawable.green_background));
                tvSaveHand.setTextColor(getResources().getColor(R.color.qmui_config_color_black));

                break;

            case R.id.id_tv_sampling://??????

                if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) { //?????????
                    //?????????
                    if (supplier != null && matterId > 0 && matterLevelId > 0) {
                        SamplingFragment samplingFragment = SamplingFragment.newInstance(samplingWeight, supplierId,
                                matterId, matterLevelId);
                        showDialogFragment(samplingFragment, SAMPLING);
                    } else {
                        myToasty.showWarning("????????????????????????????????????????????????");
                    }

                } else {//?????????

                    if (isSetSapmle) {//????????????????????????
                        try {
                            samplingDevice =
                                    BluetoothAdapter.getDefaultAdapter().getRemoteDevice(sampleMacAddress);
                            connectAndGetBluetoothScale(samplingDevice);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myToasty.showInfo(e.getMessage());
                        }
                    } else {
//                        myToasty.showInfo("???????????????????????????????????????????????????");

                        if (SAMPLING_MODE == 0) {
                            String[] items = new String[2];
                            items[0] = "??????????????????";
                            items[1] = "???????????????????????????";
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
                                                myToasty.showWarning("????????????????????????????????????????????????");
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
                                        myToasty.showWarning("????????????????????????????????????????????????");
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


            case R.id.id_tv_sampling_count://????????????

                SamplingDetailsFragment samplingDetailsFragment = new SamplingDetailsFragment();

                showDialogFragment(samplingDetailsFragment, SAMPLING_DETAILS);
                break;

            case R.id.id_tv_deduction_cumulative://????????????

                CumulativeFragment deductionCumulativeFragment = CumulativeFragment.newInstance(1);

                showDialogFragment(deductionCumulativeFragment, CUMULATIVE);
                break;

            case R.id.id_tv_cumulative://????????????

                CumulativeFragment cumulativeFragment = CumulativeFragment.newInstance(2);

                showDialogFragment(cumulativeFragment, CUMULATIVE);
                break;

            case R.id.id_tv_deduction://??????
                DeductionFragment deductionFragment =
                        DeductionFragment.newInstance(mWeightTextView.getText().toString());
                showDialogFragment(deductionFragment, DEDUCTION);
                break;

            case R.id.id_tv_deductionMix://?????????
                final EditText editText1 = new EditText(MainActivity.this);
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(MainActivity.this);
                inputDialog1.setTitle("?????????????????????%???").setView(editText1);
                inputDialog1.setPositiveButton(R.string.enter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText1.getText().toString().trim().length() == 0) {
                                    myToasty.showInfo("??????????????????");
                                    return;
                                }
                                //?????????
                                deductionMix = Integer.valueOf(editText1.getText().toString().trim());

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.id_iv_print://??????(????????????)

                BillListFragment billListFragment = new BillListFragment();

                showDialogFragment(billListFragment, BILL_LIST);
                break;

            case R.id.id_tv_piece_weight://??????
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

            case R.id.id_iv_setting://??????????????????

                SettingFragment settingFragment = new SettingFragment();

                showDialogFragment(settingFragment, SETTING);
                break;

            case R.id.id_iv_takePicture://????????????
//                takePicture();
                if (produceLine == null) {
                    myToasty.showWarning("????????????????????????????????????????????????");
                    return;
                }

                Intent it = new Intent(MainActivity.this, DeviceListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("produceLine", produceLine);
                it.putExtras(bundle);
                startActivity(it);

                break;

            case R.id.id_tv_save_bill://????????????

                if (Double.valueOf(tvCumulativeWeight.getText().toString().trim()) <= 0) {
                    myToasty.showWarning("?????????????????????");
                    return;
                }
                List<SamplingDetails> list = LitePal.where("hasBill < ?", "0").find(SamplingDetails.class);
                int samplingSize = list.size();

                if (samplingSize <= 0) {
                    //?????????
                    myToasty.showWarning("???????????????");

//                    //?????????????????????????????????????????????????????????????????????
//                    if (supplier == null || matterId <= 0 || matterLevelId <= 0 || specs == null) { //????????????????????????
//                        myToasty.showWarning("?????????????????????????????????????????????????????????");
//                        return;
//                    }
//
//                    //???????????????????????????????????????????????????
//
//                    Intent intent1 = SaveBillWithoutSamplingActivity.newInstance(MainActivity.this,
//                            deductionMix, tvCumulativeWeight.getText().toString().trim(), supplierId, matterId,
//                            matterLevelId, specsId);
//
//                    startActivity(intent1);

                } else {
                    //?????????
                    int id = list.get(0).getMatterId();
                    Matter matter = LitePal.find(Matter.class, id);
                    int type = matter.getValuationType();
                    /**
                     * ????????????
                     * ValuationType = 1;??????????????????
                     * ValuationType = 2;????????????????????????
                     */
                    if (type == 1) {
                        //??????????????????
                        //??????????????????????????????????????????????????????
                        List<SamplingBySpecs> samplingBySpecsList =
                                LitePal.where("hasBill < ?", "0").find(SamplingBySpecs.class);
                        if (samplingBySpecsList.size() <= 0) {
                            //?????????
                            myToasty.showWarning("???????????????????????????????????????????????????");
                            return;
                        }
                    }

                    Intent intent2 = SaveBillDetailsActivity.newInstance(MainActivity.this,
                            deductionMix, tvCumulativeWeight.getText().toString().trim());
                    startActivity(intent2);
                }

                break;

            case R.id.id_tv_hand://????????????

                double weight = Double.parseDouble(mWeightTextView.getText().toString());
                if (weight > 0d) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("?????????????????????")
                            .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Cumulative cumulative = new Cumulative();
                                    cumulative.setCategory("??????");
                                    cumulative.setWeight(mWeightTextView.getText().toString());

                                    //?????????????????? HH:mm:ss
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
                                    Logger.d("??????");
                                }
                            }).show();

                } else {
                    myToasty.showWarning("????????????????????????????????????");
                }

                break;

            default:
                break;
        }
    }

    private void showDialogFragment(DialogFragment dialogFragment, final String Tag) {
        //?????????????????????????????????fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //?????????????????????
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Tag);
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, Tag);
    }

    /**
     * ??????
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
                        //?????????????????????????????????
                        //showToast("?????????????????????");
                    } else {
                        //?????????????????????????????????????????????????????????
                        //showToast("???????????????????????????????????????");
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //wifi?????????
        if (manager != null) {
            //manager.send(new WriteData(Order.TURN_OFF_ALL));
            manager.unRegisterReceiver(listener);
            manager = null;
        }
        //???????????????
        if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
            bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);
            bluetoothRelay.getMyBluetoothManager().disConnect();
            bluetoothRelay = null;
        }
        //???????????????
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
        //??????
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
//            samplingFragment.onActivityResult(requestCode, resultCode, data);//???DialogFragment?????????????????????
//        }

        //????????????
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

    //???????????????????????????
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

    //??????
    private void saveWeight(String weight) {

        Cumulative cumulative = new Cumulative();
        cumulative.setCategory("??????");
        cumulative.setWeight(weight);

        //?????????????????? HH:mm:ss
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

        tvFirst.setText(tvSecond.getText());
        tvSecond.setText(tvThird.getText());
        tvThird.setText(weight);

    }

    //??????????????????????????????????????????????????????????????????????????????
    //????????????????????????????????????
    private void isContinue(Intent intent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("???????????????????????????????????????")
                .setMessage("??????????????????????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                        Logger.d("??????");
                    }
                }).show();
    }

    /**
     * ?????????????????????????????????
     */
    @SuppressLint("CheckResult")
    private Disposable tips(String msg) {

        Disposable disposable = Flowable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return true;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        myToasty.showWarning(msg);
                    }
                });
        return disposable;
    }


}
