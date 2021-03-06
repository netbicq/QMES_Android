package kkkj.android.revgoods.ui.chooseSupplier;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SupplierAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Path;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;
import kkkj.android.revgoods.bean.bill.PurPrices;
import kkkj.android.revgoods.bean.bill.PurSamples;
import kkkj.android.revgoods.bean.bill.Scales;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.ui.saveBill.BillModel;
import kkkj.android.revgoods.utils.BitMap2File;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * ???????????????
 */

public class ChooseSupplierActivity extends BaseActivity<ChooseSupplierPresenter> implements ChooseSupplierContract.View,View.OnClickListener {

    private EditText mEtSearchSupplier;
    private ImageView mBackImageView;
    private ImageView mZXingImageView;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private SupplierAdapter adapter;
    private List<Supplier> supplierList;
    private List<Supplier> supplierTempList;
    private List<Bill> billList;
    /**
     * ????????????Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    private int i = 0;



    @Override
    protected ChooseSupplierPresenter getPresenter() {
        return new ChooseSupplierPresenter();
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.id_iv_back);
        mZXingImageView = findViewById(R.id.id_iv_zxing);
        mEtSearchSupplier = findViewById(R.id.et_search_matter);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        //??????????????????????????????
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                supplierList.clear();
                supplierTempList.clear();
                mPresenter.getSupplier();
            }
        });


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

            @Override
            public void afterTextChanged(Editable editable) {
                String search = mEtSearchSupplier.getText().toString().trim();
                supplierList.clear();

//                List<Supplier> suppliers = supplierTempList.stream()
//                        .filter(supplier -> supplier.getName().contains(search))
//                        .collect(Collectors.toList());
//                supplierList.addAll(suppliers);

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

        if(smartRefreshLayout!=null)
        {
            smartRefreshLayout.finishRefresh();
        }
    }


    @Override
    public void addBillSuc(boolean data) {

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
        //?????????????????????
        if (requestCode == REQUEST_CODE) {
            //??????????????????????????????????????????
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {

                    String keyId = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.d(keyId);

                    List<Supplier> supplierList = LitePal.where("KeyID = ?", keyId).find(Supplier.class);
                    Supplier supplier = supplierList.get(0);
                    int id = supplier.getId();
                    DeviceEvent deviceEvent = new DeviceEvent();
                    deviceEvent.setSupplierId(id);
                    EventBus.getDefault().post(deviceEvent);
                    finish();
                    /**
                     result = bundle.getString(CodeUtils.RESULT_STRING);
                     Logger.d(result);
                     //                    fragments.get(showPosition).reserve(result);
                     GetEmpTaskByQRCoderModel.Request request = new GetEmpTaskByQRCoderModel.Request();
                     request.setDangerPointID(result);
                     mPresenter.getEmpTaskByQRCoder(request);
                     */
//                    Toast.makeText(this, "????????????:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    // showToast("?????????????????????");
                }
            }
        }
    }


    /**
     * ???????????????
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
                        //?????????????????????????????????
                        //showToast("?????????????????????");
                    } else {
                        //?????????????????????????????????????????????????????????
                        //showToast("???????????????????????????????????????");
                    }
                });
    }

}
