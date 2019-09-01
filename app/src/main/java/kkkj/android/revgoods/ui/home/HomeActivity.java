package kkkj.android.revgoods.ui.home;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.suke.widget.SwitchButton;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SwitchAdapter;
import kkkj.android.revgoods.bean.Bill;
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
import kkkj.android.revgoods.conn.classicbt.BleManager;
import kkkj.android.revgoods.conn.classicbt.BluetoothPermissionHandler;
import kkkj.android.revgoods.conn.classicbt.listener.BluetoothPermissionCallBack;
import kkkj.android.revgoods.conn.classicbt.listener.PinResultListener;
import kkkj.android.revgoods.conn.socket.WriteData;
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
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.ui.ChooseMatterLevelActivity;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterActivity;
import kkkj.android.revgoods.ui.chooseSpecs.ChooseSpecsActivity;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierActivity;
import kkkj.android.revgoods.ui.home.model.DeviceBean;
import kkkj.android.revgoods.ui.saveBill.SaveBillDetailsActivity;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.tv_produce_line)
    TextView mTvProduceLine;
    @BindView(R.id.tv_matter_level)
    TextView mTvMatterLevel;
    @BindView(R.id.id_iv_choose_matter_level)
    ImageView mIvChooseMatterLevel;
    @BindView(R.id.id_tv_hand)
    TextView mTvHand;
    @BindView(R.id.tv_cumulative_weight)
    TextView mTvCumulativeWeight;
    @BindView(R.id.id_iv_choose_specs)
    ImageView mIvChooseSpecs;
    @BindView(R.id.id_tv_save_bill)
    TextView mTvSaveBill;
    @BindView(R.id.id_tv_is_upload)
    TextView mTvIsUpload;
    @BindView(R.id.tv_matter)
    TextView mTvMatter;
    @BindView(R.id.tv_specs)
    TextView mTvSpecs;
    @BindView(R.id.id_tv_sampling_number)
    TextView mTvSamplingNumber;
    @BindView(R.id.id_tv_cumulative)
    TextView mTvCumulative;
    @BindView(R.id.id_tv_deduction_cumulative)
    TextView mTvDeductionCumulative;
    @BindView(R.id.tv_cumulative_count)
    TextView mTvCumulativeCount;
    @BindView(R.id.id_tv_sampling_count)
    TextView mTvSamplingCount;
    @BindView(R.id.id_tv_weight)
    TextView mTvWeight;
    @BindView(R.id.id_tv_choose_supplier)
    ImageView mTvChooseSupplier;
    @BindView(R.id.id_iv_choose_printer)
    ImageView mIvChoosePrinter;
    @BindView(R.id.id_iv_print)
    ImageView mIvPrint;
    @BindView(R.id.id_iv_takePicture)
    ImageView mIvTakePicture;
    @BindView(R.id.id_iv_choose_matter)
    ImageView mIvChooseMatter;
    @BindView(R.id.tv_supplier)
    TextView mTvSupplier;
    @BindView(R.id.id_tv_deduction)
    TextView mTvDeduction;
    @BindView(R.id.id_tv_deductionMix)
    TextView mTvDeductionMix;
    @BindView(R.id.id_tv_piece_weight)
    TextView mTvPieceWeight;
    @BindView(R.id.id_tv_sampling)
    TextView mTvSampling;
    @BindView(R.id.id_tv_hand_switch)
    SwitchButton mTvHandSwitch;
    @BindView(R.id.id_wifiRelay_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_tv_show_piece_weight)
    TextView mTvShowPieceWeight;
    @BindView(R.id.id_iv_setting)
    ImageView mIvSetting;

    //扣重率
    private int deductionMix = 0;

    private BluetoothPermissionHandler permissionHandler;
    private List<RelayBean> mRelayList;
    private SwitchAdapter switchAdapter;


    private static final int MAIN_SCALE = 0;

    private static final int BLUETOOTH_RELAY = 1;

    private static final int WIFI_RELAY = 2;

    private static final int SAMPLING_SCALE = 3;

    private static final int BLE_SCREEN = 4;

    private static final String SAMPLING = "samplingFragment";
    private static final String SAMPLING_DETAILS = "samplingDetailsFragment";
    private static final String CUMULATIVE = "cumulativeFragment";
    private static final String DEDUCTION = "deductionFragment";
    private static final String DEVICE_LIST = "deviceFragment";
    private static final String BILL_LIST = "billListFragment";
    private static final String SETTING = "settingFragment";
    private static final String SAVE_BILL = "saveBillFragment";
    private static final String PRODUCE_LINE_LIST = "produceLineListFragment";

    /**
     * 继电器连接类型
     * 1-Wifi继电器
     * 2-蓝牙继电器
     */
    private static int CONNECT_TYPE = -1;


    @Override
    protected int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(switchAdapter);

    }

    @Override
    protected void initData() {
        mRelayList = new ArrayList<>();

        //蓝牙电子秤
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

        switchAdapter = new SwitchAdapter(R.layout.item_switch, mRelayList);
        switchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (CONNECT_TYPE) {
                    /**
                     * Wifi继电器
                     */
                    case 1:
//                        if (manager != null && manager.isConnect()) {
//                            if (isClickable) {
//                                switch (view.getId()) {
//                                    case R.id.iv_switch_left:
//                                        manager.send(new WriteData((Order.getTurnOn().get(position))));
//                                        break;
//
//                                    case R.id.iv_switch_right:
//                                        manager.send(new WriteData(Order.getTurnOff().get(position)));
//                                        break;
//                                }
//                            } else {
//                                myToasty.showInfo("请先打开手动开关！");
//                            }
//                        } else {
//                            myToasty.showInfo("继电器未连接，请先连接继电器！");
//                        }
                        break;

                    case 2:
                        /**
                         * 蓝牙继电器
                         */
//                        if (bluetoothRelay != null && bluetoothRelay.isConnect()) {

                            if (true) {

                                switch (view.getId()) {
                                    case R.id.iv_switch_left:
                                        mPresenter.sendBluetoothRelayData(BTOrder.getTurnOn().get(position));
//                                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOn().get(position)).subscribe(stateOB);

                                        break;

                                    case R.id.iv_switch_right:
                                        mPresenter.sendBluetoothRelayData(BTOrder.getTurnOff().get(position));
//                                        bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.getTurnOff().get(position)).subscribe(stateOB);

                                        break;

                                    default:
                                        break;
                                }
                            } else {
                                myToasty.showWarning("请先打开手动开关！");
                            }

//                        } else {
//                            myToasty.showWarning("继电器未连接，请先连接继电器！");
//                        }

                        break;

                    default:
                        break;
                }


            }
        });

    }

    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ConnectDevice(DeviceEvent deviceEvent) {
        Device device = deviceEvent.getDevice();

        //更新间隔时间
//        if (deviceEvent.getIntervalTime() > 0) {
//            intervalTime = deviceEvent.getIntervalTime();
//        }
//
//        //更新累计数+1
//        if (deviceEvent.isAdd()) {
//            int count = Integer.parseInt(tvCumulativeCount.getText().toString());
//            count = count + 1;
//            tvCumulativeCount.setText(String.valueOf(count));
//        }
//        //更新采样数
//        if (deviceEvent.getSamplingNumber() >= 0) {
//            mSamplingNumber.setText("(" + deviceEvent.getSamplingNumber() + ")");
//        }
//        //更新供应商
//        if (deviceEvent.getSupplierId() >= 0) {
//            supplierId = deviceEvent.getSupplierId();
//            supplier = LitePal.find(Supplier.class, supplierId);
//            mTvSupplier.setText(supplier.getName());
//            //刷新生产线
////            produceLineList.clear();
////            produceLines.clear();
////
////            produceLineList.add(getResources().getString(R.string.choose_produce_line));
////            produceLineList.add("移动称重");
////
////            produceLines = LitePal.findAll(ProduceLine.class);
////            for (int i = 0; i < produceLines.size(); i++) {
////                produceLineList.add(produceLines.get(i).getName());
////            }
////            spinnerAdapter.notifyDataSetChanged();
//
//        }
//        //更新品类（物料）
//        if (deviceEvent.getMatterId() >= 0) {
//
//            matterId = deviceEvent.getMatterId();
//            matter = LitePal.find(Matter.class, matterId);
//            mTvMatter.setText(matter.getName());
//
//            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER, matterId);
//        }
//        //更新品类等级
//        if (deviceEvent.getMatterLevelId() >= 0) {
//
//            matterLevelId = deviceEvent.getMatterLevelId();
//            matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
//            mTvMatterLevel.setText(matterLevel.getName());
//
//            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_MATTER_LEVEL, matterLevelId);
//        }
//        //更新规格
//        if (deviceEvent.getSpecsId() >= 0) {
//            specs = new Specs();
//            specsId = deviceEvent.getSpecsId();
//            specs = LitePal.find(Specs.class, specsId);
//            mTvSpecs.setText(specs.getName());
//        }
//
//        //保存单据后数据重置
//        if (deviceEvent.isReset()) {
//            mSamplingNumber.setText("(0)");
//            tvCumulativeCount.setText("0");
//            tvCumulativeWeight.setText("0");
//
//            total = total + 1;
//            //大于等于0，已上传
//            isUpload = LitePal.where("isUpload >= ?", "0").find(Bill.class).size();
//
//            isUploadCount = isUpload + "/" + total;
//            mTvIsUpload.setText(isUploadCount);
//
//        }
//        //删除单据后更新
//        if (deviceEvent.isResetUploadCount()) {
//            total = total - 1;
//            //大于等于0，已上传
//            isUpload = LitePal.where("isUpload >= ?", "0").find(Bill.class).size();
//            isUploadCount = isUpload + "/" + total;
//            mTvIsUpload.setText(isUploadCount);
//        }
        //生产线
        if (deviceEvent.getProduceLine() != null) {

            if (SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_PIECE_WEIGHT).length() == 0) {
                myToasty.showInfo("请先配置单重！");
                return;
            }

            ProduceLine produceLine = deviceEvent.getProduceLine();
            mTvProduceLine.setText(produceLine.getName());
            Logger.d(produceLine.toString());

            if (produceLine.getName().equals("移动称重")) {

                //主称
//                if (BleManager.getInstance() != null && bluetoothManager != null) {
//                    BleManager.getInstance().destory();
//                }
//                //wifi继电器
//                if (manager != null) {
//                    manager.send(new WriteData(Order.TURN_OFF_ALL));
//                    manager.unRegisterReceiver(listener);
//                }
//                //蓝牙继电器
//                if (bluetoothRelay != null && bluetoothRelay.getMyBluetoothManager() != null && bluetoothRelay.getMyBluetoothManager().isConnect()) {
//                    bluetoothRelay.getMyBluetoothManager().getWriteOB(BTOrder.TURN_OFF_ALL).subscribe(stateOB);
//                    bluetoothRelay.getMyBluetoothManager().disConnect();
//                }
//
//                mWifiList.clear();
//                //初始化wifi继电器实体类
//                List<Integer> leftIcon = new ArrayList<>(SwitchIcon.getRedIcon());
//                List<Integer> rightIcon = new ArrayList<>(SwitchIcon.getGreenIcon());
//
//                for (int i = 0; i < leftIcon.size(); i++) {
//                    RelayBean relayBean = new RelayBean();
//                    relayBean.setLeftIamgeView(leftIcon.get(i));
//                    relayBean.setRightImageView(rightIcon.get(i));
//                    mWifiList.add(relayBean);
//                }
//                switchAdapter.notifyDataSetChanged();
//
//                mTvHand.setVisibility(View.VISIBLE);
//                Intent intent = ElcScaleActivity.newIntent(MainActivity.this, 0);
//                startActivity(intent);
            } else {
                mTvHand.setVisibility(View.GONE);

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
                    BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                        mPresenter.connectScaleDevice(bluetoothDevice);

                        //connectAndGetBluetoothScale(bluetoothDevice);
//                        if (!blueMainScaleConnectionState) {
//                            connect(bluetoothDevice);
//                        }

                    } else {
                        BleManager.getInstance().pin(bluetoothDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
//                                connect(device);
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

                    int inLine = power.getInLine() - 1;
//                    outLine = power.getOutLine() - 1;

                    switch (power.getDeviceType()) {
                        case 1://蓝牙继电器
                            CONNECT_TYPE = 2;
                            String address1 = power.getDeviceAddr().trim();
                            try {
                                BluetoothDevice bluetoothDevice1 =
                                        BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
                                mPresenter.connectBluetoothRelay(bluetoothDevice1,inLine);
//                                connectBluetoothRelay(bluetoothDevice1);

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
                            Device device1 = new Device();
                            device1.setWifiIp(ip);
                            device1.setWifiPort(port);
//                            connectWifi(device1);
                            break;

                        default:
                            break;
                    }

                }
//
//
//                //采样秤
//                String sapmleString = produceLine.getSapmle();
//                Sapmle sapmle = new Gson().fromJson(sapmleString, Sapmle.class);
//                //是否配置了采样的秤
////                isSetSapmle = (sapmle.getDeviceAddr() != null);
////                if (isSetSapmle) {
////                    sampleMacAddress = sapmle.getDeviceAddr().trim();
////                }
//
//                //显示屏 蓝牙Ble设备
//                String showOutString = produceLine.getShowOut();
//                ShowOut showOut = new Gson().fromJson(showOutString, ShowOut.class);
//                if (!(showOut.getDeviceAddr() == null)) {
//                    String addressBle = showOut.getDeviceAddr().trim();//"D8:E0:4B:FD:31:F6"
//                    try {
//                        BluetoothDevice bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
////                        connectBle(bleDevice);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        myToasty.showInfo(e.getMessage());
//                    }
//                } else {
//                    myToasty.showInfo("当前未配置显示屏！");
//                }
            }


        }

        if (device.getType() >= 0) {
            switch (device.getType()) {
                case 0://蓝牙电子秤

                    String address = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        //connectAndGetBluetoothScale(bluetoothDevice);
//                        connect(bluetoothDevice);
                    } else {
                        BleManager.getInstance().pin(bluetoothDevice, new PinResultListener() {
                            @Override
                            public void paired(BluetoothDevice device) {
//                                connect(device);
                                //connectAndGetBluetoothScale(device);
                            }
                        });
                    }

                    break;
                case 1://蓝牙继电器
//                    CONNECT_TYPE = 2;
                    String address1 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address1);
//                    connectBluetoothRelay(bluetoothDevice1);
                    break;
                case 2://Wifi继电器
//                    CONNECT_TYPE = 1;
//                    connectWifi(device);
                    break;

                case 3://采样连接的电子秤
                    String address2 = device.getBluetoothMac();
                    BluetoothDevice bluetoothDevice2 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address2);
