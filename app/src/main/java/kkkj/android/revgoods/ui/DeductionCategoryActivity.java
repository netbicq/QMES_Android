package kkkj.android.revgoods.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.DeductionCategoryAdapter;
import kkkj.android.revgoods.bean.DeductionCategory;

public class DeductionCategoryActivity extends AppCompatActivity {


    @BindView(R.id.iv_sampling_back)
    ImageView mivBack;
    @BindView(R.id.id_tv_add_category)
    TextView mTvAddCategory;
    @BindView(R.id.button)
    Button mSaveButton;
    @BindView(R.id.id_recyclerView)
    RecyclerView mRecyclerView;

    private DeductionCategoryAdapter adapter;
    private List<DeductionCategory> deductionCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deduction_category);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        deductionCategoryList = new ArrayList<>();
        adapter = new DeductionCategoryAdapter(R.layout.item_deduction_category,deductionCategoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initView() {

    }
}
