package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Supplier;

public class SupplierAdapter extends BaseQuickAdapter<Supplier, BaseViewHolder> {
    public SupplierAdapter(int layoutResId, @Nullable List<Supplier> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Supplier item) {
        helper.setText(R.id.id_tv_normal,item.getName());
    }
}
