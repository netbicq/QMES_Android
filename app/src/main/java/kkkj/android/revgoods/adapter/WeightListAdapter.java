package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.adapter
 * Author: Admin
 * Time: 2019/8/30 10:01
 * Describe: 单据明细  =>  计重明细
 */
public class WeightListAdapter extends BaseQuickAdapter<String[] , BaseViewHolder> {

    public WeightListAdapter(int layoutResId, @Nullable List<String[]> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String[] item) {
        helper.setText(R.id.tv_order,item[0]);
        helper.setText(R.id.tv_1,item[1]);
        helper.setText(R.id.tv_2,item[2]);
        helper.setText(R.id.tv_3,item[3]);
        helper.setText(R.id.tv_4,item[4]);
        helper.setText(R.id.tv_5,item[5]);
        helper.setText(R.id.tv_6,item[6]);
        helper.setText(R.id.tv_7,item[7]);
        helper.setText(R.id.tv_8,item[8]);
        helper.setText(R.id.tv_9,item[9]);
        helper.setText(R.id.tv_10,item[10]);
    }
}
