package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Deduction;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.adapter
 * Author: Admin
 * Time: 2019/8/30 10:52
 * Describe: 单据明细  =》 扣重明细
 */
public class DeductionAdapter extends BaseQuickAdapter<Deduction, BaseViewHolder> {


    public DeductionAdapter(int layoutResId, @Nullable List<Deduction> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Deduction item) {
        helper.setText(R.id.id_tv_count,helper.getAdapterPosition() + 1 + "");
        helper.setText(R.id.id_tv_time,item.getTime());
        helper.setText(R.id.id_tv_category,item.getCategory());
        helper.setText(R.id.id_tv_weight,String.valueOf(item.getWeight()));
    }

}
