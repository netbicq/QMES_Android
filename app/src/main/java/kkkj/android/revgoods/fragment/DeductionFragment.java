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
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 扣重
 */
public class DeductionFragment extends DialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private Spinner mSpinner;
    private EditText mEtWeight;
    private EditText mEtPrice;
    Unbinder unbinder;
    private ImageView mBackImageView;

    private ArrayAdapter adapter;
    private String weight;
    private List<DeductionCategory> deductionCategories;
    private DeductionCategory mDeductionCategory;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deduction, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    private void initData() {
        deductionCategories = LitePal.findAll(DeductionCategory.class);
        mDeductionCategory = new DeductionCategory();

        adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getDataSource());
        adapter.setDropDownViewResource(R.layout.item_spinner);

    }

    public List<String> getDataSource(){
        List<String> list = new ArrayList<>();
        for (int i=0;i<deductionCategories.size();i++) {
            list.add(deductionCategories.get(i).getCategory());
        }
        return list;
    }

    private void initView(View view) {
        mSaveButton = view.findViewById(R.id.button);
        mEtPrice = view.findViewById(R.id.id_et_price);
        mEtWeight = view.findViewById(R.id.id_et_weight);
        mEtWeight.setText(weight);
        mSpinner = view.findViewById(R.id.id_et_number);
        mSpinner.setAdapter(adapter);
        mBackImageView = view.findViewById(R.id.iv_sampling_back);
        mBackImageView.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEtPrice.setText(deductionCategories.get(position).getPrice());
                mDeductionCategory = deductionCategories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDeductionCategory = deductionCategories.get(0);
            }
        });

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_deduction);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = (2 * width) / 3;
        wlp.height = (2 * height) / 3;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sampling_back:
                dismiss();
                break;

            case R.id.button:
                if (deductionCategories.size() == 0) {
                    Toast.makeText(getContext(),"请先添加扣重类别！",Toast.LENGTH_LONG).show();
                } else {

                    String wt = mEtWeight.getText().toString().trim();
                    String price = mEtPrice.getText().toString().trim();
                    if (!TextUtils.isEmpty(wt) && !TextUtils.isEmpty(price)) {

                        mDeductionCategory.setPrice(price);
                        mDeductionCategory.save();

                        Deduction deduction = new Deduction();
                        deduction.setCategory(mDeductionCategory.getCategory());
                        deduction.setPrice(price);
                        deduction.setWeight(wt);
                        deduction.save();

                        DeviceEvent deviceEvent = new DeviceEvent();
                        deviceEvent.setAdd(true);
                        EventBus.getDefault().post(deviceEvent);

                        dismiss();

                    } else {
                        Toast.makeText(getContext(),"输入框不能为空！",Toast.LENGTH_LONG).show();
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
        unbinder.unbind();
    }
}
