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
        helper.setText(R.id.tv_name,item.getName());
        TextView close = helper.getView(R.id.tv_close);
        TextView open = helper.getView(R.id.tv_open);
        if(item.getState().equals("1"))//吸合
        {
            close.setVisibility(View.VISIBLE);
            open.setVisibility(View.GONE);
        }
        else {
            close.setVisibility(View.GONE);
            open.setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.tv_close);
        helper.addOnClickListener(R.id.tv_open);

    }
}
