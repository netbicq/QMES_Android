package kkkj.android.revgoods.ui;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.MatterAdapter;
import kkkj.android.revgoods.adapter.MatterLevelAdapter;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterActivity;

public class ChooseMatterLevelActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtSearchMatter;
    private ImageView mBackImageView;
    private ImageView mZXingImageView;
    private RecyclerView mRecyclerView;
    private MatterLevelAdapter adapter;
    private List<MatterLevel> matterList;
    private List<MatterLevel> matterTempList;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    public static final String SUPPLIER_ID = "supplierId";



    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {

        mBackImageView = findViewById(R.id.id_iv_back);
        mZXingImageView = findViewById(R.id.id_iv_zxing);
        mEtSearchMatter = findViewById(R.id.et_search_matter);

        mRecyclerView = findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(adapter);

        mZXingImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

        mEtSearchMatter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = mEtSearchMatter.getText().toString().trim();
                matterList.clear();

                for (int i=0;i<matterTempList.size();i++) {
                    MatterLevel matter = matterTempList.get(i);
                    String str = matter.getName();

                    if (str.contains(search)) {
                        matterList.add(matter);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void initData() {

        matterList = new ArrayList<>();
        matterTempList =new ArrayList<>();

        matterList = LitePal.findAll(MatterLevel.class);
        matterTempList = LitePal.findAll(MatterLevel.class);

        adapter = new MatterLevelAdapter(R.layout.item_card_view,matterList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MatterLevel matter = matterList.get(position);
                int id = matter.getId();
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setMatterLevelId(id);
                EventBus.getDefault().post(deviceEvent);
                finish();
            }
        });


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_matter_level;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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
        RxPermissions rxPermissions = new RxPermissions(ChooseMatterLevelActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        Intent intent = new Intent(ChooseMatterLevelActivity.this, CaptureActivity.class);
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

}
