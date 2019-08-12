package kkkj.android.revgoods.ui.login.view;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.app.BaseApplication;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
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
    //登陆成功
    private boolean isSuccess = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initMonitorAndData() {
        getPermission();

        action_bar_title.setText("登录");
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
                btn_login.callOnClick();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.tv_regist:
//                startActivity(new Intent(mContext,RegistActivity.class));
                break;
            case  R.id.btn_login:
                SignInModel.Request request = new SignInModel.Request();
                if(!TextUtils.isEmpty(ed_code.getText().toString().trim()))
                {
                    request.setAccountCode(ed_code.getText().toString());
                }
                else {
                    showToast("请输入账套代码");
                    return;
                }
                if(!TextUtils.isEmpty(ed_tel.getText().toString().trim()))
                {
                    request.setLogin(ed_tel.getText().toString());
                }
                else {
                    showToast("请输入用户名");
                    return;
                }

                if(!TextUtils.isEmpty(ed_pwd.getText().toString().trim()))
                {
                    request.setPwd(ed_pwd.getText().toString());
                }
                else {
                    showToast("请输入密码");
                    return;
                }
                mPresenter.signin(request);

                break;
            case R.id.ic_eys:
                isChecked = !isChecked;
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    ed_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    ed_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
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
            showToast("登录成功");
        }

        startActivity(new Intent(mContext, MainActivity.class));
        finish();

    }

    public void getPermission()
    {
        boolean enabled = isNotificationEnabled(mContext);

        if (!enabled) {
            /**
             * 跳到通知栏设置界面
             * @param context
             */
            Intent localIntent = new Intent();
            //直接跳转到应用通知设置的代码：
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                localIntent.putExtra("app_package", mContext.getPackageName());
                localIntent.putExtra("app_uid", mContext.getApplicationInfo().uid);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.setData(Uri.parse("package:" + mContext.getPackageName()));
            } else {
                //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
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
     * 获取通知权限
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
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG)));
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
