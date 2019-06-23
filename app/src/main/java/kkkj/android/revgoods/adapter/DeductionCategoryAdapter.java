package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.DeductionCategory;

public class DeductionCategoryAdapter extends BaseQuickAdapter<DeductionCategory, BaseViewHolder> {
    public DeductionCategoryAdapter(int layoutResId, @Nullable List<DeductionCategory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeductionCategory item) {
        helper.setText(R.id.id_tv_category,item.getCategory());
        helper.setText(R.id.id_tv_price,item.getPrice());
        helper.addOnClickListener(R.id.tv_delete);
    }
}
