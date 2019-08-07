package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kkkj.android.revgoods.R;


public abstract class BaseFullScreenDialogFragment extends DialogFragment {
    public RelativeLayout barContainer;
    public ImageView ivBack;
    public TextView tvTitle;
    public ImageView ivRight;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(), container, false);

        barContainer = view.findViewById(R.id.bar_container);
        ivBack = barContainer.findViewById(R.id.iv_back);
        tvTitle = barContainer.findViewById(R.id.tv_title);
        ivRight = barContainer.findViewById(R.id.iv_right);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        initData();
        initView(view);
        return view;

    }
    public abstract void initData();
    public abstract void initView(View view);
    public abstract int setLayout();

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();

        //设置背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = width;
        //wlp.height = height;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

    }

}
