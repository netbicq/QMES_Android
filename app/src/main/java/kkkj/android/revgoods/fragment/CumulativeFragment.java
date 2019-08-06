package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.CumulativeAdapter;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;

/**
 * 累计
 */
public class CumulativeFragment extends BaseDialogFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CumulativeAdapter adapter;
    private List<Cumulative> cumulativeList;
    private List<Deduction> deductionList;


    @Override
    public void initData() {
        cumulativeList = new ArrayList<>();
        deductionList = new ArrayList<>();
        deductionList = LitePal.where("hasBill < ?","0")
                                               .find(Deduction.class);

        for (int i = 0;i<deductionList.size();i++) {
            Cumulative cumulative = new Cumulative();
            cumulative.setCount(i + 1);
            cumulative.setCategory("扣重·" + deductionList.get(i).getCategory());
            cumulative.setWeight(deductionList.get(i).getWeight());
            cumulative.setPrice(deductionList.get(i).getPrice());
            cumulativeList.add(cumulative);
        }

        cumulativeList.addAll(LitePal.where("hasBill < ?","0")
                                     .find(Cumulative.class));

        adapter = new CumulativeAdapter(R.layout.item_cumulative,cumulativeList);
    }

    @Override
    public void initView(View view) {
        tvTitle.setText(R.string.cumulative_details);

        mRecyclerView = view.findViewById(R.id.id_sampling_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_cumulative;
    }

    @Override
    public void onClick(View view) {

    }
}
