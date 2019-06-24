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
public class CumulativeFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CumulativeAdapter adapter;
    private List<Cumulative> cumulativeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cumulative, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {
        cumulativeList = new ArrayList<>();
        List<Deduction> deductionList = LitePal.findAll(Deduction.class,true);
        for (int i = 0;i<deductionList.size();i++) {
            Cumulative cumulative = new Cumulative();
            cumulative.setCount(i + 1);
            cumulative.setCategory(deductionList.get(i).getCategory().getCategory());
            cumulative.setWeight(deductionList.get(i).getWeight());
            cumulativeList.add(cumulative);
        }

        cumulativeList.addAll(LitePal.findAll(Cumulative.class));

        adapter = new CumulativeAdapter(R.layout.item_cumulative,cumulativeList);
    }

    private void initView(View view) {
        mBackImageView = view.findViewById(R.id.iv_sampling_back);
        mRecyclerView = view.findViewById(R.id.id_sampling_recyclerView);
        mBackImageView.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_cumulative);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = (2 * width) / 3;
        wlp.height = (2 * height) / 3;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sampling_back:
                dismiss();
                break;

            default:
                break;
        }
    }
}
