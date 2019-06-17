package kkkj.android.revgoods;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import kkkj.android.revgoods.elcscale.view.ElcScaleActivity;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.relay.wifi.view.BlueToothRelayActivity;
import kkkj.android.revgoods.relay.wifi.view.WifiRelayActivity;

public class HomeActivity extends MvpBaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_menu1)
    TextView tv_menu1;
    @BindView(R.id.tv_menu2)
    TextView tv_menu2;
    @BindView(R.id.tv_menu3)
    TextView tv_menu3;
    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        tv_menu1.setOnClickListener(this);
        tv_menu2.setOnClickListener(this);
        tv_menu3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_menu1:
                startActivity(new Intent(mContext, WifiRelayActivity.class));
                break;
            case R.id.tv_menu2:
                startActivity(new Intent(mContext, BlueToothRelayActivity.class));
                break;
            case R.id.tv_menu3:
                startActivity(new Intent(mContext, ElcScaleActivity.class));
                break;
        }
    }
}
