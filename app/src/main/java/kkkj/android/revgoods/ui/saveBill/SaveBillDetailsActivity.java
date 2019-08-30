package kkkj.android.revgoods.ui.saveBill;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillDetailsAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.BillDetails;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Path;
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;
import kkkj.android.revgoods.bean.bill.PurPrices;
import kkkj.android.revgoods.bean.bill.PurSamples;
import kkkj.android.revgoods.bean.bill.Scales;
import kkkj.android.revgoods.customer.MyLinearLayoutManager;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.NetUtils;

public class SaveBillDetailsActivity extends BaseActivity<BillPresenter> implements BillContract.View,
        View.OnClickListener {

    @BindView(R.id.button)
    Button mBtnSaveBill;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_deduction_mix)
    TextView tvDeductionMix;
    @BindView(R.id.tv_cumulative_count)
    TextView tvCumulativeCount;
    @BindView(R.id.tv_deduction_weight)
    TextView tvDeductionWeight;
    @BindView(R.id.deduction_count)
    TextView mTvDeductionCount;
    @BindView(R.id.tv_cumulative_weight)
    TextView tvCumulativeWeight;
    @BindView(R.id.tv_real_weight)
    TextView tvRealWeight;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_total_weight)
    TextView mTvTotalWeight;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_init_price)
    TextView tvInitPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_final_price)
    TextView tvFinalPrice;
    @BindView(R.id.btn_price)
    Button btnPrice;

    private BillDetailsAdapter adapter;
    private List<BillDetails> billDetailsList;

    private SamplingBySpecs samplingBySpecs;
    private int samplingBySpecsId = -1;

    private List<Cumulative> cumulativeList = new ArrayList<>();
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();

    //总金额
    private double money = 0d;
    //记秤总重量
    private String weight;

    // 扣重总重量
    double deductionWeight = 0d;

    //实际重量:除去扣重，以及扣重率之后的
    double realWeight = 0d;

    //扣重率
    private int deductionMix;

    private int supplierId;
    private int matterId;
    private Supplier supplier;
    private Matter matter;

    private BillModel.Request request;

    private Bill bill;

    private QMUITipDialog mQMUITipDialog;

    //当前时间
    private String stringData;
    //占比最大的采样
    private SamplingDetails maxSamplingDetails;

    //调整单价
    private String tempPrice = "0";

    //是否确认了单价
    private boolean isMakeSurePrice = false;

    /**
     * 计价方式
     * ValuationType = 1;根据规格计算
     * ValuationType = 2;根据规格占比计算
     */
    private int ValuationType = -1;


    public static Intent newInstance(Context context, int deductionMix, String weight) {
        Intent intent = new Intent(context, SaveBillDetailsActivity.class);
        intent.putExtra("deductionMix", deductionMix);
        intent.putExtra("weight", weight);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected BillPresenter getPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_save_bill_details;
    }

    @Override
    protected void initView() {

        mQMUITipDialog = new QMUITipDialog.Builder(SaveBillDetailsActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getResources().getString(R.string.is_saving))
                .create();

        tvTitle.setText(R.string.bill_details);
        tvRight.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        mBtnSaveBill.setOnClickListener(this);

        btnPrice.setOnClickListener(this);

        tvDeductionMix.setText("计重明细（当前扣重率：" + deductionMix + "%）");//扣重率
        mTvDeductionCount.setText(deductionList.size() + "");//扣重次数
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//扣重总重量
        tvCumulativeCount.setText(cumulativeList.size() + "");//计秤次数
        tvCumulativeWeight.setText(weight);//计秤总重量
        tvRealWeight.setText(String.valueOf(realWeight));//实际重量

        mTvTotalWeight.setText(String.valueOf(realWeight));
        mTvTotalPrice.setText(String.valueOf(money));


        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tempPrice = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        weight = intent.getStringExtra("weight");
        deductionMix = intent.getIntExtra("deductionMix", -1);

        request = showBill();

        billDetailsList = new ArrayList<>();
        if (ValuationType == 2) {
            //根据规格占比

            tvInitPrice.setText("   /   ");
            tvFinalPrice.setText("   /   ");

            List<SamplingDetails> list = getSamplingDetails(samplingDetailsList);//合并规格相同的采样

            for (int i = 0; i < list.size(); i++) {
                SamplingDetails samplingDetails = list.get(i);
                Specs specs = LitePal.find(Specs.class, samplingDetails.getSpecsId());

                BillDetails billDetails = new BillDetails();
                billDetails.setSpecs(specs.getValue());
                billDetails.setSpecsKeyId(specs.getKeyID());
                billDetails.setPrice(samplingDetails.getPrice() + "");
                billDetails.setAdjustPrice("0");
                billDetails.setFinalPrice(samplingDetails.getPrice() + "");
                billDetails.setProportion(samplingDetails.getSpecsProportion() + "");

                double weight = samplingDetails.getSpecsProportion() * realWeight;
                billDetails.setWeight(DoubleCountUtils.keep(weight));

                billDetails.setTotalPrice(DoubleCountUtils.keep(weight * samplingDetails.getPrice()));

                billDetailsList.add(billDetails);
            }
        } else {
            //根据规格计价

            List<SamplingBySpecs> bySpecsList = LitePal.where("hasBill < ?", "0")
                    .find(SamplingBySpecs.class);
            samplingBySpecs = bySpecsList.get(0);

            Logger.d("bySpecsListSize" +  bySpecsList.size());

            Specs specs = LitePal.find(Specs.class, samplingBySpecs.getSpecsId());
            //初始单价
            String price = String.valueOf(samplingBySpecs.getPrice());
            tvInitPrice.setText(price);
            //最终单价
            tvFinalPrice.setText(price);

            BillDetails billDetails = new BillDetails();
            billDetails.setSpecs(specs.getValue());
            billDetails.setPrice(price);
            billDetails.setAdjustPrice("0");
            billDetails.setFinalPrice(price);
            billDetails.setProportion("1");
            billDetails.setWeight(realWeight);
            billDetails.setTotalPrice(DoubleCountUtils.keep(samplingBySpecs.getPrice() * realWeight));

            billDetailsList.add(billDetails);
        }

        adapter = new BillDetailsAdapter(R.layout.item_price_details, billDetailsList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void addBillSuc(boolean data) {
        Logger.d(data);
        if (data) {
            bill.setIsUpload(0);
            bill.save();
            if (mQMUITipDialog.isShowing()) {
                mQMUITipDialog.dismiss();
            }
            myToasty.showSuccess("上传成功！");
        } else {

            if (mQMUITipDialog.isShowing()) {
                mQMUITipDialog.dismiss();
            }
        }

        finish();

    }

    @Override
    public void onCompleted() {
        if (mQMUITipDialog.isShowing()) {
            mQMUITipDialog.dismiss();
        }
    }

    @Override
    public void uploadfileSuc(String date) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //上传附件
            case R.id.tv_right:

                break;

            //确认单价
            case R.id.btn_price:
                money = 0d;
                //调整单价
                double adjustPrice = DoubleCountUtils.keep(Double.valueOf(tempPrice));

                /**
                 * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408 规格ID
                 * Amount : 2.0
                 * Price : 3.0
                 * Menoy : 4.0 金额
                 * Ratio : 5.0
                 */
                List<PurPrices> purPricesList = new ArrayList<>();//计价明细

                if (ValuationType == 1) {
                    //根据规格
                    BillDetails billDetails = billDetailsList.get(0);
                    //最终单价
                    double finalPrice = 0d;
                    //对应重量
                    double weight = 0d;
                    //最终价格
                    double totalPrice = 0d;

                    finalPrice = Double.valueOf(billDetails.getPrice()) + adjustPrice;
                    weight = billDetails.getWeight();
                    totalPrice = DoubleCountUtils.keep(weight * finalPrice);
                    billDetails.setAdjustPrice(String.valueOf(adjustPrice));
                    billDetails.setFinalPrice(String.valueOf(finalPrice));
                    billDetails.setTotalPrice(totalPrice);
                    billDetails.save();

                    tvFinalPrice.setText(String.valueOf(finalPrice));

                    samplingBySpecs.setPrice(Double.valueOf(billDetails.getPrice()));
                    samplingBySpecs.setAdjustPrice(adjustPrice);
                    samplingBySpecs.setFinalPrice(finalPrice);

                    samplingBySpecs.save();

                    samplingBySpecsId = samplingBySpecs.getSpecsId();
                    Specs specs = LitePal.find(Specs.class, samplingBySpecsId);

                    PurPrices purPrices = new PurPrices();
                    purPrices.setNormsID(specs.getKeyID());//规格ID

                    purPrices.setAmount(realWeight);//当前占比的重量
                    purPrices.setInitialPrice(Double.valueOf(billDetails.getPrice()));//初始单价
                    purPrices.setRevisionPrice(adjustPrice);//调整单价
                    purPrices.setPrice(finalPrice);//最终单价

                    money = totalPrice;//总金额

                    purPrices.setMenoy(money);//金额

                    mTvTotalPrice.setText(String.valueOf(money));

                    purPrices.setRatio(100);//规格占比
                    purPricesList.add(purPrices);

                } else {

                    //根据规格占比

                    for (int i = 0; i < billDetailsList.size(); i++) {

                        //最终单价
                        double finalPrice = 0d;
                        //对应重量
                        double weight = 0d;
                        //最终价格
                        double totalPrice = 0d;

                        BillDetails billDetails = billDetailsList.get(i);

                        PurPrices purPrices = new PurPrices();
                        double ratio = Double.valueOf(billDetailsList.get(i).getProportion()) * 100;//规格占比
                        weight = billDetails.getWeight();//当前占比的重量
                        finalPrice = Double.valueOf(billDetails.getPrice()) + adjustPrice;//最终单价
                        totalPrice = DoubleCountUtils.keep(weight * finalPrice);
                        billDetails.setAdjustPrice(String.valueOf(adjustPrice));
                        billDetails.setFinalPrice(String.valueOf(finalPrice));
                        billDetails.setTotalPrice(totalPrice);

                        billDetails.save();

                        money = totalPrice + money;//总金额
                        money = DoubleCountUtils.keep(money);

                        purPrices.setNormsID(billDetails.getSpecsKeyId());//规格ID

                        purPrices.setAmount(weight);//当前占比的重量
                        purPrices.setInitialPrice(Double.valueOf(billDetails.getPrice()));//初始单价
                        purPrices.setRevisionPrice(adjustPrice);//调整单价
                        purPrices.setPrice(finalPrice);//最终单价
                        purPrices.setMenoy(totalPrice);//金额
                        purPrices.setRatio(ratio);//规格占比
                        purPricesList.add(purPrices);
                    }

                    mTvTotalPrice.setText(String.valueOf(money));

                }
                adapter.notifyDataSetChanged();
                isMakeSurePrice = true;
                request.setPurPrices(purPricesList);//计价明细


                break;

            //保存单据
            case R.id.button:

                if (!isMakeSurePrice) {
                    myToasty.showWarning("请先确认金额！");
                    return;
                }
                //单据名称
                String name = UUID.randomUUID().toString();

                //获取当前时间 HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                stringData = simpleDateFormat.format(date);

                bill = new Bill();
                bill.setUUID(name);
                bill.setName(name);
                bill.setDeductionMix(deductionMix);
                bill.setSupplierId(supplierId);
                bill.setMatterId(matterId);
                bill.setTime(stringData);
                bill.setSamplingBySpecsId(samplingBySpecsId);
                bill.setBillDetailsList(billDetailsList);


                bill.setWeight(Double.valueOf(weight));

//                Bitmap bitmap = CodeUtils.createBarcode(name,100,40,true);
//                Logger.d(bitmap);

                ContentValues values = new ContentValues();
                values.put("hasBill", 0);
                LitePal.updateAll(Cumulative.class, values);
                LitePal.updateAll(Deduction.class, values);
                LitePal.updateAll(SamplingDetails.class, values);
                LitePal.updateAll(SamplingBySpecs.class, values);

                bill.setCumulativeList(cumulativeList);
                bill.setDeductionList(deductionList);
                bill.setSamplingDetailsList(samplingDetailsList);
                bill.save();

                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setReset(true);
                EventBus.getDefault().post(deviceEvent);


                if (!NetUtils.checkNetWork()) {
                    myToasty.showSuccess("保存成功!");
                    finish();
                    return;
                }

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
                //占比最大的规格
                Specs specs = LitePal.find(Specs.class, maxSamplingDetails.getSpecsId());

                BillMaster billMasterBean = new BillMaster();

                billMasterBean.setCode(name);
                billMasterBean.setPurchaseDate(stringData);//日期
                billMasterBean.setSupplierID(supplier.getKeyID());//供应商ID
                billMasterBean.setNormID(specs.getKeyID());//规格ID
                billMasterBean.setCategoryID(matter.getKeyID());//品类ID

                MatterLevel matterLevel = LitePal.find(MatterLevel.class, maxSamplingDetails.getMatterLevelId());
                billMasterBean.setCategoryLv(matterLevel.getKeyID());//品类等级

                billMasterBean.setPrice(DoubleCountUtils.keep(money / realWeight));//整批单价
                billMasterBean.setAmount(realWeight);//总重量
                billMasterBean.setDelWeightRate(deductionMix);
                billMasterBean.setMoney(money);//总金额


                request.setBillMaster(billMasterBean);

                mQMUITipDialog.show();

                mPresenter.addBill(request);


//                final EditText editText1 = new EditText(SaveBillDetailsActivity.this);
//                //横屏时禁止输入法全屏
//                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
//                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(SaveBillDetailsActivity.this);
//                inputDialog1.setTitle(R.string.input_bill_name).setView(editText1);
//                inputDialog1.setPositiveButton(R.string.enter,
//                        new DialogInterface.OnClickListener() {
//                            @SuppressLint("CheckResult")
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (editText1.getText().toString().trim().length() == 0) {
//                                    new MyToasty(SaveBillDetailsActivity.this).showInfo(getResources().getString(R.string.input_bill_name));
//                                    return;
//                                }
//
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        }).show();

                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    //显示单据
    private BillModel.Request showBill() {
        BillModel.Request request = new BillModel.Request();

        deductionList = LitePal.where("hasBill < ?", "0")
                .find(Deduction.class);
        samplingDetailsList = LitePal.where("hasBill < ?", "0")
                .find(SamplingDetails.class, true);
        cumulativeList = LitePal.where("hasBill < ?", "0")
                .find(Cumulative.class, true);

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
            samplingDetailsList.get(i).setSpecsProportion(DoubleCountUtils.keep4(specsProportion));
        }
        LitePal.saveAll(samplingDetailsList);


        //计秤总重量
        double mWeight = Double.valueOf(weight);
        // 扣重总重量
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
        realWeight = DoubleCountUtils.keep(mWeight * (100 - deductionMix) * 0.01);


        if (samplingDetailsList.size() == 1) {
            maxSamplingDetails = samplingDetailsList.get(0);
        } else {
            //规格占比最大的采样
            maxSamplingDetails = Collections.max(samplingDetailsList, new Comparator<SamplingDetails>() {
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
        }


        supplierId = maxSamplingDetails.getSupplierId();
        matterId = maxSamplingDetails.getMatterId();
        supplier = LitePal.find(Supplier.class, supplierId);
        matter = LitePal.find(Matter.class, matterId);
        /**
         * 计价方式
         * ValuationType = 1;根据规格计算
         * ValuationType = 2;根据规格占比计算
         */
        ValuationType = matter.getValuationType();




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

            int samplingId = samplingDetailsList.get(i).getId();
            SamplingDetails samplingDetails1 = LitePal.find(SamplingDetails.class, samplingId, true);

            double ratio = samplingDetails1.getSpecsProportion() * 100;//规格占比
            double amount = Double.valueOf(samplingDetails1.getNumber());//本次采样的数量

            purSamples.setWeigth(DoubleCountUtils.keep(Double.valueOf(samplingDetails1.getWeight())));//本次采样的重量
            purSamples.setAmount(DoubleCountUtils.keep(amount));//本次采样的数量
            purSamples.setSingalWeight(samplingDetails1.getSingalWeight());//本次采样单重

            Specs specs1 = LitePal.find(Specs.class, samplingDetails1.getSpecsId());
            purSamples.setNormsID(specs1.getKeyID());//规格ID

            purSamples.setRatio(ratio);//规格占比


            //文件路径
            List<Path> pathList = samplingDetails1.getPathList();
            List<String> stringList = new ArrayList<>();
            for (int j = 0; j < pathList.size(); j++) {
                stringList.add(pathList.get(j).getPath());
            }
            purSamples.setFiles(stringList);//文件路径

            for (int j = 0; j < stringList.size(); j++) {
                Logger.d(stringList.get(j));
            }

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

    //合并规格相同的采样
    private List<SamplingDetails> getSamplingDetails(List<SamplingDetails> samplingDetailsList) {

        int size = samplingDetailsList.size();

        for (int i = 0; i < size; i++) {

            SamplingDetails samplingDetails = samplingDetailsList.get(i);
            double proportion = samplingDetails.getSpecsProportion();

            for (int j = i + 1; j < size; j++) {
                SamplingDetails samplingDetailsNext = samplingDetailsList.get(j);
                if (samplingDetails.getSpecsId() == samplingDetailsNext.getSpecsId()) {
                    proportion = DoubleCountUtils.keep4(proportion + samplingDetailsNext.getSpecsProportion());
                    samplingDetailsList.remove(samplingDetailsNext);
                    size = size - 1;
                }
            }
            samplingDetails.setSpecsProportion(proportion);

        }

        return samplingDetailsList;
    }


}
