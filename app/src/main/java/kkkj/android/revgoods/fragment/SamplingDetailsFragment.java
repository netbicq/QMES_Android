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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * ????????????
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
    private TextView tvUnit;
    private Spinner spSpecs;
    private EditText tvPrice;
    private Button btnSave;
    private ImageView mImageView;
    private View mView;

    private String tempPrice = "";

    private int count = 0;
    private double weight = 0d;

    private Matter matter;
    private Supplier supplier;
    private MatterLevel matterLevel;
    /**
     * ????????????
     * ValuationType = 1;??????????????????
     * ValuationType = 2;????????????????????????
     */
    private int ValuationType;

    //???????????????
    private double price;
    private int samplingSize = 0;

    private List<Specs> specsList;
    private List<String> specsNameList;
    private ArrayAdapter specsAdapter;
    private Specs specs;
    private Specs tempSpecs;

    private int position = 0;
    private double specstemp;

    @Override
    public int setLayout() {
        return R.layout.fragment_sampling_details;
    }

    @Override
    public void initData() {

        Logger.d("start" + LitePal.where("hasBill < ?", "0").find(SamplingBySpecs.class).size());

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
        //???????????????
        double totalWeight = 0d;
        //???????????????
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
        //?????????
        weight = totalWeight;

        specsList = new ArrayList<>();
        specsNameList = new ArrayList<>();
        tempSpecs = new Specs();

        if (count != 0) {

            specstemp = DoubleCountUtils.keep(weight / count);

            tempSpecs.setValue(String.valueOf(specstemp));

            specsList.add(tempSpecs);
            specsList.addAll(LitePal.findAll(Specs.class));
            specsNameList.add(String.valueOf(specstemp));
            for (int i = 1; i < specsList.size(); i++) {
                specsNameList.add(specsList.get(i).getValue());
            }
        }
        specsAdapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, specsNameList);
        specsAdapter.setDropDownViewResource(R.layout.item_spinner);

        //????????????
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / totalWeight;
            specsProportion = DoubleCountUtils.keep4(specsProportion);
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
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//?????? ????????????
                mFragment.show(ft, "ShowSamplingPictureFragment");
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.image_view:

                        if (!samplingDetailsList.get(position).isUsed()) {

                            for (SamplingDetails samplingDetails:samplingDetailsList) {
                                samplingDetails.setUsed(false);
                            }
                            samplingDetailsList.get(position).setUsed(true);
                            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_box_unchecked_24dp));
                            adapter.notifyDataSetChanged();
                        }

                        break;

                    case R.id.tv_delete:

                        int id = samplingDetailsList.get(position).getId();
                        LitePal.delete(SamplingDetails.class, id);
                        samplingDetailsList.remove(position);
                        /**
                         * ?????????????????????????????????????????????????????????Count????????????
                         */
                        for (int i = 0; i < samplingDetailsList.size(); i++) {
                            samplingDetailsList.get(i).setCount(i + 1);
                            //samplingDetailsList.get(i).save();
                        }
                        //??????????????????
                        //???????????????
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

                        //?????????
                        weight = total;
                        specsList.get(0).setValue(String.valueOf(DoubleCountUtils.keep(weight / count)));
                        specsAdapter.notifyDataSetChanged();

                        //????????????
                        for (int i = 0; i < samplingDetailsList.size(); i++) {
                            double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / total;
                            specsProportion = DoubleCountUtils.keep4(specsProportion);
                            samplingDetailsList.get(i).setSpecsProportion(specsProportion);
                        }
                        LitePal.saveAll(samplingDetailsList);

                        adapter.notifyDataSetChanged();
                        mRecyclerView.closeMenu();

                        break;
                }

            }
        });
    }

    public void initView(View view) {
        //??????????????????????????????
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        tvTitle.setText(R.string.sampling_details);
        ivBack.setOnClickListener(this);

        tvCount = view.findViewById(R.id.tv_count);
        tvWeight = view.findViewById(R.id.tv_weight);

        mImageView = view.findViewById(R.id.id_iv);
        mImageView.setOnClickListener(this);
        for (SamplingDetails samplingDetails : samplingDetailsList) {

            if (samplingDetails.isUsed()) {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_box_unchecked_24dp));
            }
        }

        tvUnit = view.findViewById(R.id.tv_unit);
        int samplingUnit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);
        switch (samplingUnit) {
            case 1://kg
                tvUnit.setText("??????(kg)");
                break;

            case 2://g
                tvUnit.setText("??????(g)");
                break;
            default:
                break;
        }

        spSpecs = view.findViewById(R.id.id_sp_specs);
        spSpecs.setAdapter(specsAdapter);
        tvCount.setText(String.valueOf(count));
        tvWeight.setText(String.valueOf(weight));
        tvPrice = view.findViewById(R.id.tv_price);
        btnSave = view.findViewById(R.id.button);
        btnSave.setOnClickListener(this);
        mView = view.findViewById(R.id.id_view);


        spSpecs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                specs = specsList.get(i);
                position = i;

                /**
                 * ??????supplierId???matterId ,specs ?????????????????????  ?????????????????????   ????????????
                 * ?????????mEtPrice.setText(?????????????????????)
                 * ????????????0
                 */

                List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                        supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);
                //????????????
                //?????????????????? HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date nowDate = new Date(System.currentTimeMillis());//????????????
                List<Price> tempPriceList = new ArrayList<>();//???????????????????????????
                for (int j = 0; j < priceList.size(); j++) {
                    String start = priceList.get(j).getStartDate();
                    String end = priceList.get(j).getEndDate();
                    Date startDate = new Date();
                    Date endDate = new Date();
                    try {
                        startDate = dateFormat.parse(start);//????????????
                        endDate = dateFormat.parse(end);//????????????
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                        tempPriceList.add(priceList.get(j));
                    }
                }

                if (tempPriceList.size() > 0) {
                    Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//??????????????????
                    tvPrice.setText(String.valueOf(lastPrice.getPrice()));
                } else {
                    myToasty.showWarning("??????????????????????????????????????????");
                    tvPrice.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //specs = tempSpecs;
                position = 0;
            }
        });

        int postion = 0;

        int unit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);

        for (int i=1;i<specsList.size();i++) {
            Specs specs1 = specsList.get(i);
            /**
             * unit = 1:??????????????????
             * unit = 2:???????????????
             */
            if (unit == 1) {
                //????????????????????????kg,??????????????????????????????g?????????????????????kg,???????????????????????????
                if (specs1.getUnit().equals("g")) {
                    //???g??????????????????
                    if (specs1.getMinWeight() * 0.001 < specstemp && specstemp < specs1.getMaxWeight() * 0.001) {
                        postion = i;
                    }
                }else {
                    //???kg???????????????????????????????????????????????????
                    if (specs1.getMinWeight() < specstemp && specstemp < specs1.getMaxWeight()) {
                        postion = i;
                    }
                }
            } else {
                //????????????????????????g,??????????????????????????????kg?????????????????????g,???????????????????????????
                if (specs1.getUnit().equals("kg")) {
                    //???kg??????????????????
                    if (specs1.getMinWeight() * 1000 < specstemp && specstemp < specs1.getMaxWeight() * 1000) {
                        postion = i;
                    }
                }else {
                    //???g???????????????????????????????????????????????????
                    if (specs1.getMinWeight() < specstemp && specstemp < specs1.getMaxWeight()) {
                        postion = i;
                    }
                }
            }

        }
        //??????????????????????????????
        if (postion != 0) {
            spSpecs.setSelection(postion,true);
        }else {
            if (samplingDetailsList.size() != 0) {
                myToasty.showWarning("???????????????????????????????????????????????????????????????????????????????????????");
            }

        }

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
            mView.setVisibility(View.GONE);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:

                dismiss();
                break;

            case R.id.id_iv:

                for (SamplingDetails samplingDetails : samplingDetailsList) {
                    samplingDetails.setUsed(false);
                    adapter.notifyDataSetChanged();
                }
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_box_checked_24dp));
                break;

            case R.id.button:

                if (position == 0) {
                    myToasty.showWarning("???????????????????????????????????????");
                    return;
                }

                if (tempPrice.length() == 0) {
                    myToasty.showWarning(getResources().getString(R.string.input_price));
                    return;
                }

                if (DoubleCountUtils.keep(Double.valueOf(tempPrice)) <= 0) {
                    myToasty.showWarning("?????????????????????");
                    return;
                }

                LitePal.deleteAll(SamplingBySpecs.class,"hasBill < ?", "0");


                SamplingDetails mSamplingDetails = null;
                for (SamplingDetails samplingDetails : samplingDetailsList) {
                    samplingDetails.save();

                    if (samplingDetails.isUsed()) { //??????????????????
                        mSamplingDetails = samplingDetails;
                    }
                }

                if (mSamplingDetails != null) {

                    SamplingBySpecs samplingBySpecs = new SamplingBySpecs();
                    samplingBySpecs.setCount(mSamplingDetails.getCount());
                    samplingBySpecs.setWeiht(Double.valueOf(mSamplingDetails.getWeight()));
                    samplingBySpecs.setSpecsId(mSamplingDetails.getSpecsId());
                    samplingBySpecs.setPrice(mSamplingDetails.getPrice());
                    samplingBySpecs.saveOrUpdate("hasBill < ?", "0");
                    Logger.d("end1???" + LitePal.where("hasBill < ?", "0").find(SamplingBySpecs.class).size());

                }else {

                    SamplingBySpecs samplingBySpecs = new SamplingBySpecs();
                    samplingBySpecs.setCount(count);
                    samplingBySpecs.setWeiht(weight);
                    samplingBySpecs.setSpecsId(specs.getId());
                    samplingBySpecs.setPrice(DoubleCountUtils.keep(Double.valueOf(tempPrice)));
                    samplingBySpecs.saveOrUpdate("hasBill < ?", "0");
                    Logger.d("end2???" + LitePal.where("hasBill < ?", "0").find(SamplingBySpecs.class).size());

                }

                dismiss();

                break;
            default:
                break;
        }
    }
}
