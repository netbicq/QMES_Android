package kkkj.android.revgoods.adapter;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.utils.ImageLoader;


public class PicOrMp4Adapter extends BaseQuickAdapter<GetPicModel, BaseViewHolder> {
    public PicOrMp4Adapter(int layoutResId, @Nullable List<GetPicModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetPicModel item) {
        ImageView pic = helper.getView(R.id.img);
        ImageView mp4 = helper.getView(R.id.mp4);
        EditText ed_content = helper.getView(R.id.ed_content);
        ImageView delete = helper.getView(R.id.iv_delete);
        ImageView upload = helper.getView(R.id.ic_upload);
//        if (ed_content.getTag() instanceof TextWatcher){
//            ed_content.removeTextChangedListener ((TextWatcher) ed_content.getTag());
//       }
        ed_content.setText("");
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                item.setContent(s.toString());
            }
        };
        ed_content.addTextChangedListener(watcher);
        ed_content.setTag(watcher);
        if(!TextUtils.isEmpty(item.getContent()))
        {
            helper.setText(R.id.ed_content,item.getContent());
        }
        else {
            helper.setText(R.id.ed_content,"");
        }
        if (!TextUtils.isEmpty(item.getImagePath()) && !item.getImagePath().equals("add")) {
            ImageLoader.load(mContext, pic, item.getImagePath(), 5);
        } else {

        }
        if (item.getType() == 0) {
            mp4.setVisibility(View.GONE);
        } else if (item.getType() == 1) {
            if (!TextUtils.isEmpty(item.getImagePath()) && item.getImagePath().equals("default")) {
                ImageLoader.load(mContext, pic, R.drawable.ic28, 5);
                mp4.setVisibility(View.GONE);
            }else {
                mp4.setVisibility(View.VISIBLE);
            }
        }
        helper.addOnClickListener(R.id.iv_delete);
        helper.addOnClickListener(R.id.ic_upload);
        helper.addOnClickListener(R.id.img);
        helper.addOnClickListener(R.id.mp4);
        helper.addOnClickListener(R.id.ed_content);

        /**if(item.getIsDwon()==1)
        {
            ed_content.setEnabled(false);
            ed_content.setBackground(null);
            ed_content.setGravity(Gravity.START|Gravity.CENTER);
            delete.setVisibility(View.GONE);
        }
        else {
            ed_content.setEnabled(true);
            delete.setVisibility(View.VISIBLE);
        }
         */

        if(item.getIsUpload()==0)
        {
            //ed_content.setEnabled(false);
            upload.setVisibility(View.GONE);
        }
        else {
//            upload.setVisibility(View.VISIBLE);
        }
    }
}
