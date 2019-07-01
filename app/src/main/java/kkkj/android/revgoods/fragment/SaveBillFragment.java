package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * 保存单据
 */
public class SaveBillFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mIVBack;
    private EditText mETBillName;
    private Spinner mSpBillPurchaser;
    private Button mSaveButton;
    private ArrayAdapter adapter;

    private List<Cumulative> cumulativeList = new ArrayList<>();
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_bill, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        initData();
        initView(view);
        return view;

    }

    private void initData() {

        adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getDataSource());
        adapter.setDropDownViewResource(R.layout.item_spinner);

        deductionList = LitePal.where("hasBill < ?" ,"0")
                .find(Deduction.class);

        samplingDetailsList = LitePal.where("hasBill < ?" ,"0")
                .find(SamplingDetails.class,true);

    }

    public List<String> getDataSource() {
        List<String> list = new ArrayList<>();
        list.add("0001");
        list.add("0002");
        list.add("0003");
        list.add("0004");
        return list;
    }

    private void initView(View view) {
        mIVBack = view.findViewById(R.id.iv_back);
        mSaveButton = view.findViewById(R.id.button);
        mSaveButton.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mETBillName = view.findViewById(R.id.id_et_bill_name);
        mSpBillPurchaser = view.findViewById(R.id.id_sp_user);
        mSpBillPurchaser.setAdapter(adapter);

        mSpBillPurchaser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_save_bill);
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
            case R.id.iv_back:
                dismiss();
                break;

            case R.id.button:
                String billName = mETBillName.getText().toString().trim();

                if (!TextUtils.isEmpty(billName)) {

                    Bill bill = new Bill();
                    bill.setName(billName);

                    //获取当前时间 HH:mm:ss
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());

                    bill.setTime(simpleDateFormat.format(date));

                    for (int i = 0;i<deductionList.size();i++) {
                        Cumulative cumulative = new Cumulative();
                        Deduction deduction = new Deduction();
                        deduction = deductionList.get(i);

                        cumulative.setCount(i + 1);
                        cumulative.setCategory("扣重·" + deduction.getCategory());
                        cumulative.setWeight(deduction.getWeight());
                        cumulative.setPrice(deduction.getPrice());
                        cumulative.save();

                        LitePal.delete(Deduction.class,deduction.getId());
                    }

                    cumulativeList = LitePal.where("hasBill < ?" ,"0")
                            .find(Cumulative.class,true);

                    ContentValues values = new ContentValues();
                    values.put("hasBill",0);
                    LitePal.updateAll(Cumulative.class,values);
                    LitePal.updateAll(Deduction.class,values);
                    LitePal.updateAll(SamplingDetails.class,values);

                    bill.setCumulativeList(cumulativeList);
                    bill.setSamplingDetailsList(samplingDetailsList);
                    bill.save();


                    DeviceEvent deviceEvent = new DeviceEvent();
                    deviceEvent.setReset(true);
                    EventBus.getDefault().post(deviceEvent);
                    dismiss();

                }else {

                    Toast.makeText(getContext(),"请输入单据名称！",Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
