package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.DeviceAdapter;
import kkkj.android.revgoods.bean.Device;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 设备列表
 */
public class DeviceListFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Device> mDevices;

    //BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {
        mDevices = new ArrayList<>();
        //蓝牙电子秤
        Device deviceBluetooth = new Device();
        deviceBluetooth.setType(0);
        deviceBluetooth.setName("蓝牙电子秤");
        deviceBluetooth.setBluetoothMac("20:17:03:15:05:65");
        mDevices.add(deviceBluetooth);
        //蓝牙继电器
        Device deviceBluetoothRelay = new Device();
        deviceBluetoothRelay.setType(1);
        deviceBluetoothRelay.setName("蓝牙继电器");
        deviceBluetoothRelay.setBluetoothMac("00:0D:1B:00:13:BA");
        mDevices.add(deviceBluetoothRelay);
        //Wifi继电器
        Device deviceWifi = new Device();
        deviceWifi.setType(2);
        deviceWifi.setName("Wifi继电器");
        deviceWifi.setWifiIp("192.168.123.105");
        deviceWifi.setWifiPort(10001);
        mDevices.add(deviceWifi);

        //00:0D:1B:00:13:BA


    }

    private void initView(View view) {
        mBackImageView = view.findViewById(R.id.iv_back);
        mRecyclerView = view.findViewById(R.id.id_device_recyclerView);
        mTitle = view.findViewById(R.id.id_tv_title);
        mTitle.setText("请选择当前要连接的设备");
        mBackImageView.setOnClickListener(this);

        mAdapter = new DeviceAdapter(R.layout.item_device_list,mDevices);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Device device = mDevices.get(position);
                DeviceEvent deviceEvent = new DeviceEvent(device);
                EventBus.getDefault().post(deviceEvent);
                dismiss();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_device_list);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = (2 * width) / 3;
        wlp.height = (2 * height) / 3;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;

            default:
                break;
        }
    }
}
