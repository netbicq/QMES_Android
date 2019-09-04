package kkkj.android.revgoods.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.fragment
 * Author: Admin
 * Time: 2019/9/3 10:44
 * Describe: describe
 */
public class SetProduceLineFragment extends BaseDialogFragment implements View.OnClickListener {

    private SwitchButton switchMaster;
    private SwitchButton switchRelay;
    private SwitchButton switchSampling;
    private SwitchButton switchShowout;
    private Button button;

    private int produceLineId;
    private ProduceLine produceLine;

    //是否禁用收料秤
    private boolean isMasterBaned;
    //是否禁用继电器
    private boolean isPowerBaned;
    //是否禁用采样秤
    private boolean isSapmleBaned;
    //是否禁用显示器
    private boolean isShowOutBaned;

    public static SetProduceLineFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        SetProduceLineFragment fragment = new SetProduceLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_set_produce_line;
    }

    @Override
    public void initData() {
        produceLineId = (int) getArguments().getSerializable("id");
        Logger.d(produceLineId);
        produceLine = LitePal.find(ProduceLine.class, produceLineId);
    }

    @Override
    public void initView(View view) {
        //点击透明区域不可取消
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        switchMaster = view.findViewById(R.id.switch_master);
        switchRelay = view.findViewById(R.id.switch_relay);
        switchSampling = view.findViewById(R.id.switch_sampling);
        switchShowout = view.findViewById(R.id.switch_showout);
        button = view.findViewById(R.id.button);

        tvTitle.setText(produceLine.getName());

        isMasterBaned = produceLine.isMasterBaned();
        isPowerBaned = produceLine.isPowerBaned();
        isSapmleBaned = produceLine.isSapmleBaned();
        isShowOutBaned = produceLine.isShowOutBaned();

        switchMaster.setChecked(isMasterBaned);
        switchRelay.setChecked(isPowerBaned);
        switchSampling.setChecked(isSapmleBaned);
        switchShowout.setChecked(isShowOutBaned);

        ivBack.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                dismiss();
                break;

            case R.id.button:

                produceLine.setMasterBaned(switchMaster.isChecked());
                Logger.d(switchMaster.isChecked());
                produceLine.setPowerBaned(switchRelay.isChecked());
                produceLine.setSapmleBaned(switchSampling.isChecked());
                produceLine.setShowOutBaned(switchShowout.isChecked());
                produceLine.save();

                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setRefresh(true);
                EventBus.getDefault().post(deviceEvent);
                dismiss();

                break;

            default:
                break;
        }

    }

}