//                    connectAndGetBluetoothScale(bluetoothDevice2);
                    break;

                case 4://蓝牙Ble显示屏 "D8:E0:4B:FD:31:F6"
                    String addressBle = device.getBluetoothMac();
                    BluetoothDevice bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addressBle);
//                    connectBle(bleDevice);


                default:
                    break;
            }
        }

    }

    @OnClick({R.id.id_iv_choose_matter,R.id.id_iv_choose_matter_level,R.id.id_tv_choose_supplier,
            R.id.id_iv_choose_specs, R.id.tv_produce_line,R.id.id_tv_sampling,R.id.id_tv_sampling_count,
            R.id.id_tv_deduction_cumulative,R.id.id_tv_cumulative,R.id.id_tv_deduction,R.id.id_tv_deductionMix,
            R.id.id_iv_choose_printer,R.id.id_iv_print,R.id.id_tv_piece_weight,R.id.id_iv_setting,
            R.id.id_iv_takePicture,R.id.id_tv_save_bill,R.id.id_tv_hand})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_iv_choose_matter://选择品类
                Intent intent = new Intent(HomeActivity.this, ChooseMatterActivity.class);
                startActivity(intent);
                break;

            case R.id.id_iv_choose_matter_level://选择品类等级

                startActivity(new Intent(HomeActivity.this, ChooseMatterLevelActivity.class));

                break;

            case R.id.id_tv_choose_supplier://选择供应商
                startActivity(new Intent(HomeActivity.this, ChooseSupplierActivity.class));
                break;

            case R.id.id_iv_choose_specs://选择规格
                startActivity(new Intent(HomeActivity.this, ChooseSpecsActivity.class));
                break;

            case R.id.tv_produce_line://选择生产线
                ProduceLineListFragment produceLineListFragment = new ProduceLineListFragment();

                showDialogFragment(produceLineListFragment, PRODUCE_LINE_LIST);

                break;

            case R.id.id_tv_sampling://采样

