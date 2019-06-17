package kkkj.android.revgoods.relay.adapter;

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
        TextView tv_menu1 = helper.getView(R.id.tv_menu1);//吸合
        TextView tv_menu2 = helper.getView(R.id.tv_menu2);//断开
        if(item.getState().equals("1"))//吸合
        {
            tv_menu1.setVisibility(View.GONE);
            tv_menu2.setVisibility(View.VISIBLE);
        }
        else {
            tv_menu1.setVisibility(View.VISIBLE);
            tv_menu2.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.tv_menu1);
        helper.addOnClickListener(R.id.tv_menu2);
    }
}
