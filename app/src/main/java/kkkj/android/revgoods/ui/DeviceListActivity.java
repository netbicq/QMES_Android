package kkkj.android.revgoods.ui;

/**
 * 设备清单
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Master;
import kkkj.android.revgoods.bean.Power;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.bean.Sapmle;
import kkkj.android.revgoods.bean.ShowOut;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;

public class DeviceListActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_master)
    TextView tvMaster;
    @BindView(R.id.tv_relay)
    TextView tvRelay;
    @BindView(R.id.tv_in_line)
    TextView tvInLine;
    @BindView(R.id.tv_out_line)
    TextView tvOutLine;
    @BindView(R.id.tv_sampling)
    TextView tvSampling;
    @BindView(R.id.tv_show_out)
    TextView tvShowOut;
    @BindView(R.id.switch_master)
    SwitchButton switchMaster;
    @BindView(R.id.switch_relay)
    SwitchButton switchRelay;
    @BindView(R.id.switch_sampling)
    SwitchButton switchSampling;
    @BindView(R.id.switch_show_out)
    SwitchButton switchShowOut;
    private ProduceLine produceLine;

    @Override
    protected int setLayout() {
        return R.layout.activity_device_list;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        produceLine = (ProduceLine) bundle.getSerializable("produceLine");

        //主秤
        if (produceLine.getMaster() != null) {
            String masterString = produceLine.getMaster();
            Master master = new Gson().fromJson(masterString, Master.class);
            String address = master.getDeviceAddr();
            if (address != null) {
                //已配置
                switchMaster.setVisibility(View.VISIBLE);
                switchMaster.setChecked(produceLine.isMasterConnectionState());

                switch (master.getDeviceType()) {
                    case 1://蓝牙
                        tvMaster.setText("蓝牙");
                        break;

                    case 2://wifi
                        tvMaster.setText("Wifi");
                        break;
                    default:
                        break;

                }
            }
        }


        //继电器
        if (produceLine.getPower() != null) {
            String powerString = produceLine.getPower();
            Power power = new Gson().fromJson(powerString, Power.class);
            if (power.getDeviceAddr() != null) {
                //已配置
                switchRelay.setVisibility(View.VISIBLE);
                switchRelay.setChecked(produceLine.isRelayConnectionState());

                int inLine = power.getInLine();
                int outLine = power.getOutLine();
                tvInLine.setText(String.valueOf(inLine));
                tvOutLine.setText(String.valueOf(outLine));

                switch (power.getDeviceType()) {
                    case 1://蓝牙
                        tvRelay.setText("蓝牙");
                        break;

                    case 2://wifi
                        tvRelay.setText("Wifi");
                        break;
                    default:
                        break;

                }
            }
        }


        //采样秤
        if (produceLine.getSapmle() != null) {
            String sapmleString = produceLine.getSapmle();
            Sapmle sapmle = new Gson().fromJson(sapmleString, Sapmle.class);
            //是否配置了采样的秤
            if (sapmle.getDeviceAddr() != null) {
                //已配置
                switchSampling.setVisibility(View.VISIBLE);
                switchSampling.setChecked(produceLine.isSamplingConnectionState());

                switch (sapmle.getDeviceType()) {
                    case 1://蓝牙
                        tvSampling.setText("蓝牙");
                        break;

                    case 2://wifi
                        tvSampling.setText("Wifi");
                        break;
                    default:
                        break;

                }
            }
        }

        //显示屏 蓝牙Ble设备
        if (produceLine.getShowOut() != null) {
            String showOutString = produceLine.getShowOut();
            ShowOut showOut = new Gson().fromJson(showOutString, ShowOut.class);
            if (showOut.getDeviceAddr() != null) {
                //已配置
                switchShowOut.setVisibility(View.VISIBLE);
                switchShowOut.setChecked(produceLine.isShowOutConnectionState());

                switch (showOut.getDeviceType()) {
                    case 1://蓝牙
                        tvShowOut.setText("蓝牙");
                        break;

                    case 2://wifi
                        tvShowOut.setText("Wifi");
                        break;
                    default:
                        break;

                }
            }
        }

    }

    @Override
    protected void initView() {
        tvTitle.setText(produceLine.getName());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchMaster.setOnCheckedChangeListener(this);
        switchRelay.setOnCheckedChangeListener(this);
        switchSampling.setOnCheckedChangeListener(this);
        switchShowOut.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        DeviceEvent deviceEvent = new DeviceEvent();
        switch (view.getId()) {

            case R.id.switch_master:
                if (isChecked) {
                    deviceEvent.setConnectType(1);
                }else {
                    deviceEvent.setDisConnectType(1);
                }
                break;

            case R.id.switch_relay:
                if (isChecked) {
                    deviceEvent.setConnectType(2);
                }else {
                    deviceEvent.setDisConnectType(2);
                }
                break;

            case R.id.switch_sampling:
                if (isChecked) {
                    deviceEvent.setConnectType(3);
                }else {
                    deviceEvent.setDisConnectType(3);
                }
                break;

            case R.id.switch_show_out:
                if (isChecked) {
                    deviceEvent.setConnectType(4);
                }else {
                    deviceEvent.setDisConnectType(4);
                }
                break;

            default:
                break;
        }
        EventBus.getDefault().post(deviceEvent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
