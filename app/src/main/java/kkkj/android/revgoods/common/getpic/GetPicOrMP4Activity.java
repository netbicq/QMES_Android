package kkkj.android.revgoods.common.getpic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;

import java.io.File;

import butterknife.BindView;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;


public class GetPicOrMP4Activity extends MvpBaseActivity {
    @BindView(R.id.jcameraview)
    JCameraView mJCameraView;
    boolean isPermited = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_getpic;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        //设置视频保存路径
        mJCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        //JCameraView监听
        mJCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                GetPicModel data = new GetPicModel();
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                data.setImagePath(getRealPathFromUri(mContext, uri));
                data.setType(0);
                Intent intent = new Intent();
                intent.putExtra("result", data);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                GetPicModel data = new GetPicModel();
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), firstFrame, null, null));
                data.setImagePath(getRealPathFromUri(mContext, uri));
                data.setType(1);
                data.setMp4Path(url);
                Intent intent = new Intent();
                intent.putExtra("result", data);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
        mJCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        //6.0动态权限获取
        getPermissions();
    }

    /*
    获取真实路径
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPermited) {
            mJCameraView.onPause();
        }
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        rxPermissions
                .requestEachCombined(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        isPermited = true;
                        mJCameraView.onResume();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //有至少一个权限没有同意
                        finish();
                    } else {
                        //有至少一个权限没有同意且勾选了不在提示
                        showToast("请在权限管理中打开相关权限");
                        finish();
                    }
                });
    }
}
