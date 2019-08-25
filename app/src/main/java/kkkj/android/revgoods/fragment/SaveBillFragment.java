package kkkj.android.revgoods.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import kkkj.android.revgoods.utils.NetUtils;

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
    private RadioGroup mRadioGroup;
    private ArrayAdapter userAdapter;
    private ArrayAdapter matterLevelAdapter;

    private List<Cumulative> cumulativeList = new ArrayList<>();
    private List<SamplingDetails> samplingDetailsList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();
    //品类等级
    private MatterLevel matterLevel;
    private List<MatterLevel> matterLevels;

    //重量
    private String weight;
    private int supplierId;
    private int matterId;
    private int specsId;
    //通过服务端获取单价
    private static final int BY_SEVER = 0;
    //通过客户端（本地）获取单价
    private static final int BY_CLIENT = 1;
    private int byWho = BY_SEVER;

    public static SaveBillFragment newInstance(int supplierId, int matterId, int specsId, String weight) {
        Bundle args = new Bundle();
        args.putInt("supplierId", supplierId);
        args.putInt("matterId", matterId);
        args.putInt("specsId", specsId);
        args.putString("weight", weight);

        SaveBillFragment fragment = new SaveBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            supplierId = (int) getArguments().getSerializable("supplierId");
            matterId = (int) getArguments().getSerializable("matterId");
            specsId = (int) getArguments().getSerializable("specsId");
            weight = (String) getArguments().getSerializable("weight");
        }

    }

    public void initData() {

        userAdapter = new ArrayAdapter<>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, getUserSource());
        userAdapter.setDropDownViewResource(R.layout.item_spinner);

        matterLevelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getMatterLevelSource());
        matterLevelAdapter.setDropDownViewResource(R.layout.item_spinner);

        deductionList = LitePal.where("hasBill < ?", "0")
                .find(Deduction.class);

        samplingDetailsList = LitePal.where("hasBill < ?", "0")
                .find(SamplingDetails.class, true);

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
        for (MatterLevel matterLevel : matterLevels) {
            list.add(matterLevel.getName());
        }
        return list;
    }

    public void initView(View view) {
        tvTitle.setText(R.string.save_bill);

        mRadioGroup = view.findViewById(R.id.radio_group);
        mSaveButton = view.findViewById(R.id.button);
        mSaveButton.setOnClickListener(this);
        mETBillName = view.findViewById(R.id.id_et_bill_name);
        mETDudection = view.findViewById(R.id.id_et_deduction);
        mSpBillPurchaser = view.findViewById(R.id.id_sp_user);
        mSpBillPurchaser.setAdapter(userAdapter);
        mSpMatterLevel = view.findViewById(R.id.id_sp_matter_level);
        mSpMatterLevel.setAdapter(matterLevelAdapter);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_server:
                        byWho = BY_SEVER;
                        break;

                    case R.id.radioButton_client:
                        byWho = BY_CLIENT;
                        break;
                    default:
                        break;
                }
            }
        });

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
                //扣重率
                String deductionMix = mETDudection.getText().toString().trim();

                if (!TextUtils.isEmpty(billName) && !TextUtils.isEmpty(deductionMix)) {

                    if (matterLevel != null) {

                        //重量
                        double mWeight = Double.valueOf(weight);
                        // 减扣重
                        for (int i=0;i<deductionList.size();i++) {
                            BigDecimal b1 = new BigDecimal(Double.toString(mWeight));
                            BigDecimal b2 = new BigDecimal(deductionList.get(i).getWeight());
                            mWeight = b1.subtract(b2).doubleValue();
                        }
                        //除去扣重，以及扣重率之后的实际重量
                        double realWeight = mWeight * (100 - Integer.valueOf(deductionMix) * 0.01);

                        /**
                         * 1.根据所选择的品类，品类决定哪种方式计算价格
                         *      （1）根据最大规格占比计算价格； 最大规格所对应的单价  *  重量
                         *      （2）根据各个规格占比计算价格： 根据占比情况计算各种规格的重量  *  对应单价
                         *
                         */
                        //当前所选择的品类
                        Supplier supplier = LitePal.find(Supplier.class, supplierId);
                        Matter matter = LitePal.find(Matter.class, matterId);
                        Specs specs = LitePal.find(Specs.class, specsId);

                        int type = matter.getType();
                        switch (type) {

                            case 0://根据最大规格计算
                                //规格占比最大的采样
                                SamplingDetails maxSamplingDetails = Collections.max(samplingDetailsList, new Comparator<SamplingDetails>() {
                                    @Override
                                    public int compare(SamplingDetails samplingDetails, SamplingDetails t1) {
                                        if (samplingDetails.getSpecsProportion() > t1.getSpecsProportion()) {
                                            return 1;
                                        } else if (samplingDetails.getSpecsProportion() == t1.getSpecsProportion()) {
                                            return 0;
                                        } else {
                                            return -1;
                                        }
                                    }

                                });
                                //当前采样对应的规格-->得出单价 price
                                //Specs specsBySampling = maxSamplingDetails.getSpecs();
                                //double totalPrice = realWeight * price;

                                break;

                            case 1://根据规格占比计算

                                break;

                            default:
                                break;
                        }


                        Bill bill = new Bill();
                        bill.setName(billName);
                       // bill.setDeductionMix(deductionMix);

                        bill.setSupplierId(supplierId);
                        bill.setMatterId(matterId);

                        //获取当前时间 HH:mm:ss
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());

                        bill.setTime(simpleDateFormat.format(date));


                        cumulativeList = LitePal.where("hasBill < ?", "0")
                                .find(Cumulative.class, true);

                        ContentValues values = new ContentValues();
                        values.put("hasBill", 0);
                        LitePal.updateAll(Cumulative.class, values);
                        LitePal.updateAll(Deduction.class, values);
                        LitePal.updateAll(SamplingDetails.class, values);

                        bill.setCumulativeList(cumulativeList);
                        bill.setDeductionList(deductionList);
                        bill.setSamplingDetailsList(samplingDetailsList);
                        bill.save();


                        DeviceEvent deviceEvent = new DeviceEvent();
                        deviceEvent.setReset(true);
                        EventBus.getDefault().post(deviceEvent);
                        dismiss();

                    } else {
                        new MyToasty(getContext()).showWarning(getResources().getString(R.string.choose_matter_level));
                    }

                } else {
                    new MyToasty(getContext()).showWarning(getResources().getString(R.string.input_bill_name));
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
