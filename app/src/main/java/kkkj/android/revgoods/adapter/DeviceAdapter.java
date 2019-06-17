package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Device;

public class DeviceAdapter extends BaseQuickAdapter<Device, BaseViewHolder> {
    public DeviceAdapter(int layoutResId, @Nullable List<Device> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Device item) {
        helper.setText(R.id.id_tv_device,item.getName());
    }
}