//                if (bluetoothScale != null && bluetoothScale.getMyBluetoothManager() != null && bluetoothScale.getMyBluetoothManager().isConnect()) { //已连接
//                    //已连接
//                    if (supplier != null && matterId > 0 && matterLevelId > 0) {
//                        SamplingFragment samplingFragment = SamplingFragment.newInstance(samplingWeight, supplierId,
//                                matterId, matterLevelId);
//                        showDialogFragment(samplingFragment, SAMPLING);
//                    } else {
//                        myToasty.showWarning("请先选择供应商，品类，品类等级！");
//                    }
//
//                } else {//未连接
//
//                    if (isSetSapmle) {//是否配置了采样秤
//                        try {
//                            BluetoothDevice bluetoothDevice2 =
//                                    BluetoothAdapter.getDefaultAdapter().getRemoteDevice(sampleMacAddress);
//                            connectAndGetBluetoothScale(bluetoothDevice2);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            myToasty.showInfo(e.getMessage());
//                        }
//                    } else {
////                        myToasty.showInfo("未配置采样秤，请选择要进行的操作！");
//
//                        if (SAMPLING_MODE == 0) {
//                            String[] items = new String[2];
//                            items[0] = "手动填写重量";
//                            items[1] = "扫描连接附近电子秤";
//                            QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(HomeActivity.this);
//                            builder.addItems(items, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    switch (i) {
//                                        case 0:
//
//                                            if (supplier != null && matterId > 0 && matterLevelId > 0) {
//
//                                                SAMPLING_MODE = 1;
//
//                                                SamplingFragment samplingFragment = SamplingFragment.newInstance("0",
//                                                        supplierId,
//                                                        matterId, matterLevelId);
//                                                showDialogFragment(samplingFragment, SAMPLING);
//                                            } else {
//                                                myToasty.showWarning("请先选择供应商，品类，品类等级！");
//                                            }
//
//                                            break;
//
//                                        case 1:
//
//                                            SAMPLING_MODE = 2;
//
//                                            Intent intent2 = ElcScaleActivity.newIntent(HomeActivity.this, 3);
//                                            startActivity(intent2);
//                                            break;
//                                        default:
//                                            break;
//                                    }
//
//                                    dialogInterface.dismiss();
//                                }
//                            }).show();
//                        } else {
//
//                            switch (SAMPLING_MODE) {
//                                case 1:
//                                    if (supplier != null && matterId > 0 && matterLevelId > 0) {
//
//                                        SAMPLING_MODE = 1;
//
//                                        SamplingFragment samplingFragment = SamplingFragment.newInstance("0",
//                                                supplierId,
//                                                matterId, matterLevelId);
//                                        showDialogFragment(samplingFragment, SAMPLING);
//                                    } else {
//                                        myToasty.showWarning("请先选择供应商，品类，品类等级！");
//                                    }
//                                    break;
//
//                                case 2:
//                                    Intent intent2 = ElcScaleActivity.newIntent(HomeActivity.this, 3);
//                                    startActivity(intent2);
//                                    break;
//                                default:
//                                    break;
//                            }
//
//
//                        }
//                    }
//                }

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
                        DeductionFragment.newInstance(mTvWeight.getText().toString());
                showDialogFragment(deductionFragment, DEDUCTION);
                break;

            case R.id.id_tv_deductionMix://扣重率
                final EditText editText1 = new EditText(HomeActivity.this);
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(HomeActivity.this);
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

            case R.id.id_iv_choose_printer://选择打印机
