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
import java.util.UUID;

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

    //?????????
    private double money = 0d;
    //???????????????
    private String weight;

    // ???????????????
    double deductionWeight = 0d;

    //????????????:???????????????????????????????????????
    double realWeight = 0d;

    //??????
    private String tempPrice = "0";

    //?????????
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

    //????????????
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
        tvDeductionMix.setText("?????????????????????????????????" + deductionMix + "%???");//?????????
        deductionCount.setText(deductionList.size() + "");//????????????
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//???????????????
        tvCumulativeCount.setText(cumulativeList.size() + "");//????????????
        tvCumulativeWeight.setText(weight);//???????????????
        tvRealWeight.setText(String.valueOf(realWeight));//????????????
        tvSpecs.setText(specs.getName());//??????

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

        //??????????????????????????????
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
            myToasty.showSuccess("???????????????");
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
            case R.id.button://??????????????????

                //????????????
                String name = UUID.randomUUID().toString();

                bill = new Bill();
                bill.setName(name);
                bill.setUUID(name);
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
                    myToasty.showSuccess("????????????!");
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
                 * "DelWeightRate": 10.0 //?????????
                 */
                BillMaster billMasterBean = new BillMaster();
                //?????????????????? HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                stringData = simpleDateFormat.format(date);
                billMasterBean.setCode(name);//Code
                billMasterBean.setPurchaseDate(stringData);//??????
                billMasterBean.setSupplierID(supplier.getKeyID());//?????????ID
                billMasterBean.setNormID(specs.getKeyID());//??????ID
                billMasterBean.setCategoryID(matter.getKeyID());//??????ID

                billMasterBean.setCategoryLv(matterLevel.getKeyID());//????????????

                billMasterBean.setPrice(DoubleCountUtils.keep(money / realWeight));//????????????
                billMasterBean.setAmount(realWeight);//?????????
                billMasterBean.setDelWeightRate(deductionMix);
                billMasterBean.setMoney(money);//?????????


                request.setBillMaster(billMasterBean);


                mQMUITipDialog.show();
                mPresenter.addBill(request);

