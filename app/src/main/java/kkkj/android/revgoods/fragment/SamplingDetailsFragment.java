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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.SamplingDetailsAdapter;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.customer.SlideRecyclerView;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 采样明细
 */
public class SamplingDetailsFragment extends BaseDialogFragment implements View.OnClickListener {

    private SlideRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SamplingDetailsAdapter adapter;
    private List<SamplingDetails> samplingDetailsList;
    private ShowSamplingPictureFragment mFragment;


    public void initData() {
        samplingDetailsList = new ArrayList<>();
        samplingDetailsList = LitePal.where("hasBill < ?","0")
                                     .find(SamplingDetails.class,true);
        for (int i=0;i<samplingDetailsList.size();i++) {
            Logger.d(samplingDetailsList.get(i).getSpecs().toString());
        }


        //采样总重量
        double total = 0d;
        for (int i = 0;i<samplingDetailsList.size();i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(total));
            BigDecimal b2 = new BigDecimal(samplingDetailsList.get(i).getWeight());
            total = b1.add(b2).doubleValue();
        }

        //计算占比
        for (int i = 0;i<samplingDetailsList.size();i++) {
            double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / total;
            samplingDetailsList.get(i).setSpecsProportion(specsProportion);
        }
        LitePal.saveAll(samplingDetailsList);

        adapter = new SamplingDetailsAdapter(R.layout.item_sampling_deatils,samplingDetailsList);
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
                mFragment.show(ft,"ShowSamplingPictureFragment");
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = samplingDetailsList.get(position).getId();
                LitePal.delete(SamplingDetails.class,id);
                samplingDetailsList.remove(position);
                /**
                 * 由于不一定是删除的最后一个，所以只能对Count重新赋值
                 */
                for (int i=0;i<samplingDetailsList.size();i++) {
                    samplingDetailsList.get(i).setCount(i+1);
                    //samplingDetailsList.get(i).save();
                }
                //重新计算占比
                //采样总重量
                double total = 0d;
                for (int i = 0;i<samplingDetailsList.size();i++) {
                    BigDecimal b1 = new BigDecimal(Double.toString(total));
                    BigDecimal b2 = new BigDecimal(samplingDetailsList.get(i).getWeight());
                    total = b1.add(b2).doubleValue();
                }

                //计算占比
                for (int i = 0;i<samplingDetailsList.size();i++) {
                    double specsProportion = Double.parseDouble(samplingDetailsList.get(i).getWeight()) / total;
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

            default:
                break;
        }
    }
}
