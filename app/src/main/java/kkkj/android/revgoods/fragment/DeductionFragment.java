package kkkj.android.revgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.utils.DoubleCountUtils;

/**
 * 扣重
 */
public class DeductionFragment extends BaseDialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private Spinner mSpinner;
    private EditText mEtWeight;
    private EditText mEtCount;
    private TextView mTvTotalWeight;

    private ArrayAdapter adapter;
    private String weight;

    //单重
    private double singleWeight;
    //次数
    private int deductionCount = 1;

    //扣重类别
    private List<DeductionCategory> deductionList;

    private DeductionCategory deductionCategory;
    private Deduction deduction;

    public static DeductionFragment newInstance(String weight) {
        Bundle args = new Bundle();
        args.putString("weight",weight);

        DeductionFragment fragment = new DeductionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weight = (String)getArguments().getSerializable("weight");
        singleWeight = Double.valueOf(weight);
    }


    @Override
    public void initData() {

        deductionList = LitePal.findAll(DeductionCategory.class);

        adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getDataSource());
        adapter.setDropDownViewResource(R.layout.item_spinner);

    }

    public List<String> getDataSource(){
        List<String> list = new ArrayList<>();
        for (int i=0;i<deductionList.size();i++) {
            list.add(deductionList.get(i).getName());
        }
        return list;
    }

    public void initView(View view) {
        tvTitle.setText(R.string.deduction);

        mSaveButton = view.findViewById(R.id.button);

        mEtWeight = view.findViewById(R.id.id_et_weight);
        mEtWeight.setText(weight);
        mEtCount = view.findViewById(R.id.id_et_count);
        mTvTotalWeight = view.findViewById(R.id.tv_total_weight);
        mTvTotalWeight.setText(weight);
        mSpinner = view.findViewById(R.id.id_et_number);
        mSpinner.setAdapter(adapter);
        mSaveButton.setOnClickListener(this);

        mEtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sWeight = charSequence.toString().trim();
                String sCount = mEtCount.getText().toString().trim();
                if (sWeight.length() > 0 && sCount.length() > 0) {
                    double totalWeight = Double.valueOf(sWeight);
                    singleWeight = totalWeight;
                    int count = Integer.valueOf(sCount);
                    totalWeight = DoubleCountUtils.keep(count * totalWeight);
                    mTvTotalWeight.setText(String.valueOf(totalWeight));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sCount = charSequence.toString().trim();
                String sWeight = mEtWeight.getText().toString().trim();
                if (sCount.length() > 0 && sWeight.length() > 0) {
                    int count = Integer.valueOf(sCount);
                    deductionCount = count;
                    double totalWeight = Double.valueOf(sWeight);
                    totalWeight = DoubleCountUtils.keep(count * totalWeight);
                    mTvTotalWeight.setText(String.valueOf(totalWeight));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deductionCategory = deductionList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deductionCategory = deductionList.get(0);
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_deduction;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:
                if (deductionList.size() == 0) {
                    myToasty.showWarning("请先添加扣重类别！");
                } else {

                    String wt = mEtWeight.getText().toString().trim();
                    String sCount = mEtCount.getText().toString().trim();
                    String totalWeight = mTvTotalWeight.getText().toString();


                    if (!TextUtils.isEmpty(wt) && !TextUtils.isEmpty(sCount)) {

                        deduction = new Deduction();
                        deduction.setCategory(deductionCategory.getName());
                        deduction.setWeight(Double.valueOf(totalWeight));
                        deduction.setKeyID(deductionCategory.getKeyID());

                        //获取当前时间 HH:mm:ss
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        String time = simpleDateFormat.format(date);
                        deduction.setTime(time);
                        deduction.setSingleWeight(singleWeight);
                        deduction.setCount(deductionCount);

                        deduction.save();

                        dismiss();

                    } else {
                        myToasty.showError("输入框不能为空！");
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
