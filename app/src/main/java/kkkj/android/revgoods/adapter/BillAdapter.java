package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Bill;

public class BillAdapter extends BaseQuickAdapter<Bill, BaseViewHolder> {
    public BillAdapter(int layoutResId, @Nullable List<Bill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bill item) {
        helper.setText(R.id.id_tv_device,item.getName());
        helper.addOnClickListener(R.id.tv_delete);
    }
}
