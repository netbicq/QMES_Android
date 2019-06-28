package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.relay.bean.RelayBean;

public class SwitchAdapter extends BaseQuickAdapter<RelayBean, BaseViewHolder> {
    public SwitchAdapter(int layoutResId, @Nullable List<RelayBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RelayBean item) {
        ImageView imageViewLeft = helper.getView(R.id.iv_switch_left);
        ImageView imageViewRight = helper.getView(R.id.iv_switch_right);
        helper.setImageResource(R.id.iv_switch_left,item.getLeftIamgeView());
        helper.setImageResource(R.id.iv_switch_right,item.getRightImageView());
        if(item.getState().equals("1")){ //吸和
            imageViewLeft.setVisibility(View.GONE);
            imageViewRight.setVisibility(View.VISIBLE);
        } else {
            imageViewLeft.setVisibility(View.VISIBLE);
            imageViewRight.setVisibility(View.GONE);
        }
    }
}
