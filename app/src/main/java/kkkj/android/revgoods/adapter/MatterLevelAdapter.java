package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.MatterLevel;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.adapter
 * 创建者:   Bpldbt
 * 创建时间: 2019/8/14 23:13
 * 描述:    TODO
 */
public class MatterLevelAdapter extends BaseQuickAdapter<MatterLevel, BaseViewHolder> {
    public MatterLevelAdapter(int layoutResId, @Nullable List<MatterLevel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MatterLevel item) {
        helper.setText(R.id.id_tv_normal,item.getName());
    }
}
