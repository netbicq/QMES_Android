package kkkj.android.revgoods.ui.login.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.app.BaseApplication;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.relay.bluetooth.model.BTOrder;
import kkkj.android.revgoods.ui.login.contract.LoginContract;
import kkkj.android.revgoods.ui.login.model.SignInModel;
import kkkj.android.revgoods.ui.login.presenter.LoginPresenter;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import kkkj.android.revgoods.utils.SoftHideKeyBoardUtil;


public class LoginActivity extends MvpBaseActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {
    @BindView(R.id.tv_regist)
    TextView tv_regist;
    @BindView(R.id.ed_tel)
    EditText ed_tel;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.ed_pwd)
    TextView ed_pwd;
    @BindView(R.id.btn_login)
    QMUIRoundButton btn_login;
    @BindView(R.id.ic_eys)
    ImageView ic_eys;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    boolean isChecked = false;
    //????????????
    private boolean isSuccess = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initMonitorAndData() {
        getPermission();

        action_bar_title.setText("??????");
        ivBack.setVisibility(View.GONE);

        tv_regist.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ic_eys.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    checkBox2.setChecked(false);
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    checkBox.setChecked(true);
                }
            }
        });

        String userName =  SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_NAME);
        String accountCode = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_ACCOUNTCODE);
        String password = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_PWD);
        if(!TextUtils.isEmpty(userName))
        {
            ed_tel.setText(userName);
        }

        if(!TextUtils.isEmpty(accountCode))
        {
            ed_code.setText(accountCode);
        }
        boolean SP_AUTO_LOGIN = SharedPreferenceUtil.getBoolean(SharedPreferenceUtil.SP_AUTO_LOGIN);
        if(SP_AUTO_LOGIN)
        {
            if(!TextUtils.isEmpty(password))
            {
                ed_pwd.setText(password);
                //??????3??????????????????
                Observable.timer(0, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {

                                login();
                            }
                        });


            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.tv_regist:
//                startActivity(new Intent(mContext,RegistActivity.class));
                //??????baseUrl
                setBaseUrl();

                break;
            case  R.id.btn_login:
                login();

                break;
            case R.id.ic_eys:
                isChecked = !isChecked;
                if (isChecked) {
                    //???????????? ????????????--????????????????????????
                    ed_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //????????????????????????--???????????? ???????????????????????????  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    ed_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

                default:
                    break;
        }
    }

    private void setBaseUrl() {

        final EditText editText = new EditText(LoginActivity.this);
        String baseUrl = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_BASE_URL);
        editText.setText(baseUrl);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(LoginActivity.this);
        inputDialog.setTitle("?????????????????????").setView(editText);
        inputDialog.setPositiveButton(R.string.enter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String baseUrl = editText.getText().toString().trim();
                        if (baseUrl.length() == 0){
                            showToast("?????????????????????");
                        }else {
                            SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_BASE_URL,
                                    baseUrl);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();

    }

    @Override
    public void signSuc(SignInModel.Response response) {
        BaseApplication.getInstance().setCommonParts(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_Commonparts_Token)
                ,SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_Commonparts_AccountID));
        if(response.getData()!=null)
        {
            BaseApplication.getInstance().setUserProfile(response.getData().getUserProfile());
            SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_USER_ACCOUNTCODE,ed_code.getText().toString().trim());
            SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_USER_NAME,ed_tel.getText().toString().trim());
            if(checkBox.isChecked())
            {
                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_USER_PWD,ed_pwd.getText().toString().trim());
            }
            else {
                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_USER_PWD,"");
            }
            if(checkBox2.isChecked())
            {
                SharedPreferenceUtil.setBoolean(SharedPreferenceUtil.SP_AUTO_LOGIN,true);
            }
            else {
                SharedPreferenceUtil.setBoolean(SharedPreferenceUtil.SP_AUTO_LOGIN,false);
            }
            showToast("????????????");
        }

        startActivity(new Intent(mContext, MainActivity.class));
        finish();

    }

    private void login() {
        SignInModel.Request request = new SignInModel.Request();
        if(!TextUtils.isEmpty(ed_code.getText().toString().trim()))
        {
            request.setAccountCode(ed_code.getText().toString());
        }
        else {
            showToast("?????????????????????");
            return;
        }
        if(!TextUtils.isEmpty(ed_tel.getText().toString().trim()))
        {
            request.setLogin(ed_tel.getText().toString());
        }
        else {
            showToast("??????????????????");
            return;
        }

        if(!TextUtils.isEmpty(ed_pwd.getText().toString().trim()))
        {
            request.setPwd(ed_pwd.getText().toString());
        }
        else {
            showToast("???????????????");
            return;
        }
        if (TextUtils.isEmpty(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_BASE_URL))) {
            showToast("???????????????????????????");
            return;
        }
        mPresenter.signin(request);
    }

    public void getPermission()
    {
        boolean enabled = isNotificationEnabled(mContext);

        if (!enabled) {
            /**
             * ???????????????????????????
             * @param context
             */
            Intent localIntent = new Intent();
            //?????????????????????????????????????????????
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                localIntent.putExtra("app_package", mContext.getPackageName());
                localIntent.putExtra("app_uid", mContext.getApplicationInfo().uid);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.setData(Uri.parse("package:" + mContext.getPackageName()));
            } else {
                //4.4???????????????app????????????????????????????????????Action???????????????????????????????????????,
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
                }
            }
            mContext.startActivity(localIntent);
        }
    }

    /**
     * ??????????????????
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG,0)));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


}
