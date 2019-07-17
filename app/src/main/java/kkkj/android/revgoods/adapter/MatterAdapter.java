package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Matter;

public class MatterAdapter extends BaseQuickAdapter<Matter, BaseViewHolder> {
    public MatterAdapter(int layoutResId, @Nullable List<Matter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Matter item) {
        helper.setText(R.id.id_tv_normal,item.getName());
    }
}
