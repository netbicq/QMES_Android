package kkkj.android.revgoods.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import kkkj.android.revgoods.app.BaseApplication;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.ui.login.model.SignInModel;
import kkkj.android.revgoods.ui.login.view.LoginActivity;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

import static kkkj.android.revgoods.utils.NetUtils.checkNetWork;

public  abstract class BaseActivity <T extends MvpPresenter> extends AppCompatActivity implements MvpView {

    public T mPresenter;
    public FragmentActivity mActivity;
    public Context mContext;
    public MyToasty myToasty;


    @Override
    public void startActivity(Intent intent) {
        //把默认启动模式改为singleTop
        if (intent.getFlags() != Intent.FLAG_ACTIVITY_CLEAR_TASK) {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        super.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        myToasty = new MyToasty(this);
        mContext = this;
        mActivity = this;
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        ButterKnife.bind(this);

        BaseApplication.getInstance().addActivity(mActivity);

        initData();
        initView();

    }

    protected abstract T getPresenter();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int setLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        BaseApplication.getInstance().removeActivity(mActivity);
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


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showErr(String msg) {

    }

    @Override
    public void goLogin() {
        if(mContext==null)
            return;
        Logger.d("重新登录");
        SignInModel.Request request = new SignInModel.Request();
        String AccountCode = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_ACCOUNTCODE);
        String Login = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_NAME);
        String Pwd = SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_USER_PWD);

        request.setLogin(Login);
        request.setPwd(Pwd);
        request.setAccountCode(AccountCode);

        if (!TextUtils.isEmpty(request.getPwd())) {
            if (!checkNetWork()) {
                showErr("请检查网络");
                return;
            }
            //显示正在加载进度条
            myToasty.showInfo("正在为您重新登录");
            // 调用Model请求数据
            SignInModel loginModel = new SignInModel();
            loginModel.getResponse(request, new MvpCallback<SignInModel.Response>(this) {
                @Override
                public void onSuccess(SignInModel.Response data) {
                    showToast("登录成功,请重新发起请求");
                    BaseApplication.getInstance().setUserProfile(data.getData().getUserProfile());
                    if(data.getData().getUserInfo()!=null)
                    {
                        if(!TextUtils.isEmpty(data.getData().getAccountID()))
                        {
                            BaseApplication.getInstance().setCommonParts(data.getData().getUserInfo().getToken(),data.getData().getAccountID());
                        }
                    }
                }
            });
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onComplete() {

    }


}
