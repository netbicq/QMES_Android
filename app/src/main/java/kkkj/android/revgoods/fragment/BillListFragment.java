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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillAdapter;
import kkkj.android.revgoods.adapter.DeviceAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.customer.SlideRecyclerView;

/**
 * 单据列表
 */
public class BillListFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private TextView mTitle;
    private SlideRecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private BillAdapter billAdapter;
    private List<Bill> mBills;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {
        mBills = new ArrayList<>();
        mBills = LitePal.findAll(Bill.class,true);

    }


    private void initView(View view) {
        mBackImageView = view.findViewById(R.id.iv_back);
        mRecyclerView = view.findViewById(R.id.id_device_recyclerView);
        mTitle = view.findViewById(R.id.id_tv_title);
        mTitle.setText("单据列表");
        mBackImageView.setOnClickListener(this);

        billAdapter = new BillAdapter(R.layout.item_device_list,mBills);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(billAdapter);

        billAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


            }
        });

        billAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = mBills.get(position).getId();
                LitePal.delete(Bill.class,id);
                mBills.remove(position);
                billAdapter.notifyDataSetChanged();
                mRecyclerView.closeMenu();

                Logger.d("Bill-------" + LitePal.findAll(SamplingDetails.class).size());
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_device_list);
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
            case R.id.iv_back:
                dismiss();
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
