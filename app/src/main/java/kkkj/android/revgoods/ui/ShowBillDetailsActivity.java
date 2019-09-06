package kkkj.android.revgoods.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillDetailsAdapter;
import kkkj.android.revgoods.adapter.BillDetailsSamplingAdapter;
import kkkj.android.revgoods.adapter.DeductionAdapter;
import kkkj.android.revgoods.adapter.WeightListAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.BillDetails;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.customer.MyLinearLayoutManager;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

public class ShowBillDetailsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_supplier)
    TextView tvSupplier;
    @BindView(R.id.tv_supplier_number)
    TextView tvSupplierNumber;
    @BindView(R.id.tv_matter)
    TextView tvMatter;
    @BindView(R.id.tv_matter_level)
    TextView tvMatterLevel;
    @BindView(R.id.tv_specs)
    TextView tvSpecs;
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
    @BindView(R.id.ll_cumulative)
    LinearLayout llCumulative;
    @BindView(R.id.recycler_view_cumulative)
    RecyclerView recyclerViewCumulative;
    @BindView(R.id.recycler_view_deduction)
    RecyclerView recyclerViewDeduction;
    @BindView(R.id.tv_deduction_weight_details)
    TextView tvDeductionWeightDetails;
    @BindView(R.id.tv_time_sampling)
    TextView tvTimeSampling;
    @BindView(R.id.tv_supplier_sampling)
    TextView tvSupplierSampling;
    @BindView(R.id.tv_supplier_number_sampling)
    TextView tvSupplierNumberSampling;
    @BindView(R.id.recycler_view_sampling)
    RecyclerView recyclerViewSampling;
    @BindView(R.id.tv_sampling_total_quantity)
    TextView tvSamplingTotalQuantity;
    @BindView(R.id.tv_sampling_total_weight)
    TextView tvSamplingTotalWeight;
    @BindView(R.id.tv_specs_sampling)
    TextView tvSpecsSampling;
    @BindView(R.id.tv_price_sampling)
    TextView tvPriceSampling;
    @BindView(R.id.tv_unit)
    TextView tvUnit;


    private List<BillDetails> billDetailsList;
    private List<String[]> weightList;
    private List<Deduction> deductionList;
    private List<SamplingDetails> samplingDetailsList;

    public static Intent newInstance(Context context, int billId) {
        Intent intent = new Intent(context, ShowBillDetailsActivity.class);
        intent.putExtra("billId", billId);

        return intent;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_show_bill_details;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.bill_details);
        ivBack.setOnClickListener(this);

        int samplingUnit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);
        switch (samplingUnit) {
            case 1://kg
                tvUnit.setText("重量(kg)");
                break;

            case 2://g
                tvUnit.setText("重量(g)");
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        billDetailsList = new ArrayList<>();
        weightList = new ArrayList<>();
        deductionList = new ArrayList<>();
        samplingDetailsList = new ArrayList<>();

        Intent intent = getIntent();
        int billId = intent.getIntExtra("billId", -1);
        Bill bill = LitePal.find(Bill.class, billId, true);
        billDetailsList = bill.getBillDetailsList();

        getBillRequest(bill);

        BillDetailsAdapter adapter = new BillDetailsAdapter(R.layout.item_price_details, billDetailsList);
        WeightListAdapter weightListAdapter = new WeightListAdapter(R.layout.item_weight_list, weightList);
        DeductionAdapter deductionAdapter = new DeductionAdapter(R.layout.item_deduction_details, deductionList);
        BillDetailsSamplingAdapter billDetailsSamplingAdapter = new BillDetailsSamplingAdapter(R.layout.item_bill_details_sampling, samplingDetailsList);

//        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerViewCumulative.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerViewCumulative.setAdapter(weightListAdapter);

        recyclerViewDeduction.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerViewDeduction.setAdapter(deductionAdapter);

        recyclerViewSampling.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerViewSampling.setAdapter(billDetailsSamplingAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void getBillRequest(Bill bill) {

        //总金额
        double money = 0d;

        deductionList = bill.getDeductionList();
        List<Cumulative> cumulativeList = bill.getCumulativeList();
        samplingDetailsList = bill.getSamplingDetailsList();
        int quantity = 0;//采样的总数量
        double samplingWeight = 0d;//采样的总重量
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            quantity = quantity + Integer.valueOf(samplingDetailsList.get(i).getNumber());
            samplingWeight = samplingWeight + Double.valueOf(samplingDetailsList.get(i).getWeight());
            samplingWeight = DoubleCountUtils.keep(samplingWeight);
        }

        tvSamplingTotalQuantity.setText(String.valueOf(quantity));
        tvSamplingTotalWeight.setText(String.valueOf(samplingWeight));

        weightList = getWeightList(cumulativeList);

        Supplier supplier = LitePal.find(Supplier.class, bill.getSupplierId());
        Matter matter = LitePal.find(Matter.class, bill.getMatterId());
        MatterLevel matterLevel = LitePal.find(MatterLevel.class, samplingDetailsList.get(0).getMatterLevelId());

        String time = bill.getTime();

        tvTime.setText(time);
        tvTimeSampling.setText(time);
        tvSupplier.setText(supplier.getName());
        tvSupplierSampling.setText(supplier.getName());
        tvSupplierNumber.setText(supplier.getKeyID());
        tvSupplierNumberSampling.setText(supplier.getKeyID());
        tvMatter.setText(matter.getName());
        tvMatterLevel.setText(matterLevel.getName());


        //计秤总重量
        double mWeight = bill.getWeight();
        tvCumulativeWeight.setText(String.valueOf(mWeight));
        // 扣重总重量
        double deductionWeight = 0d;
        for (int i = 0; i < deductionList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(deductionWeight));
            BigDecimal b2 = new BigDecimal(deductionList.get(i).getWeight());
            deductionWeight = b1.add(b2).doubleValue();
            deductionWeight = DoubleCountUtils.keep(deductionWeight);
        }
        //减扣重
        BigDecimal b1 = new BigDecimal(Double.toString(mWeight));
        BigDecimal b2 = new BigDecimal(Double.toString(deductionWeight));
        mWeight = b1.subtract(b2).doubleValue();
        //实际重量  :除去扣重，以及扣重率之后的
        double realWeight = DoubleCountUtils.keep(mWeight * (100 - bill.getDeductionMix()) * 0.01);

        /**
         * 计价方式
         * ValuationType = 1;根据规格计算
         * ValuationType = 2;根据规格占比计算
         */
        int ValuationType = matter.getValuationType();

        if (ValuationType == 2 || bill.getSamplingBySpecsId() == -1) {

            for (int i = 0; i < billDetailsList.size(); i++) {
                BillDetails billDetails = billDetailsList.get(i);

                money = billDetails.getTotalPrice() + money;//总金额
                money = DoubleCountUtils.keep(money);

            }
            tvSpecs.setVisibility(View.GONE);
        } else {
            BillDetails billDetails = billDetailsList.get(0);

            tvSpecsSampling.setText(billDetails.getSpecs());
            tvPriceSampling.setText(String.valueOf(billDetails.getPrice()));

            tvSpecs.setText(billDetails.getSpecs());
            llCumulative.setVisibility(View.GONE);
        }

        tvDeductionMix.setText("计重明细（当前扣重率：" + bill.getDeductionMix() + "%）");//扣重率
        mTvDeductionCount.setText(deductionList.size() + "");//扣重次数
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//扣重总重量
        tvDeductionWeightDetails.setText(String.valueOf(deductionWeight));//扣重明细里面扣重总重量
        tvCumulativeCount.setText(cumulativeList.size() + "");//计秤次数

        tvRealWeight.setText(String.valueOf(realWeight));//实际重量

        mTvTotalWeight.setText(String.valueOf(realWeight));
        mTvTotalPrice.setText(String.valueOf(money));

    }

    private List<String[]> getWeightList(List<Cumulative> cumulativeList) {
        List<String[]> weightList = new ArrayList<>();
        int order = 1;//序号

        for (int i = 0; i < cumulativeList.size(); i++) {

            if (cumulativeList.size() - i >= 11) {

                String[] sWeight = {String.valueOf(order), cumulativeList.get(i).getWeight(), cumulativeList.get(i + 1).getWeight(), cumulativeList.get(i + 2).getWeight()
                        , cumulativeList.get(i + 3).getWeight(), cumulativeList.get(i + 4).getWeight(), cumulativeList.get(i + 5).getWeight(), cumulativeList.get(i + 6).getWeight()
                        , cumulativeList.get(i + 7).getWeight(), cumulativeList.get(i + 8).getWeight(), cumulativeList.get(i + 9).getWeight()};

                i = i + 9;
                order++;

                weightList.add(sWeight);

            } else {
                /**
                 * 最后一排不满10个数
                 */

                //最后一排剩下数据的数量
                int lastCount = cumulativeList.size() - (i - 1);

                String[] lastWeight = new String[lastCount];
                String[] s = new String[11 - lastCount];//末尾补齐，一共需11个数据

                lastWeight[0] = String.valueOf(order);//序号
                for (int j = 1; j < lastCount; j++) {
                    lastWeight[j] = cumulativeList.get(i).getWeight();
                    i++;
                }

                for (int k = 0; k < 11 - lastCount; k++) {
                    s[k] = "";
                }
                //合并两个数组
                String[] sWeight = Arrays.copyOf(lastWeight, lastWeight.length + s.length);
                System.arraycopy(s, 0, sWeight, lastWeight.length, s.length);

                weightList.add(sWeight);

            }
        }

        return weightList;
    }

}
