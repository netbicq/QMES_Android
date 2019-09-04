package kkkj.android.revgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.ProduceLineAdapter;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.fragment
 * Author: Admin
 * Time: 2019/8/22 9:40
 * Describe: 生产线列表
 */
public class ProduceLineListFragment extends BaseDialogFragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProduceLineAdapter adapter;
    private List<ProduceLine> produceLineList;
    private SetProduceLineFragment mFragment;
    private ProduceLine produceLine;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_device_list;
    }

    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Refresh(DeviceEvent deviceEvent) {
        if (deviceEvent.isRefresh()) {
            produceLineList.clear();

            produceLineList.add(produceLine);
            produceLineList.addAll(LitePal.findAll(ProduceLine.class));
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void initData() {
        produceLineList = new ArrayList<>();
        produceLine = new ProduceLine();
        produceLine.setName("移动称重");
        produceLineList.add(produceLine);
        produceLineList.addAll(LitePal.findAll(ProduceLine.class));
    }

    @Override
    public void initView(View view) {
        tvTitle.setText(R.string.choose_produce_line);

        mRecyclerView = view.findViewById(R.id.id_device_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new ProduceLineAdapter(R.layout.item_produce_line_list,produceLineList);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceEvent deviceEvent = new DeviceEvent();
                deviceEvent.setProduceLine(produceLineList.get(position));
                EventBus.getDefault().post(deviceEvent);
                Logger.d(produceLineList.get(position).isMasterBaned());
                dismiss();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = produceLineList.get(position).getId();
                if (mFragment != null) {
                    mFragment = null;
                }
                mFragment = SetProduceLineFragment.newInstance(id);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//动画 淡入淡出
                mFragment.show(ft, "ShowSamplingPictureFragment");

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
