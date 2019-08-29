package kkkj.android.revgoods.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.customer.MyLinearLayoutManager;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.utils.DoubleCountUtils;

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


    private List<BillDetails> billDetailsList;

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

    }

    @Override
    protected void initData() {
        billDetailsList = new ArrayList<>();

        Intent intent = getIntent();
        int billId = intent.getIntExtra("billId", -1);
        Bill bill = LitePal.find(Bill.class, billId, true);
        billDetailsList = bill.getBillDetailsList();

        getBillRequest(bill);

        BillDetailsAdapter adapter = new BillDetailsAdapter(R.layout.item_price_details, billDetailsList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

        List<Deduction> deductionList = bill.getDeductionList();
        List<Cumulative> cumulativeList = bill.getCumulativeList();
        List<SamplingDetails> samplingDetailsList = bill.getSamplingDetailsList();

        Supplier supplier = LitePal.find(Supplier.class,bill.getSupplierId());
        Matter matter = LitePal.find(Matter.class, bill.getMatterId());
        MatterLevel matterLevel = LitePal.find(MatterLevel.class,samplingDetailsList.get(0).getMatterLevelId());

        String time = bill.getTime();

        tvTime.setText(time);
        tvSupplier.setText(supplier.getName());
        tvSupplierNumber.setText(supplier.getKeyID());
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
            SamplingBySpecs samplingBySpecs = LitePal.find(SamplingBySpecs.class,bill.getSamplingBySpecsId());
            Specs specs = LitePal.find(Specs.class,samplingBySpecs.getSpecsId());
            tvSpecs.setText(specs.getValue());
            llCumulative.setVisibility(View.GONE);
        }

        tvDeductionMix.setText("计重明细（当前扣重率：" + bill.getDeductionMix() + "%）");//扣重率
        mTvDeductionCount.setText(deductionList.size() + "");//扣重次数
        tvDeductionWeight.setText(String.valueOf(deductionWeight));//扣重总重量
        tvCumulativeCount.setText(cumulativeList.size() + "");//计秤次数

        tvRealWeight.setText(String.valueOf(realWeight));//实际重量

        mTvTotalWeight.setText(String.valueOf(realWeight));
        mTvTotalPrice.setText(String.valueOf(money));

    }

}
