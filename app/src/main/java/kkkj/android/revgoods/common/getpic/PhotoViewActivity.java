package kkkj.android.revgoods.common.getpic;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.utils.ImageLoader;

public class PhotoViewActivity extends MvpBaseActivity {

    @BindView(R.id.photo_view)
    PhotoView photoView;
    @BindView(R.id.id_iv_back)
    ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_photoview;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        //action_bar_title.setText("照片详情");
        photoView.setZoomable(true);
        String url = getIntent().getStringExtra("picUrl");
        String uploadPicUrl = getIntent().getStringExtra("uploadPicUrl");
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.load(mContext, photoView, url);
        } else if (!TextUtils.isEmpty(uploadPicUrl)) {
            Uri uploadPic = Uri.parse(getIntent().getStringExtra("uploadPicUrl"));
            if (uploadPic != null) {
                ImageLoader.load(mContext, photoView, uploadPic);
            }
        }

    }

    @OnClick(R.id.id_iv_back)
    public void onClick() {
        finish();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}


