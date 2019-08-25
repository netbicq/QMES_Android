package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.ProduceLine;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.adapter
 * Author: Admin
 * Time: 2019/8/22 9:46
 * Describe: describe
 */
public class ProduceLineAdapter extends BaseQuickAdapter<ProduceLine, BaseViewHolder> {
    public ProduceLineAdapter(int layoutResId, @Nullable List<ProduceLine> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProduceLine item) {
        helper.setText(R.id.id_tv_device,item.getName());
    }
}
