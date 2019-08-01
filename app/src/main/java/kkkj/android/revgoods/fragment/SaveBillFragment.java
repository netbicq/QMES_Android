package kkkj.android.revgoods.fragment;

import android.content.ContentValues;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 保存单据
 */
public class SaveBillFragment extends BaseDialogFragment implements View.OnClickListener {

    private EditText mETBillName;
    //扣重率
    private EditText mETDudection;
    private Spinner mSpBillPurchaser;
    private Spinner mSpMatterLevel;
    private Button mSaveButton;
    private ArrayAdapter userAdapter;
    private ArrayAdapter matterLevelAdapter;

    private List<Cumulative> cumulativeList = new ArrayList<>();
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();
    //品类等级
    private MatterLevel matterLevel;
    private List<MatterLevel> matterLevels;

    private int supplierId;
    private int matterId;
    private int specsId;

    public static SaveBillFragment newInstance(int supplierId,int matterId,int specsId) {
        Bundle args = new Bundle();
        args.putInt("supplierId",supplierId);
        args.putInt("matterId",matterId);
        args.putInt("specsId",specsId);

        SaveBillFragment fragment = new SaveBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supplierId = (int)getArguments().getSerializable("supplierId");
        matterId = (int)getArguments().getSerializable("matterId");
        specsId = (int)getArguments().getSerializable("specsId");
    }

    public void initData() {

        userAdapter = new ArrayAdapter<>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getUserSource());
        userAdapter.setDropDownViewResource(R.layout.item_spinner);

        matterLevelAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,
                getMatterLevelSource());
        matterLevelAdapter.setDropDownViewResource(R.layout.item_spinner);

        deductionList = LitePal.where("hasBill < ?" ,"0")
                .find(Deduction.class);

        samplingDetailsList = LitePal.where("hasBill < ?" ,"0")
                .find(SamplingDetails.class,true);

    }

    public List<String> getUserSource() {
        List<String> list = new ArrayList<>();
        list.add("请选择采购人");
        list.add("0001");
        list.add("0002");
        list.add("0003");
        list.add("0004");
        return list;
    }

    private List<String> getMatterLevelSource() {
        matterLevels = LitePal.findAll(MatterLevel.class);
        List<String> list = new ArrayList<>();
        list.add("请选择品类等级");
        for (MatterLevel matterLevel:matterLevels) {
            list.add(matterLevel.getLevel());
        }
        return list;
    }

    public void initView(View view) {
        tvTitle.setText(R.string.save_bill);

        mSaveButton = view.findViewById(R.id.button);
        mSaveButton.setOnClickListener(this);
        mETBillName = view.findViewById(R.id.id_et_bill_name);
        mETDudection = view.findViewById(R.id.id_et_deduction);
        mSpBillPurchaser = view.findViewById(R.id.id_sp_user);
        mSpBillPurchaser.setAdapter(userAdapter);
        mSpMatterLevel = view.findViewById(R.id.id_sp_matter_level);
        mSpMatterLevel.setAdapter(matterLevelAdapter);

        mSpBillPurchaser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpMatterLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                matterLevel = matterLevels.get(i - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                String billName = mETBillName.getText().toString().trim();
                String deductionMix = mETDudection.getText().toString().trim();

                if (!TextUtils.isEmpty(billName) && !TextUtils.isEmpty(deductionMix)) {

                    if (matterLevel != null) {

                        Bill bill = new Bill();
                        bill.setName(billName);
                        bill.setDeductionMix(deductionMix);

                        Supplier supplier = LitePal.find(Supplier.class,supplierId);
                        Matter matter = LitePal.find(Matter.class,matterId);
                        Specs specs = LitePal.find(Specs.class,specsId);

                        bill.setSupplier(supplier);
                        bill.setMatter(matter);
                        bill.setSpecs(specs);
                        bill.setMatterLevel(matterLevel);

                        //获取当前时间 HH:mm:ss
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());

                        bill.setTime(simpleDateFormat.format(date));

                        for (int i = 0;i<deductionList.size();i++) {
                            Cumulative cumulative = new Cumulative();
                            Deduction deduction = deductionList.get(i);

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
                        new MyToasty(getContext()).showWarning("请选择品类等级！");
                    }

                }else {
                    new MyToasty(getContext()).showWarning("请输入单据名称！");
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
