package kkkj.android.revgoods.ui.addDeductionCategory;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.DeductionCategoryAdapter;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.ui.BaseActivity;
import kkkj.android.revgoods.utils.NetUtils;

public class DeductionCategoryActivity extends BaseActivity<DeductionCategoryPresenter>implements DeductionCategoryContract.View {

    @BindView(R.id.iv_sampling_back)
    ImageView mIvBack;
    @BindView(R.id.id_tv_add_category)
    TextView mTvAddCategory;
    @BindView(R.id.button)
    Button mSaveButton;
    @BindView(R.id.id_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_constraintLayout)
    ConstraintLayout mConstraintLayout;

    private DeductionCategoryAdapter adapter;
    private List<DeductionCategory> deductionCategoryList;
    private View mAddCategoryView;
    private EditText mEditTextCategory;

    private DeductionCategory deductionCategory;


    @Override
    protected DeductionCategoryPresenter getPresenter() {
        return new DeductionCategoryPresenter();
    }

    @Override
    protected void initData() {
        deductionCategoryList = new ArrayList<>();
        mPresenter.getDeductionCategory();
        adapter = new DeductionCategoryAdapter(R.layout.item_card_view, deductionCategoryList);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_deduction_category;
    }

    @Override
    protected void initView() {
        mAddCategoryView = getLayoutInflater().inflate(R.layout.add_deduction_category,
                mConstraintLayout, false);
        mEditTextCategory = mAddCategoryView.findViewById(R.id.id_et_category);
//        linearLayoutManager = new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL,false);
//        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
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
                if (!NetUtils.checkNetWork()) {
                    myToasty.showWarning("当前网络连接不可用，请联网后重试！");
                    return;
                }
                String category = mEditTextCategory.getText().toString().trim();

                if (!TextUtils.isEmpty(category)) {

                    deductionCategory = new DeductionCategory();
                    deductionCategory.setName(mEditTextCategory.getText().toString().trim());

                    AddDeductionCategoryModel.Request request = new AddDeductionCategoryModel.Request();

                    request.setDictName(mEditTextCategory.getText().toString().trim());

                    mPresenter.addDeductionCategory(request);

                    adapter.removeFooterView(mAddCategoryView);
                    deductionCategoryList.add(deductionCategory);
                    adapter.notifyDataSetChanged();

                    mEditTextCategory.setText("");
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void getDeductionCategorySuc(List<DeductionCategory> data) {
        deductionCategoryList.clear();
        deductionCategoryList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addDeductionCategorySuc(DeductionCategory data) {
        if (data != null) {
            myToasty.showSuccess("添加成功！");
            data.save();
        }


    }
}
