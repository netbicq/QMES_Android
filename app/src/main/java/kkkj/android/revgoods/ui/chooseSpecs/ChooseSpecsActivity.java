package kkkj.android.revgoods.ui.chooseSpecs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SpecsAdapter;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * 选择规格
 */
public class ChooseSpecsActivity extends BaseActivity<ChooseSpecsPresenter> implements ChooseSpecsContract.View,View.OnClickListener {

    @BindView(R.id.et_search_matter)
    EditText mEtSearchMatter;
    private ImageView mBackImageView;
    private ImageView mZXingImageView;
    private RecyclerView mRecyclerView;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    private List<Specs> mSpecs;
    private List<Specs> mTempSpecs;
    private SpecsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public static final String MATTER_ID = "matterID";
    private String matterId;

    public static Intent newIntent(Context context, String matterId) {
        Intent intent = new Intent(context, ChooseSpecsActivity.class);
        intent.putExtra(MATTER_ID, matterId);
        return intent;
    }

    @Override
    protected ChooseSpecsPresenter getPresenter() {
        return new ChooseSpecsPresenter();
    }

    protected void initData() {
        mSpecs = new ArrayList<>();
        mTempSpecs = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            matterId = intent.getStringExtra(MATTER_ID);
        }

        ChooseSpecsModel.Request request = new ChooseSpecsModel.Request();
        request.setMatterId(matterId);
        mPresenter.getSpecsByMatterId(request);


        mAdapter = new SpecsAdapter(R.layout.item_card_view, mSpecs);
        mLayoutManager = new GridLayoutManager(this,4);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Specs specs = mSpecs.get(position);
                int id = specs.getId();
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setSpecsId(id);
                EventBus.getDefault().post(deviceEvent);
                finish();
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_specs;
    }

    protected void initView() {
        mBackImageView = findViewById(R.id.id_iv_back);
        mZXingImageView = findViewById(R.id.id_iv_zxing);
        mZXingImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mEtSearchMatter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = mEtSearchMatter.getText().toString().trim();
                mSpecs.clear();

                for (int i=0;i<mTempSpecs.size();i++) {
                    Specs specs = mTempSpecs.get(i);
                    String str = specs.getSpecs();

                    if (str.contains(search)) {
                        mSpecs.add(specs);
                    }
                }

                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void getSpecsSuc(List<Specs> data) {
        mSpecs.clear();
        mTempSpecs.clear();
        mSpecs.addAll(data);
        mTempSpecs.addAll(data);
        mAdapter.notifyDataSetChanged();
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
        RxPermissions rxPermissions = new RxPermissions(ChooseSpecsActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        Intent intent = new Intent(ChooseSpecsActivity.this, CaptureActivity.class);
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