//                final EditText editText1 = new EditText(SaveBillWithoutSamplingActivity.this);
//                //??????????????????????????????
//                editText1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
//                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(SaveBillWithoutSamplingActivity.this);
//                inputDialog1.setTitle(R.string.input_bill_name).setView(editText1);
//                inputDialog1.setPositiveButton(R.string.enter,
//                        new DialogInterface.OnClickListener() {
//                            @SuppressLint("CheckResult")
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (editText1.getText().toString().trim().length() == 0) {
//                                    new MyToasty(SaveBillWithoutSamplingActivity.this).showInfo(getResources().getString(R.string.input_bill_name));
//                                    return;
//                                }
//
//                                //????????????
//                                String name = editText1.getText().toString().trim();
//
//
//
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        }).show();

                break;

            case R.id.btn_price://????????????

                if (tempPrice.length() == 0) {
                    new MyToasty(SaveBillWithoutSamplingActivity.this).showWarning(getResources().getString(R.string.input_price));
                    return;
                }
                double price = DoubleCountUtils.keep(Double.valueOf(tempPrice));//??????
                if (price <= 0) {
                    new MyToasty(SaveBillWithoutSamplingActivity.this).showWarning("?????????????????????");
                    return;
                }

                /**
                 * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408 ??????ID
                 * Amount : 2.0
                 * Price : 3.0
                 * Menoy : 4.0 ??????
                 * Ratio : 5.0
                 */
                List<PurPrices> purPricesList = new ArrayList<>();//????????????

                PurPrices purPrices = new PurPrices();
                double ratio = 100;//????????????
                double weight = realWeight;//?????????????????????

                money = DoubleCountUtils.keep(weight * price);//?????????

                purPrices.setNormsID(specs.getKeyID());//??????ID

                purPrices.setAmount(weight);//?????????????????????
                purPrices.setPrice(price);//??????
                purPrices.setMenoy(money);//??????
                purPrices.setRatio(ratio);//????????????
                purPricesList.add(purPrices);

                request.setPurPrices(purPricesList);//????????????

                tvTotalPrice.setText(String.valueOf(money));
                tvFinalPrice.setText(String.valueOf(money));


                break;

            case R.id.iv_back:
                finish();
                break;
            default:

        }
    }




    /**
     * ??????????????????????????????
     */
    private void setPrice() {
        /**
         * ??????supplierId???matterId ,specs ?????????????????????  ?????????????????????   ????????????
         * ?????????mEtPrice.setText(?????????????????????)
         * ????????????0
         */

        List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);

        //????????????
        //?????????????????? HH:mm:ss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date nowDate = new Date(System.currentTimeMillis());//????????????
        List<Price> tempPriceList = new ArrayList<>();//???????????????????????????
        for (int j = 0; j < priceList.size(); j++) {
            String start = priceList.get(j).getStartDate();
            String end = priceList.get(j).getEndDate();
            Date startDate = new Date();
            Date endDate = new Date();
            try {
                startDate = dateFormat.parse(start);//????????????
                endDate = dateFormat.parse(end);//????????????
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                tempPriceList.add(priceList.get(j));
            }
        }

        if (tempPriceList.size() > 0) {
            Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//??????????????????
            tvPrice.setText(String.valueOf(lastPrice.getPrice()));
            tempPrice = String.valueOf(lastPrice.getPrice());
        }else {
            myToasty.showWarning("??????????????????????????????????????????");
        }


    }

    //????????????
    private BillModel.Request showBill() {
        BillModel.Request request = new BillModel.Request();

        deductionList = LitePal.where("hasBill < ?", "0")
                .find(Deduction.class);

        cumulativeList = LitePal.where("hasBill < ?", "0")
                .find(Cumulative.class, true);


        //???????????????
        double mWeight = Double.valueOf(weight);
        // ???????????????
        for (int i = 0; i < deductionList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(deductionWeight));
            BigDecimal b2 = new BigDecimal(deductionList.get(i).getWeight());
            deductionWeight = b1.add(b2).doubleValue();
        }
        //?????????
        BigDecimal b1 = new BigDecimal(Double.toString(mWeight));
        BigDecimal b2 = new BigDecimal(Double.toString(deductionWeight));
        mWeight = b1.subtract(b2).doubleValue();
        //????????????  :???????????????????????????????????????
        realWeight = DoubleCountUtils.keep(mWeight * (100 - deductionMix) * 0.01);


        /**
         * Weight : 1.0
         * DelWeightType : a641ee4c-e57c-40ef-bd62-fae0705a284a
         */
        List<DelWeights> delWeightsList = new ArrayList<>();//????????????
        for (int i = 0; i < deductionList.size(); i++) {
            DelWeights delWeights = new DelWeights();
            delWeights.setDelWeightType(deductionList.get(i).getKeyID());//????????????ID
            delWeights.setWeight(deductionList.get(i).getWeight());//??????
            delWeightsList.add(delWeights);
        }

        request.setDelWeights(delWeightsList);//????????????


        /**
         * Weigth : 1.0
         * Amount : 2.0
         * SingalWeight : 3.0
         * NormsID : b8b62085-43a5-488e-9cfa-659f4c73927e
         * Ratio : 5.0
         * Files : ["sample string 1","sample string 2"]
         */
        List<PurSamples> purSamplesList = new ArrayList<>();//????????????

        PurSamples purSamples = new PurSamples();
//            purSamples.setWeigth(0);//?????????????????????
//            purSamples.setAmount(0);//?????????????????????
//            purSamples.setSingalWeight(0);//??????????????????
//            purSamples.setNormsID("null");//??????ID
//            purSamples.setRatio(0);//????????????
//
//            //????????????
//            List<String> stringList = new ArrayList<>();
//            stringList.add("null");
//            purSamples.setFiles(stringList);//????????????
        purSamplesList.add(purSamples);

        request.setPurSamples(purSamplesList);//????????????


        /**
         * Weight : 1.0
         */
        List<Scales> scalesList = new ArrayList<>();
        for (int i = 0; i < cumulativeList.size(); i++) {
            Scales scales = new Scales();
            scales.setWeight(Double.valueOf(cumulativeList.get(i).getWeight()));
            scalesList.add(scales);
        }
        request.setScales(scalesList);//????????????

        return request;

    }

}
