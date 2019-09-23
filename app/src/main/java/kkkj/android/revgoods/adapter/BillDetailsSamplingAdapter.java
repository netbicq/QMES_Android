package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.litepal.LitePal;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.adapter
 * Author: Admin
 * Time: 2019/8/30 11:36
 * Describe: 单据明细  =》 采样明细
 */
public class BillDetailsSamplingAdapter extends BaseQuickAdapter<SamplingDetails, BaseViewHolder> {
    public BillDetailsSamplingAdapter(int layoutResId, @Nullable List<SamplingDetails> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SamplingDetails item) {
        Specs specs = LitePal.find(Specs.class,item.getSpecsId());
        helper.setText(R.id.id_tv_number,item.getNumber());
        helper.setText(R.id.id_tv_count,helper.getAdapterPosition() + 1 + "");
        helper.setText(R.id.id_tv_weight,item.getWeight());
        helper.setText(R.id.id_tv_specs,specs.getName());
        helper.setText(R.id.id_tv_price,item.getPrice() + "");
        helper.setText(R.id.id_tv_proportion,item.getSpecsProportion()+ "");
    }
}
