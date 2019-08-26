package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SamplingDetailsAdapter;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.customer.SlideRecyclerView;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.utils.DoubleCountUtils;

/**
 * 采样明细
 */
public class SamplingDetailsFragment extends BaseDialogFragment implements View.OnClickListener {

    private SlideRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private LinearLayout linearLayout;
    private SamplingDetailsAdapter adapter;
    private List<SamplingDetails> samplingDetailsList;
    private ShowSamplingPictureFragment mFragment;
    private TextView tvCount;
    private TextView tvWeight;
    private Spinner spSpecs;
    private EditText tvPrice;
    private Button btnSave;

    private String tempPrice = "";

    private int count = 0;
    private double weight = 0d;

    private Matter matter;
    private Supplier supplier;
    private MatterLevel matterLevel;
    /**
     * 计价方式
     * ValuationType = 1;根据规格计算
     * ValuationType = 2;根据规格占比计算
     */
    private int ValuationType = 0;

    //单价平均值
    private double price;
    private int samplingSize = 0;

    private List<Specs> specsList;
    private List<String> specsNameList;
    private ArrayAdapter specsAdapter;
    private Specs specs;
    private Specs tempSpecs;

    private int position = 0;

    @Override
    public void initData() {

        samplingDetailsList = new ArrayList<>();
        samplingDetailsList = LitePal.where("hasBill < ?", "0")
                .find(SamplingDetails.class, true);

        samplingSize = samplingDetailsList.size();
        if (samplingSize > 0) {
            matter = LitePal.find(Matter.class, samplingDetailsList.get(0).getMatterId());
            matterLevel = LitePal.find(MatterLevel.class, samplingDetailsList.get(0).getMatterLevelId());
            supplier = LitePal.find(Supplier.class, samplingDetailsList.get(0).getSupplierId());
            ValuationType = matter.getValuationType();
        }
        //采样总重量
        double totalWeight = 0d;
        //采样总单价
        double totalPrice = 0d;
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(totalWeight));
            BigDecimal b2 = new BigDecimal(samplingDetailsList.get(i).getWeight());
            totalWeight = b1.add(b2).doubleValue();

            BigDecimal b3 = new BigDecimal(Double.toString(totalPrice));
            BigDecimal b4 = new BigDecimal(samplingDetailsList.get(i).getPrice());
            totalPrice = b3.add(b4).doubleValue();

