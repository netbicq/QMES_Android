package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Cumulative;

public class CumulativeAdapter extends BaseQuickAdapter<Cumulative, BaseViewHolder> {
    public CumulativeAdapter(int layoutResId, @Nullable List<Cumulative> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Cumulative item) {
        helper.setText(R.id.id_tv_count,item.getCount()+"");
        helper.setText(R.id.id_tv_category,item.getCategory());
        helper.setText(R.id.id_tv_weight,item.getWeight());
    }
}
