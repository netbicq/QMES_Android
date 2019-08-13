package kkkj.android.revgoods.ui.chooseSupplier;

import android.Manifest;
import android.content.Intent;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import kkkj.android.revgoods.utils.NetUtils;

/**
 * 选择供应商
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
     * 扫描跳转Activity RequestCode
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
        //是否启用上拉加载更多
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

//        if (NetUtils.checkNetWork()) {
//            billList = LitePal.where("isUpload < ?","0").find(Bill.class,true);
//            if (billList.size() > 0) {
//                BillModel.Request request = showBill(billList.get(i));
//                mPresenter.addBill(request);
//            }
//        }

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
//        if (data) {
//            billList.get(i).setIsUpload(0);
//            billList.get(i).save();
//            while (i < billList.size()) {
//                i++;
//                BillModel.Request request = showBill(billList.get(i));
//                mPresenter.addBill(request);
//            }
//
//        }else {
//            BillModel.Request request = showBill(billList.get(i));
//            mPresenter.addBill(request);
//        }
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


    //显示单据
    private BillModel.Request showBill(Bill bill) {

        BillModel.Request request = new BillModel.Request();
        //总金额
        double money = 0d;

        List<Deduction> deductionList = bill.getDeductionList();
        List<SamplingDetails> samplingDetailsList = bill.getSamplingDetailsList();
        List<Cumulative> cumulativeList = bill.getCumulativeList();
        int supplierId = bill.getSupplierId();
        int matterId = bill.getMatterId();
        Supplier supplier = LitePal.find(Supplier.class,supplierId);
        Matter matter = LitePal.find(Matter.class,matterId);

        //重新计算占比
        //采样总重量
        double total = 0d;
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(total));
            BigDecimal b2 = new BigDecimal(samplingDetailsList.get(i).getWeight());
            total = b1.add(b2).doubleValue();
        }

        //计算占比
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / total;
            samplingDetailsList.get(i).setSpecsProportion(specsProportion);
        }
        LitePal.saveAll(samplingDetailsList);


        //计秤总重量
        double mWeight = bill.getWeight();
        // 扣重总重量
        double deductionWeight = 0d;
        for (int i = 0; i < deductionList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(deductionWeight));
            BigDecimal b2 = new BigDecimal(deductionList.get(i).getWeight());
            deductionWeight = b1.add(b2).doubleValue();
        }
        //减扣重
        BigDecimal b1 = new BigDecimal(Double.toString(mWeight));
        BigDecimal b2 = new BigDecimal(Double.toString(deductionWeight));
        mWeight = b1.subtract(b2).doubleValue();
        //实际重量  :除去扣重，以及扣重率之后的
        double realWeight = mWeight * (100 - bill.getDeductionMix()) * 0.01;

        //规格占比最大的采样
        SamplingDetails maxSamplingDetails = Collections.max(samplingDetailsList, new Comparator<SamplingDetails>() {
            @Override
            public int compare(SamplingDetails samplingDetails, SamplingDetails t1) {
                if (samplingDetails.getSpecsProportion() > t1.getSpecsProportion()) {
                    return 1;
                } else if (samplingDetails.getSpecsProportion() == t1.getSpecsProportion()) {
                    return 0;
                } else {
                    return -1;
                }
            }

        });
        //占比最大的规格
        Specs specs = LitePal.find(Specs.class,maxSamplingDetails.getSpecsId());

        /**
         * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408 规格ID
         * Amount : 2.0
         * Price : 3.0
         * Menoy : 4.0 金额
         * Ratio : 5.0
         */
        List<PurPrices> purPricesList = new ArrayList<>();//计价明细


        for (int i = 0; i < samplingDetailsList.size(); i++) {
            PurPrices purPrices = new PurPrices();
            double ratio = samplingDetailsList.get(i).getSpecsProportion();//规格占比
            double weight = realWeight * ratio;//当前占比的重量
            double price = samplingDetailsList.get(i).getPrice();//单价

            money = weight * price + money;//总金额

            Specs specs1 = LitePal.find(Specs.class,samplingDetailsList.get(i).getSpecsId());
            purPrices.setNormsID(specs1.getKeyID());//规格ID

            purPrices.setAmount(weight);//当前占比的重量
            purPrices.setPrice(price);//单价
            purPrices.setMenoy(weight * price);//金额
            purPrices.setRatio(ratio);//规格占比
            purPricesList.add(purPrices);
        }
        request.setPurPrices(purPricesList);//计价明细

        /**
         * PurchaseDate : 2019-08-08 15:07:06
         * SupplierID : cd529f25-5fae-40a2-ac49-1df6627fe769
         * NormID : f1a26ea8-d60f-4874-a8b2-abc5d8387b4d
         * CategoryID : 4bdfa721-cc93-4588-b55e-270865614f6c
         * CategoryLv : 312387db-e6f9-4ae5-8715-a888c53184de
         * Price : 6.0
         * Amount : 7.0
         * Money : 8.0
         * Memo : sample string 9
         * "DelWeightRate": 10.0 //扣重率
         */
        BillMaster billMasterBean = new BillMaster();

        billMasterBean.setPurchaseDate(bill.getTime());//日期
        billMasterBean.setSupplierID(supplier.getKeyID());//供应商ID
        billMasterBean.setNormID(specs.getKeyID());//规格ID
        billMasterBean.setCategoryID(matter.getKeyID());//品类ID

        MatterLevel matterLevel = LitePal.find(MatterLevel.class,maxSamplingDetails.getMatterLevelId());
        billMasterBean.setCategoryLv(matterLevel.getKeyID());//品类等级

        billMasterBean.setPrice(money / realWeight);//整批单价
        billMasterBean.setAmount(realWeight);//总重量
        billMasterBean.setDelWeightRate(bill.getDeductionMix());

        billMasterBean.setMoney(money);//总金额

        request.setBillMaster(billMasterBean);

        /**
         * Weight : 1.0
         * DelWeightType : a641ee4c-e57c-40ef-bd62-fae0705a284a
         */
        List<DelWeights> delWeightsList = new ArrayList<>();//扣重明细
        for (int i = 0; i < deductionList.size(); i++) {
            DelWeights delWeights = new DelWeights();
            delWeights.setDelWeightType(deductionList.get(i).getKeyID());//扣重类型ID
            delWeights.setWeight(deductionList.get(i).getWeight());//重量
            delWeightsList.add(delWeights);
        }

        request.setDelWeights(delWeightsList);//扣重明细


        /**
         * Weigth : 1.0
         * Amount : 2.0
         * SingalWeight : 3.0
         * NormsID : b8b62085-43a5-488e-9cfa-659f4c73927e
         * Ratio : 5.0
         * Files : ["sample string 1","sample string 2"]
         */
        List<PurSamples> purSamplesList = new ArrayList<>();//采样明细
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            PurSamples purSamples = new PurSamples();
            double ratio = samplingDetailsList.get(i).getSpecsProportion();//规格占比
            double amount = Double.valueOf(samplingDetailsList.get(i).getNumber());//本次采样的数量

            purSamples.setWeigth(Double.valueOf(samplingDetailsList.get(i).getWeight()));//本次采样的重量
            purSamples.setAmount(amount);//本次采样的数量
            purSamples.setSingalWeight(samplingDetailsList.get(i).getSingalWeight());//本次采样单重

            Specs specs1 = LitePal.find(Specs.class,samplingDetailsList.get(i).getSpecsId());
            purSamples.setNormsID(specs1.getKeyID());//规格ID

            purSamples.setRatio(ratio);//规格占比
            //文件路径
            List<Path> pathList = samplingDetailsList.get(i).getPathList();
            List<String> stringList = new ArrayList<>();
            for (int j = 0; j < pathList.size(); j++) {
                stringList.add(pathList.get(j).getPath());
            }
            purSamples.setFiles(stringList);//文件路径


            purSamplesList.add(purSamples);
        }
        request.setPurSamples(purSamplesList);//采样明细

        /**
         * Weight : 1.0
         */
        List<Scales> scalesList = new ArrayList<>();
        for (int i = 0; i < cumulativeList.size(); i++) {
            Scales scales = new Scales();
            scales.setWeight(Double.valueOf(cumulativeList.get(i).getWeight()));
            scalesList.add(scales);
        }
        request.setScales(scalesList);//计秤明细

        return request;

    }


}