            count = count + Integer.valueOf(samplingDetailsList.get(i).getNumber());
        }
        //总重量
        weight = totalWeight;

        specsList = new ArrayList<>();
        specsNameList = new ArrayList<>();
        tempSpecs = new Specs();
        double specstemp = DoubleCountUtils.keep(weight / count);
        tempSpecs.setValue(String.valueOf(specstemp));
        Logger.d(weight);
        Logger.d(count);
        specsList.add(tempSpecs);
        specsList.addAll(LitePal.findAll(Specs.class));
        Logger.d(specsList.get(0).getValue());
        for (int i = 0; i < specsList.size(); i++) {
            specsNameList.add(specsList.get(i).getValue());
        }
        Logger.d(specsNameList.get(0));
        specsAdapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, specsNameList);
        specsAdapter.setDropDownViewResource(R.layout.item_spinner);

        //计算占比
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / totalWeight;
            specsProportion = DoubleCountUtils.keep(specsProportion);
            samplingDetailsList.get(i).setSpecsProportion(specsProportion);
        }
        LitePal.saveAll(samplingDetailsList);

        adapter = new SamplingDetailsAdapter(R.layout.item_sampling_deatils, samplingDetailsList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int id = samplingDetailsList.get(position).getId();
                if (mFragment != null) {
                    mFragment = null;
                }
                mFragment = ShowSamplingPictureFragment.newInstance(id);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//动画 淡入淡出
                mFragment.show(ft, "ShowSamplingPictureFragment");
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = samplingDetailsList.get(position).getId();
                LitePal.delete(SamplingDetails.class, id);
                samplingDetailsList.remove(position);
                /**
                 * 由于不一定是删除的最后一个，所以只能对Count重新赋值
                 */
                for (int i = 0; i < samplingDetailsList.size(); i++) {
                    samplingDetailsList.get(i).setCount(i + 1);
                    //samplingDetailsList.get(i).save();
                }
                //重新计算占比
                //采样总重量
                double total = 0d;
                double totalPrice = 0d;
                for (int i = 0; i < samplingDetailsList.size(); i++) {
                    BigDecimal b1 = new BigDecimal(Double.toString(total));
                    BigDecimal b2 = new BigDecimal(samplingDetailsList.get(i).getWeight());
                    total = b1.add(b2).doubleValue();

                    BigDecimal b3 = new BigDecimal(Double.toString(totalPrice));
                    BigDecimal b4 = new BigDecimal(samplingDetailsList.get(i).getPrice());
                    totalPrice = b3.add(b4).doubleValue();

                    count = count + Integer.valueOf(samplingDetailsList.get(i).getNumber());

                }

                //总重量
                weight = total;
                specsList.get(0).setValue(String.valueOf(DoubleCountUtils.keep(weight / count)));
                specsAdapter.notifyDataSetChanged();

                //计算占比
                for (int i = 0; i < samplingDetailsList.size(); i++) {
                    double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / total;
                    specsProportion = DoubleCountUtils.keep(specsProportion);
                    samplingDetailsList.get(i).setSpecsProportion(specsProportion);
                }
                LitePal.saveAll(samplingDetailsList);

                adapter.notifyDataSetChanged();
                mRecyclerView.closeMenu();
            }
        });
    }

    public void initView(View view) {
        //点击透明区域不可取消
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        tvTitle.setText(R.string.sampling_details);
        ivBack.setOnClickListener(this);

        tvCount = view.findViewById(R.id.tv_count);
        tvWeight = view.findViewById(R.id.tv_weight);
        spSpecs = view.findViewById(R.id.id_sp_specs);
        spSpecs.setAdapter(specsAdapter);
        tvCount.setText(String.valueOf(count));
        tvWeight.setText(String.valueOf(weight));
        tvPrice = view.findViewById(R.id.tv_price);
        btnSave = view.findViewById(R.id.button);
        btnSave.setOnClickListener(this);

        spSpecs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                specs = specsList.get(i);
                position = i;

                /**
                 * 根据supplierId，matterId ,specs 和当前品类等级  查找价格配置表   是否匹配
                 * 匹配：mEtPrice.setText(当前配置的价格)
                 * 未匹配：0
                 */

                List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                        supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);
                //比交时间
                //获取当前时间 HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date nowDate = new Date(System.currentTimeMillis());//当前时间
                List<Price> tempPriceList = new ArrayList<>();//所有满足条件的集合
                for (int j = 0; j < priceList.size(); j++) {
                    String start = priceList.get(j).getStartDate();
                    String end = priceList.get(j).getEndDate();
                    Date startDate = new Date();
                    Date endDate = new Date();
                    try {
                        startDate = dateFormat.parse(start);//开始时间
                        endDate = dateFormat.parse(end);//截止时间
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                        tempPriceList.add(priceList.get(j));
                    }
                }

                if (tempPriceList.size() > 0) {
                    Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//最新一个价格
                    tvPrice.setText(String.valueOf(lastPrice.getPrice()));
                } else {
                    myToasty.showWarning("当前未配置价格，请手动填写！");
                    tvPrice.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //specs = tempSpecs;
                position = 0;
            }
        });

        tvPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tempPrice = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        linearLayout = view.findViewById(R.id.id_ll_bottom);
        if (samplingSize == 0 || ValuationType != 1) {
            linearLayout.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
        }

        mRecyclerView = view.findViewById(R.id.id_sampling_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setCanMove(true);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_sampling_details;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setSamplingNumber(samplingDetailsList.size());
                EventBus.getDefault().post(deviceEvent);
                dismiss();
                break;

            case R.id.button:

                if (position == 0) {
                    myToasty.showWarning("请选择系统默认提供的规格！");
                    return;
                }

                if (tempPrice.length() == 0) {
                    myToasty.showWarning(getResources().getString(R.string.input_price));
                    return;
                }

                if (DoubleCountUtils.keep(Double.valueOf(tempPrice)) <= 0) {
                    myToasty.showWarning("单价不能为零！");
                    return;
                }

                SamplingBySpecs samplingBySpecs = new SamplingBySpecs();
                samplingBySpecs.setCount(count);
                samplingBySpecs.setWeiht(weight);
                samplingBySpecs.setSpecsId(specs.getId());
                samplingBySpecs.setPrice(DoubleCountUtils.keep(Double.valueOf(tempPrice)));
                samplingBySpecs.save();
                dismiss();

                break;
            default:
                break;
        }
    }
}
