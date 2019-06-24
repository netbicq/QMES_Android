package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Specs;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.adapter
 * 创建者:   Bpldbt
 * 创建时间: 2019/6/24 15:39
 * 描述:    TODO
 */

public class SpecsAdapter extends BaseQuickAdapter<Specs, BaseViewHolder> {


    public SpecsAdapter(int layoutResId, @Nullable List<Specs> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Specs item) {
        helper.setText(R.id.id_tv_normal,item.getName());
    }
}
