package kkkj.android.revgoods.ui.saveBill;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;
import kkkj.android.revgoods.bean.bill.PurPrices;
import kkkj.android.revgoods.bean.bill.PurSamples;
import kkkj.android.revgoods.bean.bill.Scales;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.NetUtils;

public class SaveBillWithoutSamplingActivity extends BaseActivity<BillPresenter> implements BillContract.View, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.bar_container)
    RelativeLayout barContainer;
    @BindView(R.id.tv_deduction_mix)
    TextView tvDeductionMix;
    @BindView(R.id.tv_cumulative_count)
    TextView tvCumulativeCount;
    @BindView(R.id.tv_deduction_weight)
    TextView tvDeductionWeight;
    @BindView(R.id.deduction_count)
    TextView deductionCount;
    @BindView(R.id.tv_cumulative_weight)
    TextView tvCumulativeWeight;
    @BindView(R.id.tv_real_weight)
    TextView tvRealWeight;
    @BindView(R.id.tv_specs)
    TextView tvSpecs;
    @BindView(R.id.tv_price)
    EditText tvPrice;
    @BindView(R.id.tv_final_weight)
    TextView tvFinalWeight;
    @BindView(R.id.tv_final_price)
    TextView tvFinalPrice;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_price)
    Button btnPrice;

    private List<Cumulative> cumulativeList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();

    //总金额
    private double money = 0d;
    //记秤总重量
    private String weight;

    // 扣重总重量
    double deductionWeight = 0d;

    //实际重量:除去扣重，以及扣重率之后的
    double realWeight = 0d;

    //单价
    private String tempPrice = "0";

    //扣重率
    private int deductionMix;

    private int supplierId;
    private int matterId;
    private int matterLevelId;
    private int specsId;
    private Supplier supplier;
    private Matter matter;
    private MatterLevel matterLevel;
    private Specs specs;

    private BillModel.Request request;

    private Bill bill;

    private QMUITipDialog mQMUITipDialog;

    //当前时间
    private String stringData;

    public static Intent newInstance(Context context, int deductionMix, String weight, int supplierId, int matterId, int matterLevelId, int specsId) {
        Intent intent = new Intent(context, SaveBillWithoutSamplingActivity.class);
        intent.putExtra("deductionMix", deductionMix);
        intent.putExtra("weight", weight);
        intent.putExtra("supplierId", supplierId);
        intent.putExtra("matterId", matterId);
        intent.putExtra("matterLevelId", matterLevelId);
        intent.putExtra("specsId", specsId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_save_bill_without_sampling;
    }

    @Override
    protected BillPresenter getPresenter() {
        return new BillPresenter();
    }

    @Override
    protected void initView() {
        mQMUITipDialog = new QMUITipDialog.Builder(SaveBillWithoutSamplingActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getResources().getString(R.string.is_saving))
                .create();

        tvTitle.setText(R.string.bill_details);
        tvRight.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        button.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        tvDeductionMix.setText("计重明细（当前扣重率：" + deductionMix + "%）");//扣重率
        deductionCount.setText(deductionList.size() + "");//扣重次数
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//扣重总重量
        tvCumulativeCount.setText(cumulativeList.size() + "");//计秤次数
        tvCumulativeWeight.setText(weight);//计秤总重量
        tvRealWeight.setText(String.valueOf(realWeight));//实际重量
        tvSpecs.setText(specs.getName());//规格

        tvTotalWeight.setText(String.valueOf(realWeight));
        tvFinalWeight.setText(String.valueOf(realWeight));

        tvPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvPrice.addTextChangedListener(new TextWatcher() {
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
        supplierId = intent.getIntExtra("supplierId", -1);
        matterId = intent.getIntExtra("matterId", -1);
        matterLevelId = intent.getIntExtra("matterLevelId", -1);
        specsId = intent.getIntExtra("specsId", -1);
        supplier = LitePal.find(Supplier.class, supplierId);
        matter = LitePal.find(Matter.class, matterId);
        matterLevel = LitePal.find(MatterLevel.class, matterLevelId);
        specs = LitePal.find(Specs.class, specsId);

        //查询是否有匹配的单价
        setPrice();

        request = showBill();

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
            new MyToasty(SaveBillWithoutSamplingActivity.this).showSuccess("上传成功！");
        } else {

            if (mQMUITipDialog.isShowing()) {
                mQMUITipDialog.dismiss();
            }
        }
        finish();
    }

    @Override
    public void uploadfileSuc(String date) {

    }

    @Override
    public void onCompleted() {
        if (mQMUITipDialog.isShowing()) {
            mQMUITipDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button://保存上传单据

                final EditText editText1 = new EditText(SaveBillWithoutSamplingActivity.this);
                //横屏时禁止输入法全屏
                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(SaveBillWithoutSamplingActivity.this);
                inputDialog1.setTitle(R.string.input_bill_name).setView(editText1);
                inputDialog1.setPositiveButton(R.string.enter,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText1.getText().toString().trim().length() == 0) {
                                    new MyToasty(SaveBillWithoutSamplingActivity.this).showInfo(getResources().getString(R.string.input_bill_name));
                                    return;
                                }

                                //单据名称
                                String name = editText1.getText().toString().trim();

                                bill = new Bill();
                                bill.setName(name);
                                bill.setDeductionMix(deductionMix);
                                bill.setSupplierId(supplierId);
                                bill.setMatterId(matterId);
                                bill.setTime(stringData);
                                bill.setWeight(Double.valueOf(weight));

                                List<SamplingDetails> samplingDetailsList = new ArrayList<>();
                                SamplingDetails samplingDetails = new SamplingDetails();
                                samplingDetails.setMatterId(matterId);
                                samplingDetails.setMatterLevelId(matterLevelId);
                                samplingDetails.setSpecsId(specsId);
                                samplingDetails.setSupplierId(supplierId);
                                samplingDetails.setSpecsProportion(1.0);
                                samplingDetails.setPrice(Double.valueOf(tempPrice));
                                samplingDetails.setNumber("0");
                                samplingDetails.setWeight("0");
                                samplingDetails.save();

                                samplingDetailsList.add(samplingDetails);

                                ContentValues values = new ContentValues();
                                values.put("hasBill", 0);
                                LitePal.updateAll(Cumulative.class, values);
                                LitePal.updateAll(Deduction.class, values);
                                LitePal.updateAll(SamplingDetails.class, values);

                                bill.setCumulativeList(cumulativeList);
                                bill.setDeductionList(deductionList);
                                bill.setSamplingDetailsList(samplingDetailsList);

                                bill.save();

                                DeviceEvent deviceEvent = new DeviceEvent();
                                deviceEvent.setReset(true);
                                EventBus.getDefault().post(deviceEvent);

                                if (!NetUtils.checkNetWork()) {
                                    new MyToasty(SaveBillWithoutSamplingActivity.this).showSuccess("保存成功");
                                    finish();
                                    return;
                                }
                                mQMUITipDialog.show();
                                mPresenter.addBill(request);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                break;

            case R.id.btn_price://确认单价

                if (tempPrice.length() == 0) {
                    new MyToasty(SaveBillWithoutSamplingActivity.this).showWarning(getResources().getString(R.string.input_price));
                    return;
                }
                double price = DoubleCountUtils.keep(Double.valueOf(tempPrice));//单价
                if (price <= 0) {
                    new MyToasty(SaveBillWithoutSamplingActivity.this).showWarning("单价不能为零！");
                    return;
                }

                /**
                 * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408 规格ID
                 * Amount : 2.0
                 * Price : 3.0
                 * Menoy : 4.0 金额
                 * Ratio : 5.0
                 */
                List<PurPrices> purPricesList = new ArrayList<>();//计价明细

                PurPrices purPrices = new PurPrices();
                double ratio = 100;//规格占比
                double weight = realWeight;//当前占比的重量

                money = DoubleCountUtils.keep(weight * price);//总金额

                purPrices.setNormsID(specs.getKeyID());//规格ID

                purPrices.setAmount(weight);//当前占比的重量
                purPrices.setPrice(price);//单价
                purPrices.setMenoy(money);//金额
                purPrices.setRatio(ratio);//规格占比
                purPricesList.add(purPrices);

                request.setPurPrices(purPricesList);//计价明细

                tvTotalPrice.setText(String.valueOf(money));
                tvFinalPrice.setText(String.valueOf(money));

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

                billMasterBean.setCategoryLv(matterLevel.getKeyID());//品类等级

                billMasterBean.setPrice(DoubleCountUtils.keep(money / realWeight));//整批单价
                billMasterBean.setAmount(realWeight);//总重量
                billMasterBean.setDelWeightRate(deductionMix);
                billMasterBean.setMoney(money);//总金额


                request.setBillMaster(billMasterBean);



                break;

            case R.id.iv_back:
                finish();
                break;
            default:

        }
    }




    /**
     * 查询是否有匹配的单价
     */
    private void setPrice() {
        /**
         * 根据supplierId，matterId ,specs 和当前品类等级  查找价格配置表   是否匹配
         * 匹配：mEtPrice.setText(当前配置的价格)
         * 未匹配：0
         */

        List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);

        //比交时间
        //获取当前时间 HH:mm:ss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date nowDate = new Date(System.currentTimeMillis());//当前时间
        List<Price> tempPriceList = new ArrayList<>();//所有满足条件的集合
        for (int j = 0; j < priceList.size(); j++) {
            String start = priceList.get(j).getStartDate();
            String end = priceList.get(j).getEndDate();
            Date startDate = new Date();
            Date endDate = new Date();
            try {
                startDate = dateFormat.parse(start);//开始时间
                endDate = dateFormat.parse(end);//截止时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                tempPriceList.add(priceList.get(j));
            }
        }

        if (tempPriceList.size() > 0) {
            Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//最新一个价格
            tvPrice.setText(String.valueOf(lastPrice.getPrice()));
            tempPrice = String.valueOf(lastPrice.getPrice());
        }else {
            new MyToasty(SaveBillWithoutSamplingActivity.this).showInfo("当前未配置单价，请手动输入！");
        }


    }

    //显示单据
    private BillModel.Request showBill() {
        BillModel.Request request = new BillModel.Request();

        deductionList = LitePal.where("hasBill < ?", "0")
                .find(Deduction.class);

        cumulativeList = LitePal.where("hasBill < ?", "0")
                .find(Cumulative.class, true);


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

        PurSamples purSamples = new PurSamples();
//            purSamples.setWeigth(0);//本次采样的重量
//            purSamples.setAmount(0);//本次采样的数量
//            purSamples.setSingalWeight(0);//本次采样单重
//            purSamples.setNormsID("null");//规格ID
//            purSamples.setRatio(0);//规格占比
//
//            //文件路径
//            List<String> stringList = new ArrayList<>();
//            stringList.add("null");
//            purSamples.setFiles(stringList);//文件路径
        purSamplesList.add(purSamples);

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
