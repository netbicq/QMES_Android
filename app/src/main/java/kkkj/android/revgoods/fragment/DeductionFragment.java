package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.bean.Dict;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 扣重
 */
public class DeductionFragment extends BaseDialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private Spinner mSpinner;
    private EditText mEtWeight;

    private ArrayAdapter adapter;
    private String weight;

    //扣重类别 + 品类等级
    private List<Dict> dictList;
    //扣重类别
    private List<Dict> deductionDictList;

    private Dict dict;

    private MyToasty myToasty;

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


    public void initData() {
        myToasty = new MyToasty(getContext());

        /**
         * DictType = 2:扣重类型
         * DictType = 3:品类等级
         */
        dictList = LitePal.findAll(Dict.class);
        deductionDictList = LitePal.where("DictType = ?","2").find(Dict.class);

        adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getDataSource());
        adapter.setDropDownViewResource(R.layout.item_spinner);

    }

    public List<String> getDataSource(){
        List<String> list = new ArrayList<>();
        for (int i=0;i<dictList.size();i++) {
            list.add(deductionDictList.get(i).getDictName());
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
                dict = deductionDictList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dict = deductionDictList.get(0);
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
                if (deductionDictList.size() == 0) {
                    myToasty.showWarning("请先添加扣重类别！");
                } else {

                    String wt = mEtWeight.getText().toString().trim();

                    if (!TextUtils.isEmpty(wt)) {

                        dict.setWeight(Double.valueOf(wt));
                        dict.save();

                        DeviceEvent deviceEvent = new DeviceEvent();
                        deviceEvent.setAdd(true);
                        EventBus.getDefault().post(deviceEvent);

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
