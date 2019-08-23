package kkkj.android.revgoods.elcscale.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.conn.bluetooth.Bluetooth;
import kkkj.android.revgoods.conn.bluetooth.ScanBlueCallBack;
import kkkj.android.revgoods.conn.bluetooth.ScanBlueReceiver;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * 蓝牙电子秤
 */

public class ElcScaleActivity extends MvpBaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private int REQUEST_ENABLE_BT = 101;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ScanBlueReceiver scanBlueReceiver;
    List<BluetoothBean> mList;
    private List<String> mNameList;
    private StringAdapter mAdapter;
    Bluetooth mBluetooth;

    private int type = -1;
    private Device device;

    public static Intent newIntent(Context context, int type) {
        Intent intent = new Intent(context,ElcScaleActivity.class);
        intent.putExtra("deviceType",type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            type = bundle.getInt("deviceType");
        }
        device = new Device();
        device.setType(type);
        Logger.d("deviceType" + type);

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBluetooth = new Bluetooth();
        mList = new ArrayList<>();
        mNameList = new ArrayList<>();
        //adapter = new ElcScaleManagerAdapter(R.layout.item_elcscale, mList);
        mAdapter = new StringAdapter(R.layout.item_device_list, mNameList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                device.setBluetoothMac(mList.get(position).getBluetoothDevice().getAddress());
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setDevice(device);
                EventBus.getDefault().post(deviceEvent);
                finish();
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        ;
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新前断开所有连接和停止所有监听

                if (!mBluetooth.scanBlue()) {
                    smartRefreshLayout.finishRefresh();
                    mBluetooth.openBlueSync(mActivity, REQUEST_ENABLE_BT);
                } else {
                    mList.clear();
                    mNameList.clear();
                    mAdapter.notifyDataSetChanged();
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
                if (!TextUtils.isEmpty(device.getName())) {
                    BluetoothBean bluetoothBean = new BluetoothBean();
                    bluetoothBean.setBluetoothDevice(device);
                    Logger.d("deviceAddress:" + device.getAddress());

                    if (!mNameList.contains(device.getName() + "     Mac地址:" + device.getAddress())) {
                        mNameList.add(device.getName() + "     Mac地址:" + device.getAddress());
                        mList.add(bluetoothBean);
                    }

                    mAdapter.notifyDataSetChanged();

                }
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(scanBlueReceiver, filter);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(scanBlueReceiver, filter2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanBlueReceiver != null) {
            unregisterReceiver(scanBlueReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == -1)//蓝牙打开
            {
                smartRefreshLayout.autoRefresh();
            } else {
                showToast("蓝牙打开失败");
                smartRefreshLayout.finishRefresh();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG,0)));
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

    public class StringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public StringAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.id_tv_device, item);
        }
    }
}
