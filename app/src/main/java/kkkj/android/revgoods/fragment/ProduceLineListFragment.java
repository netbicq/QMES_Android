package kkkj.android.revgoods.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
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

    @Override
    public void initData() {
        produceLineList = new ArrayList<>();
        ProduceLine produceLine = new ProduceLine();
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
                dismiss();
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_device_list;
    }
}
