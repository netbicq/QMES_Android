package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.SamplingDetails;

public class SamplingDetailsAdapter extends BaseQuickAdapter<SamplingDetails, BaseViewHolder> {

    public SamplingDetailsAdapter(int layoutResId, @Nullable List<SamplingDetails> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SamplingDetails item) {
        helper.setText(R.id.id_tv_number,item.getNumber());
        helper.setText(R.id.id_tv_count,item.getCount()+"");
        helper.setText(R.id.id_tv_weight,item.getWeight());
        helper.setText(R.id.id_tv_specs,item.getSpecs().getSpecs());
        helper.setText(R.id.id_tv_proportion,item.getSpecsProportion()+ "");
        helper.addOnClickListener(R.id.tv_delete);
    }
}
