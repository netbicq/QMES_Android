package kkkj.android.revgoods.ui.chooseSupplier;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SupplierAdapter;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.BaseActivity;

/**
 * 选择供应商
 */

public class ChooseSupplierActivity extends BaseActivity<ChooseSupplierPresenter> implements ChooseSupplierContract.View,View.OnClickListener {

    private EditText mEtSearchSupplier;
    private ImageView mBackImageView;
    private ImageView mZXingImageView;
    private RecyclerView mRecyclerView;
    private SupplierAdapter adapter;
    private List<Supplier> supplierList;
    private List<Supplier> supplierTempList;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;


    @Override
    protected ChooseSupplierPresenter getPresenter() {
        return new ChooseSupplierPresenter();
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.id_iv_back);
        mZXingImageView = findViewById(R.id.id_iv_zxing);
        mEtSearchSupplier = findViewById(R.id.et_search_matter);

        mRecyclerView = findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(adapter);

        mZXingImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

        mEtSearchSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable editable) {
                String search = mEtSearchSupplier.getText().toString().trim();
                supplierList.clear();

                List<Supplier> suppliers = supplierTempList.stream()
                        .filter(supplier -> supplier.getName().contains(search))
                        .collect(Collectors.toList());
                supplierList.addAll(suppliers);

                for (int i=0;i<supplierTempList.size();i++) {
                    Supplier supplier = supplierTempList.get(i);
                    String str = supplier.getName();

                    if (str.contains(search)) {
                        supplierList.add(supplier);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void initData() {
        supplierList = new ArrayList<>();
        supplierTempList = new ArrayList<>();

        mPresenter.getSupplier();

        adapter = new SupplierAdapter(R.layout.item_card_view,supplierList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Supplier supplier = supplierList.get(position);
                int id = supplier.getId();
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setSupplierId(id);
                EventBus.getDefault().post(deviceEvent);
                finish();
            }
        });


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_supplier;
    }

    @Override
    public void getSupplierSuc(List<Supplier> data) {
        supplierList.clear();
        supplierTempList.clear();
        supplierList.addAll(data);
        supplierTempList.addAll(data);

        adapter.notifyDataSetChanged();
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
        RxPermissions rxPermissions = new RxPermissions(ChooseSupplierActivity.this);
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        Intent intent = new Intent(ChooseSupplierActivity.this, CaptureActivity.class);
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
