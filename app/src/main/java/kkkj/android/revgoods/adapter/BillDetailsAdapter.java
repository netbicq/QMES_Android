package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.BillDetails;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.adapter
 * Author: Admin
 * Time: 2019/8/6 14:29
 * Describe: describe
 */
public class BillDetailsAdapter extends BaseQuickAdapter<BillDetails, BaseViewHolder> {
    public BillDetailsAdapter(int layoutResId, @Nullable List<BillDetails> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillDetails item) {
        helper.setText(R.id.tv_specs,item.getSpecs());
        helper.setText(R.id.tv_adjust_price,item.getAdjustPrice());
        helper.setText(R.id.tv_final_price,item.getFinalPrice());
        helper.setText(R.id.tv_price,item.getPrice());
        helper.setText(R.id.tv_specs_proportion,item.getProportion());
        helper.setText(R.id.tv_weight,item.getWeight()+ "");
        helper.setText(R.id.tv_total_price,item.getTotalPrice() + "");

    }
}
