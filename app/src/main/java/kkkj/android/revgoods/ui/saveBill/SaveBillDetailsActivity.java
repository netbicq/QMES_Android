package kkkj.android.revgoods.ui.saveBill;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private BillDetailsAdapter adapter;
    private List<BillDetails> billDetailsList;

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
    protected void initView() {
        mQMUITipDialog = new QMUITipDialog.Builder(SaveBillDetailsActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在保存...")
                .create();

        tvTitle.setText("单据明细");
        ivBack.setOnClickListener(this);
        mBtnSaveBill.setOnClickListener(this);
        tvDeductionMix.setText("计重明细（当前扣重率：" + deductionMix + "%）");//扣重率
        mTvDeductionCount.setText(deductionList.size() + "");//扣重次数
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//扣重总重量
        tvCumulativeCount.setText(cumulativeList.size() + "");//计秤次数
        tvCumulativeWeight.setText(weight);//计秤总重量
        tvRealWeight.setText(String.valueOf(realWeight));//实际重量

        mTvTotalWeight.setText(String.valueOf(realWeight));
        mTvTotalPrice.setText(String.valueOf(money));

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        weight = intent.getStringExtra("weight");
        deductionMix = intent.getIntExtra("deductionMix", -1);

        showBill();

        supplier = LitePal.find(Supplier.class, supplierId);
        matter = LitePal.find(Matter.class, matterId);

        billDetailsList = new ArrayList<>();
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            SamplingDetails samplingDetails = samplingDetailsList.get(i);
            Specs specs = LitePal.find(Specs.class, samplingDetails.getSpecsId());

            BillDetails billDetails = new BillDetails();
            billDetails.setSpecs(specs.getValue());
            billDetails.setPrice(samplingDetails.getPrice() + "");
            billDetails.setProportion(samplingDetails.getSpecsProportion() + "");

            double weight = samplingDetails.getSpecsProportion() * realWeight;
            billDetails.setWeight(weight);

            billDetails.setTotalPrice(weight * samplingDetails.getPrice());

            billDetailsList.add(billDetails);
        }

        adapter = new BillDetailsAdapter(R.layout.item_price_details, billDetailsList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_save_bill_details;
    }


    @Override
    public void addBillSuc(boolean data) {
        if (data) {
            bill.setIsUpload(0);
            bill.save();
            if (mQMUITipDialog.isShowing()) {
                mQMUITipDialog.dismiss();
            }
            Toast.makeText(SaveBillDetailsActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
        } else {

            if (mQMUITipDialog.isShowing()) {
                mQMUITipDialog.dismiss();
            }
        }
    }

    @Override
    public void uploadfileSuc(String date) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //保存单据
            case R.id.button:

                mQMUITipDialog.show();

                bill = new Bill();
                bill.setDeductionMix(deductionMix);
                bill.setSupplierId(supplierId);
                bill.setMatterId(matterId);
                bill.setTime(stringData);
                bill.setWeight(Double.valueOf(weight));

                ContentValues values = new ContentValues();
                values.put("hasBill", 0);
                LitePal.updateAll(Cumulative.class, values);
                LitePal.updateAll(Deduction.class, values);
                LitePal.updateAll(SamplingDetails.class, values);

                bill.setCumulativeList(cumulativeList);
                bill.setDeductionList(deductionList);
                bill.setSamplingDetailsList(samplingDetailsList);
                bill.save();

                mPresenter.addBill(request);

                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setReset(true);
                EventBus.getDefault().post(deviceEvent);

                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    //显示单据
    private void showBill() {

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
            samplingDetailsList.get(i).setSpecsProportion(specsProportion);
        }
        LitePal.saveAll(samplingDetailsList);


        request = new BillModel.Request();

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
        realWeight = mWeight * (100 - deductionMix) * 0.01;

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

        supplierId = maxSamplingDetails.getSupplierId();
        matterId = maxSamplingDetails.getMatterId();


        //占比最大的规格
        Specs specs = LitePal.find(Specs.class, maxSamplingDetails.getSpecsId());

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

            Specs specs1 = LitePal.find(Specs.class, samplingDetailsList.get(i).getSpecsId());
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
        //获取当前时间 HH:mm:ss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        stringData = simpleDateFormat.format(date);
        billMasterBean.setPurchaseDate(stringData);//日期
        billMasterBean.setSupplierID(supplier.getKeyID());//供应商ID
        billMasterBean.setNormID(specs.getKeyID());//规格ID
        billMasterBean.setCategoryID(matter.getKeyID());//品类ID

        MatterLevel matterLevel = LitePal.find(MatterLevel.class, maxSamplingDetails.getMatterLevelId());
        billMasterBean.setCategoryLv(matterLevel.getKeyID());//品类等级

        billMasterBean.setPrice(money / realWeight);//整批单价
        billMasterBean.setAmount(realWeight);//总重量
        billMasterBean.setDelWeightRate(deductionMix);

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

            Specs specs1 = LitePal.find(Specs.class, samplingDetailsList.get(i).getSpecsId());
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

    }

}