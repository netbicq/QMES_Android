package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

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
        TextView viewDelete = helper.getView(R.id.tv_delete);
        TextView viewUpload = helper.getView(R.id.tv_upload);

        helper.setText(R.id.id_tv_device,item.getName());

        if (item.getIsUpload() < 0) { //未上传
            viewDelete.setVisibility(View.GONE);
            viewUpload.setVisibility(View.VISIBLE);
        }else {
            viewDelete.setVisibility(View.VISIBLE);
            viewUpload.setVisibility(View.GONE);

        }

        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_upload);
        helper.addOnClickListener(R.id.tv_share);
    }
}