//                if (deviceListFragment == null) {
//                    deviceListFragment = new DeviceListFragment();
//                }
//                showDialogFragment(deviceListFragment, DEVICE_LIST);
                break;

            case R.id.id_iv_print://打印(单据列表)

                BillListFragment billListFragment = new BillListFragment();

                showDialogFragment(billListFragment, BILL_LIST);
                break;

            case R.id.id_tv_piece_weight://单重
                final EditText editText = new EditText(HomeActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(HomeActivity.this);
                inputDialog.setTitle(R.string.input_piece_weight).setView(editText);
                inputDialog.setPositiveButton(R.string.enter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pieceWeight = editText.getText().toString().trim();

                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_PIECE_WEIGHT,
                                        pieceWeight);
                                mTvPieceWeight.setText(pieceWeight);

//                                observablePieceWeight = Observable.create(new ObservableOnSubscribe<Double>() {
//                                    @Override
//                                    public void subscribe(ObservableEmitter<Double> emitter) throws Exception {
//                                        emitter.onNext(Double.valueOf(pieceWeight));
//                                        emitter.onComplete();
//                                    }
//                                });

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

            case R.id.id_iv_takePicture:
                takePicture();
                break;

            case R.id.id_tv_save_bill://保存单据

                if (Double.valueOf(mTvCumulativeWeight.getText().toString().trim()) <= 0) {
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

                    Intent intent2 = SaveBillDetailsActivity.newInstance(HomeActivity.this,
                            deductionMix, mTvCumulativeWeight.getText().toString().trim());
                    startActivity(intent2);
                }

                break;

            case R.id.id_tv_hand://手动计重
                double weight = Double.parseDouble(mTvWeight.getText().toString());
                if (weight > 0d) {
                    Cumulative cumulative = new Cumulative();
                    cumulative.setCategory("净重");
                    cumulative.setWeight(mTvWeight.getText().toString());

                    cumulative.save();

                    int count = Integer.parseInt(mTvCumulativeCount.getText().toString());
                    double cWeight = Double.parseDouble(mTvCumulativeWeight.getText().toString());

                    cWeight = new DoubleCountUtils(cWeight,Double.valueOf(mTvWeight.getText().toString())).add();
                    count = count + 1;

                    mTvCumulativeCount.setText(String.valueOf(count));
                    mTvCumulativeWeight.setText(String.valueOf(cWeight));
                } else {
                    myToasty.showWarning("当前重量为零，请勿计重！");
                }

                break;

            default:
                break;
        }
    }

    /**
     * @param data 读取电子秤返回的数据
     */
    @Override
    public void readScaleData(String data) {
        mTvWeight.setText(data);
    }

    @Override
    public void readBluetoothRelayData(List<RelayBean> relayBeanList) {
        mRelayList.clear();
        mRelayList.addAll(relayBeanList);
        switchAdapter.notifyDataSetChanged();
    }


    @Override
    public void deviceInfo(DeviceBean deviceBean) {
        int type = deviceBean.getType();
        boolean isConnected = deviceBean.isConnected();
        boolean connectionChanged = deviceBean.isConnectionChanged();
        String failMsg = deviceBean.getFailMsg();

        String connectInfo = "";

        switch (type) {

            case MAIN_SCALE:
            case SAMPLING_SCALE:

                if (isConnected) {
                    connectInfo = "电子秤连接成功";
                } else {
                    connectInfo = "电子秤连接失败";
                }

                break;

            case BLUETOOTH_RELAY:

                if (isConnected) {
                    connectInfo = "蓝牙继电器连接成功";
                } else {
                    connectInfo = "蓝牙继电器连接失败";
                }

                break;

            case WIFI_RELAY:

                if (isConnected) {
                    connectInfo = "Wifi继电器连接成功";
                } else {
                    connectInfo = "Wifi继电器连接失败";
                }

                break;


            case BLE_SCREEN:

                if (isConnected) {
                    connectInfo = "显示屏连接成功";
                } else {
                    connectInfo = "显示屏连接失败";
                }

                break;

            default:
                break;
        }

        if (isConnected) {
            myToasty.showSuccess(connectInfo);
        } else {
            myToasty.showError(connectInfo);
        }

        if (failMsg != null && failMsg.length() > 0) {
            myToasty.showError(failMsg);
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
        RxPermissions rxPermissions = new RxPermissions(HomeActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        startActivityForResult(new Intent(HomeActivity.this, GetPicOrMP4Activity.class), 200);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
