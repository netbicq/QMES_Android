package kkkj.android.revgoods.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * 保存单据
 */
public class DelayTimeFragment extends BaseDialogFragment implements View.OnClickListener {


    private ImageView ivBack;
    private TextView tvTitle;
    private Button button;
    private EditText etSave;
    private EditText etOut;
    private EditText etZero;
    private RadioButton rbYes;
    private RadioButton rbNo;
    private RadioGroup radioGroup;

    private String zeroTime = "";
    private String saveTime = "";
    private String outTime = "";
    //是否零位启动
    private boolean isZeroStart;

    public void initData() {

        //是否零位启动，默认false
        isZeroStart = SharedPreferenceUtil.getBoolean(SharedPreferenceUtil.SP_ZERO_START);

        //置零延时
        zeroTime = String.valueOf(SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_ZERO_TIME, 0));
        //记录之后的延时
        saveTime = String.valueOf(SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAVE_TIME, 0));
        //卸料之后的延时
        outTime = String.valueOf(SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_OUT_TIME, 0));

    }


    public void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("参数设置");

        ivBack = view.findViewById(R.id.iv_back);
        etZero = view.findViewById(R.id.et_zero);
        etZero.setText(zeroTime);
        etSave = view.findViewById(R.id.et_save);
        etSave.setText(saveTime);
        etOut = view.findViewById(R.id.et_out);
        etOut.setText(outTime);
        button = view.findViewById(R.id.button);
        radioGroup = view.findViewById(R.id.radioGroup);
        rbYes = view.findViewById(R.id.rb_yes);
        rbNo = view.findViewById(R.id.rb_no);
        if (isZeroStart) {
            rbYes.setChecked(true);
        }else {
            rbNo.setChecked(true);
        }

        button.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_yes:
                        isZeroStart = true;
                        break;

                    case R.id.rb_no:
                        isZeroStart = false;
                        break;
                    default:
                        break;
                }
            }
        });

        etZero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                zeroTime = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveTime = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                outTime = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_save_bill;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (zeroTime.length() <= 0 || saveTime.length() <= 0 || outTime.length() <= 0) {
                    new MyToasty(getActivity()).showWarning("输入不能为空！");

                } else {
                    int zero = Integer.valueOf(zeroTime);
                    int save = Integer.valueOf(saveTime);
                    int out = Integer.valueOf(outTime);
                    SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_ZERO_TIME, zero);
                    SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_SAVE_TIME, save);
                    SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_OUT_TIME, out);
                    SharedPreferenceUtil.setBoolean(SharedPreferenceUtil.SP_ZERO_START,isZeroStart);
                    dismiss();
                }
                break;

            case R.id.iv_back:
                dismiss();
                break;

            default:
                break;
        }
    }


}
