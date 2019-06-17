package kkkj.android.revgoods.common.getpic;

import android.net.Uri;
import android.text.TextUtils;
import com.github.chrisbanes.photoview.PhotoView;
import butterknife.BindView;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.utils.ImageLoader;

public class PhotoViewActivity extends MvpBaseActivity {

    @BindView(R.id.photo_view)
    PhotoView photoView;
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
        action_bar_title.setText("照片详情");
        photoView.setZoomable(true);
        String url = getIntent().getStringExtra("picUrl");
        String uploadPicUrl = getIntent().getStringExtra("uploadPicUrl");
        if(!TextUtils.isEmpty(url))
        {
            ImageLoader.load(mContext,photoView,url);
        }
        else if(!TextUtils.isEmpty(uploadPicUrl))
        {
            Uri uploadPic = Uri.parse(getIntent().getStringExtra("uploadPicUrl")) ;
            if(uploadPic!=null)
            {
                ImageLoader.load(mContext,photoView,uploadPic);
            }
        }

    }
}
