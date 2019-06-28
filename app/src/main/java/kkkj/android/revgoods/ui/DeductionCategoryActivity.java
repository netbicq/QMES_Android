package kkkj.android.revgoods.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.DeductionCategoryAdapter;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.customer.SlideRecyclerView;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

public class DeductionCategoryActivity extends AppCompatActivity {


    @BindView(R.id.iv_sampling_back)
    ImageView mIvBack;
    @BindView(R.id.id_tv_add_category)
    TextView mTvAddCategory;
    @BindView(R.id.button)
    Button mSaveButton;
    @BindView(R.id.id_recyclerView)
    SlideRecyclerView mRecyclerView;
    @BindView(R.id.id_constraintLayout)
    ConstraintLayout mConstraintLayout;

    private DeductionCategoryAdapter adapter;
    private List<DeductionCategory> deductionCategoryList;
    private View mAddCategoryView;
    private EditText mEditTextCategory;
    private EditText mEditTextPrice;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_deduction_category);
        ButterKnife.bind(this);

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        initData();
        initView();
    }

    private void initData() {
        deductionCategoryList = new ArrayList<>();
        deductionCategoryList = LitePal.findAll(DeductionCategory.class);

        adapter = new DeductionCategoryAdapter(R.layout.item_deduction_category, deductionCategoryList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = deductionCategoryList.get(position).getId();
                LitePal.delete(DeductionCategory.class,id);
                deductionCategoryList.remove(position);
                adapter.notifyDataSetChanged();
                mRecyclerView.closeMenu();
            }
        });
    }

    private void initView() {
        mAddCategoryView = getLayoutInflater().inflate(R.layout.add_deduction_category,
                mConstraintLayout, false);
        mEditTextCategory = mAddCategoryView.findViewById(R.id.id_et_category);
        mEditTextPrice = mAddCategoryView.findViewById(R.id.id_et_price);

        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setCanMove(true);
        mRecyclerView.setAdapter(adapter);

    }

    @OnClick({R.id.iv_sampling_back, R.id.id_tv_add_category, R.id.button,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sampling_back:

                finish();
                break;

            case R.id.id_tv_add_category:
                adapter.setFooterView(mAddCategoryView);
                adapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(deductionCategoryList.size());
                //mRecyclerView.scrollToPosition(keyboardHeight);
                break;

            case R.id.button:
                String category = mEditTextCategory.getText().toString().trim();
                String price = mEditTextPrice.getText().toString().trim();
                
                if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(price)) {
                    DeductionCategory deductionCategory = new  DeductionCategory();
                    deductionCategory.setCategory(mEditTextCategory.getText().toString().trim());
                    deductionCategory.setPrice(mEditTextPrice.getText().toString().trim());
                    deductionCategory.save();
                    adapter.removeFooterView(mAddCategoryView);
                    deductionCategoryList.add(deductionCategory);
                    adapter.notifyDataSetChanged();

                    mEditTextPrice.setText("");
                    mEditTextCategory.setText("");
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG)));
    }

}
