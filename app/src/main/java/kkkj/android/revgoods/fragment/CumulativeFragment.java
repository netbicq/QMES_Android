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
import kkkj.android.revgoods.adapter.DeductionAdapter;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;

/**
 * 累计
 */
public class CumulativeFragment extends BaseDialogFragment implements View.OnClickListener {

    private CumulativeAdapter cumulativeAdapter;
    private DeductionAdapter deductionAdapter;

    /**
     * type = 1 : 扣重明细
     * type = 2 : 计重明细
     */
    private int type = 0;

    public static CumulativeFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type",type);

        CumulativeFragment fragment = new CumulativeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (Integer) getArguments().getSerializable("type");
    }

    @Override
    public int setLayout() {
        if (type == 1) {
            return R.layout.fragment_deduction_details;
        } else {
            return R.layout.fragment_cumulative;
        }

    }

    @Override
    public void initData() {
        List<Cumulative> cumulativeList = new ArrayList<>();
        List<Deduction> deductionList = new ArrayList<>();

        if (type == 1) {

            deductionList =LitePal.where("hasBill < ?","0")
                    .find(Deduction.class);
            deductionAdapter = new DeductionAdapter(R.layout.item_deduction_details,deductionList);

        }else {

            cumulativeList = LitePal.where("hasBill < ?","0")
                    .find(Cumulative.class);
            cumulativeAdapter = new CumulativeAdapter(R.layout.item_cumulative,cumulativeList);

        }


    }

    @Override
    public void initView(View view) {
        if (type == 1) {
            tvTitle.setText("扣重记录");
        }else if (type == 2) {
            tvTitle.setText("计重记录");
        }


        RecyclerView mRecyclerView = view.findViewById(R.id.id_sampling_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        if (type == 1) {
            mRecyclerView.setAdapter(deductionAdapter);
        }else {
            mRecyclerView.setAdapter(cumulativeAdapter);
        }

    }


    @Override
    public void onClick(View view) {

    }
}
