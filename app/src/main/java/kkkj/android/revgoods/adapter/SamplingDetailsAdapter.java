package kkkj.android.revgoods.adapter;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.litepal.LitePal;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;

public class SamplingDetailsAdapter extends BaseQuickAdapter<SamplingDetails, BaseViewHolder> {

    public SamplingDetailsAdapter(int layoutResId, @Nullable List<SamplingDetails> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SamplingDetails item) {
        Specs specs = LitePal.find(Specs.class,item.getSpecsId());
        helper.setText(R.id.id_tv_number,item.getNumber());
        helper.setText(R.id.id_tv_count,item.getCount()+"");
        helper.setText(R.id.id_tv_weight,item.getWeight());
        helper.setText(R.id.id_tv_specs,specs.getName());
        helper.setText(R.id.id_tv_price,item.getPrice() + "");
        helper.setText(R.id.id_tv_proportion,item.getSpecsProportion()+ "");

        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.image_view);

        Resources resources = helper.getConvertView().getContext().getResources();
        ImageView imageView = helper.getView(R.id.image_view);
        if (item.isUsed()) { //套用
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_check_box_checked_24dp));
        }else {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_check_box_unchecked_24dp));
        }

        /**
         * 计价方式
         * ValuationType = 1;根据规格计算
         * ValuationType = 2;根据规格占比计算
         */
        int valuationType = LitePal.find(Matter.class,item.getMatterId()).getValuationType();

        if (valuationType == 2) {
            imageView.setClickable(false);
        }
    }
}
