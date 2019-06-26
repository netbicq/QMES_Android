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

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

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
public class SamplingDetailsFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private SlideRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SamplingDetailsAdapter adapter;
    private List<SamplingDetails> samplingDetailsList;
    private ShowSamplingPictureFragment mFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sampling_details, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {
        samplingDetailsList = new ArrayList<>();
        //samplingDetailsList = LitePal.findAll(SamplingDetails.class,true);
        samplingDetailsList = LitePal.where("hasBill < ?","0")
                                     .find(SamplingDetails.class,true);

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
                    samplingDetailsList.get(i).save();
                }
                adapter.notifyDataSetChanged();
                mRecyclerView.closeMenu();
            }
        });
    }

    private void initView(View view) {
        mBackImageView = view.findViewById(R.id.iv_sampling_back);
        mRecyclerView = view.findViewById(R.id.id_sampling_recyclerView);
        mBackImageView.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setCanMove(true);
        mRecyclerView.setAdapter(adapter);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_sampling_details);
        //点击透明区域不可取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.LEFT;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = (2 * width) / 3;
        wlp.height = (2 * height) / 3;
        //wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sampling_back:
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
