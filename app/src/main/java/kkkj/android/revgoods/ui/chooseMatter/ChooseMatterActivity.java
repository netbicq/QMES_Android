package kkkj.android.revgoods.ui.chooseMatter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.MatterAdapter;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * 选择品类
 */
public class ChooseMatterActivity extends BaseActivity<ChooseMatterPresenter> implements ChooseMatterContract.View,View.OnClickListener {

    private ImageView mBackImageView;
    private ImageView mZXingImageView;
    private RecyclerView mRecyclerView;
    private MatterAdapter adapter;
    private List<Matter> matterList;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    public static final String SUPPLIER_ID = "supplierId";
    private String supplierId;

    public static Intent newIntent(Context context, String supplierId) {
        Intent intent = new Intent(context, ChooseMatterActivity.class);
        intent.putExtra(SUPPLIER_ID, supplierId);
        return intent;
    }

    @Override
    protected ChooseMatterPresenter getPresenter() {
        return new ChooseMatterPresenter();
    }

    protected void initView() {
        mBackImageView = findViewById(R.id.id_iv_back);
        mZXingImageView = findViewById(R.id.id_iv_zxing);

        mRecyclerView = findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(adapter);

        mZXingImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        matterList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            supplierId = intent.getStringExtra(SUPPLIER_ID);
        }

        ChooseMatterModel.Request request = new ChooseMatterModel.Request();
        request.setSupplierId(supplierId);
        mPresenter.getMatterBySupplierId(request);

        adapter = new MatterAdapter(R.layout.camera_view,matterList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_matter;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_back:
                finish();
                break;

            case R.id.id_iv_zxing:
                zxing();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //二维码扫描回调
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    /**
                     result = bundle.getString(CodeUtils.RESULT_STRING);
                     Logger.d(result);
                     //                    fragments.get(showPosition).reserve(result);
                     GetEmpTaskByQRCoderModel.Request request = new GetEmpTaskByQRCoderModel.Request();
                     request.setDangerPointID(result);
                     mPresenter.getEmpTaskByQRCoder(request);
                     */
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    // showToast("解析二维码失败");
                }
            }
        }
    }


    /**
     * 二维码扫描
     */
    private void zxing() {
        RxPermissions rxPermissions = new RxPermissions(ChooseMatterActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        Intent intent = new Intent(ChooseMatterActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //有至少一个权限没有同意
                        //showToast("请同意全部权限");
                    } else {
                        //有至少一个权限没有同意且勾选了不在提示
                        //showToast("请在权限管理中打开相关权限");
                    }
                });
    }


    @Override
    public void getMatterSuc(List<Matter> data) {
        matterList.clear();
        matterList.addAll(data);
        adapter.notifyDataSetChanged();
    }
}