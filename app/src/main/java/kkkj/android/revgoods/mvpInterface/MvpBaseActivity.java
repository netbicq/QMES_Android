package kkkj.android.revgoods.mvpInterface;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.app.BaseApplication;
import kkkj.android.revgoods.customer.MyToasty;

/**
 * MVP activity基类
 */
public abstract class MvpBaseActivity<T extends MvpPresenter> extends AppCompatActivity implements MvpView {

    MyToasty mToast;
    public QMUITipDialog tLoading;
    public RxPermissions rxPermissions = new RxPermissions(this);
    private Unbinder unbinder;
    public T mPresenter;
    public FragmentActivity mActivity;
    public Context mContext;

    public TextView action_bar_title;
    public TextView action_bar_right;
    public ImageView ivBack;

    @Override
    public void startActivity(Intent intent) {
        //把默认启动模式改为singleTop
//        if (intent.getFlags() != Intent.FLAG_ACTIVITY_CLEAR_TASK) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        }
        super.startActivity(intent);
    }

    /**
     * 界面设置状态栏字体颜色
     */

    private void setAndroidNativeLightStatusBar(boolean dark) {
        View decor = mActivity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//继承AppCompatActivity使用
        setContentView(getLayout());
        /**
         * 强制竖屏
         */
        //if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // }
        /*
         *透明状态栏
         */

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mActivity = this;
        setAndroidNativeLightStatusBar(true);
        mContext = this;
        rxPermissions = new RxPermissions(mActivity);
        action_bar_title = findViewById(R.id.tv_title);
       // action_bar_right = findViewById(R.id.action_bar_right);
        ivBack = findViewById(R.id.iv_back);
        /*
         *初始化自定义toast
         **/
        unbinder = ButterKnife.bind(this);
        mToast = new MyToasty(mContext);
        tLoading= new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在登录...")
                .create();

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        BaseApplication.getInstance().addActivity(mActivity);
        initMonitorAndData();
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tLoading.isShowing()) {
            tLoading.dismiss();
        }

        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null)
            mPresenter.detachView();
        BaseApplication.getInstance().removeActivity(mActivity);
    }

    protected abstract int getLayout();

    protected abstract T getPresenter();

    protected abstract void initMonitorAndData();

    @Override
    public void showLoading() {
        if(tLoading==null)
            return;
        if (!tLoading.isShowing()) {
            tLoading.show();
        }
    }

    public void showLoading(String msg) {
        if(tLoading==null)
            return;
        if (!tLoading.isShowing()) {
            tLoading= new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
            tLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        if(tLoading==null)
            return;
        if (tLoading.isShowing()) {
            tLoading.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        if (tLoading.isShowing()) {
            tLoading.dismiss();
        }
        mToast.showInfo(msg);
    }

    @Override
    public void showErr(String msg) {
        if (tLoading.isShowing()) {
            tLoading.dismiss();
        }
        mToast.showError(msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onComplete() {
        hideLoading();
    }

    @Override
    public void goLogin() {

    }

    public void goBack(View view) {
        finish();
    }
}
