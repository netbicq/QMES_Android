package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import org.greenrobot.eventbus.EventBus;

import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.ui.addDeductionCategory.DeductionCategoryActivity;
import kkkj.android.revgoods.ui.login.view.LoginActivity;
import kkkj.android.revgoods.ui.saveBill.SaveBillDetailsActivity;
import kkkj.android.revgoods.utils.AppVersionInfoUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import skin.support.SkinCompatManager;

public class SettingFragment extends DialogFragment implements View.OnClickListener {

    private TextView mSettingDeductionCategoryTextView;
    private TextView mChangeLanguageTv;
    private TextView mTvDeviceList;
    private TextView mTvChangeUser;
    private TextView mTvVersionName;
    private TextView mTvChangeTheme;

    private String versionName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        //??????????????????
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }

    private void initData() {

        versionName = AppVersionInfoUtils.getAppVersionName(getActivity());
    }

    private void initView(View view) {
        mChangeLanguageTv = view.findViewById(R.id.id_tv_change_language);
        mSettingDeductionCategoryTextView = view.findViewById(R.id.id_tv_deduction_category);
        mTvDeviceList = view.findViewById(R.id.tv_device_list);

        mTvVersionName = view.findViewById(R.id.tv_version_name);
        mTvVersionName.setText(getResources().getString(R.string.versionCode) + versionName);

        mTvChangeUser = view.findViewById(R.id.tv_change_user);
        mTvChangeUser.setOnClickListener(this);
        mSettingDeductionCategoryTextView.setOnClickListener(this);
        mChangeLanguageTv.setOnClickListener(this);
        mTvDeviceList.setOnClickListener(this);
        mTvChangeTheme = view.findViewById(R.id.tv_change_theme);
        mTvChangeTheme.setOnClickListener(this);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_setting);
        dialog.setCanceledOnTouchOutside(true);

        // ????????????????????????????????????????????????????????????
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
                items[0] = "??????";
                items[1] = "English";
                QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(getContext());
                builder.addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeLanguage(i);
                    }
                }).show();
                break;

            case R.id.tv_device_list://????????????

                //?????????????????????????????????fragment
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                //?????????????????????
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                Fragment fragment = getChildFragmentManager().findFragmentByTag("DelayTimeFragment");
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                new DelayTimeFragment().show(ft, "DelayTimeFragment");


//                //????????????,??????2???
//                int intervalTime = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_INTERVAL_TIME,2);
//
//                final EditText editText = new EditText(getActivity());
//                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//                editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
//
//                editText.setText(String.valueOf(intervalTime));
//
//                AlertDialog.Builder inputDialog1 = new AlertDialog.Builder(getActivity());
//                inputDialog1.setTitle("?????????????????????").setView(editText);
//                inputDialog1.setPositiveButton(R.string.enter,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String time = editText.getText().toString().trim();
//                                MyToasty myToasty = new MyToasty(getActivity());
//                                if (time.length() == 0) {
//                                    myToasty.showWarning("?????????????????????");
//                                    return;
//                                }
//                                if (Double.valueOf(time) <= 0) {
//                                    myToasty.showWarning("?????????????????????0");
//                                    return;
//                                }
//
//                                SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_INTERVAL_TIME,Integer.valueOf(time));
//                                DeviceEvent deviceEvent = new DeviceEvent();
//                                deviceEvent.setIntervalTime(Integer.valueOf(time));
//                                EventBus.getDefault().post(deviceEvent);
//                                dismiss();
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        }).show();
                break;

            case R.id.tv_change_theme://????????????

                String[] item = new String[2];
                item[0] = "????????????";
                item[1] = "????????????";
                QMUIDialog.MenuDialogBuilder qBuilder = new QMUIDialog.MenuDialogBuilder(getContext());
                qBuilder.addItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                SkinCompatManager.getInstance().loadSkin("day", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // ????????????
                                break;

                            case 1:
                                SkinCompatManager.getInstance().restoreDefaultTheme();

                                break;
                            default:
                                break;
                        }
                        dialogInterface.dismiss();
                        dismiss();
                    }
                }).show();

                break;

            case R.id.tv_change_user:
                SharedPreferenceUtil.setBoolean(SharedPreferenceUtil.SP_AUTO_LOGIN, false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            default:
                break;
        }
    }

    private void changeLanguage(int language) {
        //????????????language?????????SP???
        SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_USER_LANG, language);
        //????????????Activity,??????????????????
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
