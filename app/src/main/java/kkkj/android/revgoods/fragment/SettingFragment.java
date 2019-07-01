package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.Locale;

import butterknife.ButterKnife;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.ui.DeductionCategoryActivity;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

public class SettingFragment extends DialogFragment implements View.OnClickListener {

    private TextView mSettingDeuctionCategoryTextView;
    private TextView mChangeLanguageTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {
    }

    private void initView(View view) {
        mChangeLanguageTv = view.findViewById(R.id.id_tv_change_language);
        mSettingDeuctionCategoryTextView = view.findViewById(R.id.id_tv_deduction_category);
        mSettingDeuctionCategoryTextView.setOnClickListener(this);
        mChangeLanguageTv.setOnClickListener(this);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_setting);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，高度铺满全屏，左侧。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.LEFT;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = width / 3;
        //wlp.height = (2 * height) / 3;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_deduction_category:
                startActivity(new Intent(getContext(), DeductionCategoryActivity.class));
                break;

            case R.id.id_tv_change_language:
                String[] items = new String[2];
                items[0] = "中文";
                items[1] = getResources().getString(R.string.english);
                QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(getContext());
                builder.addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeLanguage(i);
                    }
                }).show();
                break;

            default:
                break;
        }
    }

    private void changeLanguage(int language) {
        //将选择的language保存到SP中
        SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_USER_LANG,language);
        //重新启动Activity,并且要清空栈
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
