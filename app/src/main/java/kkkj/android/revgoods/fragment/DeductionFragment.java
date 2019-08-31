package kkkj.android.revgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
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

    private ArrayAdapter adapter;
    private String weight;


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
        mSpinner = view.findViewById(R.id.id_et_number);
        mSpinner.setAdapter(adapter);
        mSaveButton.setOnClickListener(this);

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

                    if (!TextUtils.isEmpty(wt)) {

                        deduction = new Deduction();
                        deduction.setCategory(deductionCategory.getName());
                        deduction.setWeight(DoubleCountUtils.keep(Double.valueOf(wt)));
                        deduction.setKeyID(deductionCategory.getKeyID());
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
