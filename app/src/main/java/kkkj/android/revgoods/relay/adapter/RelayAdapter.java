package kkkj.android.revgoods.relay.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.relay.bean.RelayBean;

public class RelayAdapter extends BaseQuickAdapter<RelayBean, BaseViewHolder> {
    public RelayAdapter(int layoutResId, @Nullable List<RelayBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper,  RelayBean item) {
        helper.setText(R.id.id_tv_relay_name,item.getName());
        TextView mrelayName = helper.getView(R.id.id_tv_relay_name);
        if(item.getState().equals("1"))//吸合
        {
            mrelayName.setBackgroundColor(Color.parseColor("#008577"));
        }
        else {
            mrelayName.setBackgroundColor(Color.parseColor("#C1CDCD"));
        }
        helper.addOnClickListener(R.id.id_tv_relay_name);

    }
}
