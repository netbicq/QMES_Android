package kkkj.android.revgoods.app;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import com.tencent.bugly.Bugly;
import com.tencent.smtt.sdk.QbSdk;
import com.uuzuche.lib_zxing.ZApplication;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.bean.Auth_UserProfile;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


public class BaseApplication extends ZApplication {
    private static BaseApplication instance;
    private Set<Activity> allActivities;
    private static Context mContext;
    private Map<String, String> commonparts;

    //private RefWatcher refWatcher;

    public Auth_UserProfile getUserProfile() {
        Auth_UserProfile userProfile = new Auth_UserProfile();
        userProfile.setLogin(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_UserProfile_Login));
        userProfile.setCNName(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_UserProfile_CNName));
        userProfile.setTel(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_UserProfile_Tel));
        userProfile.setID(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_UserProfile_ID));
        userProfile.setHeadIMG(SharedPreferenceUtil.getString(SharedPreferenceUtil.SP_UserProfile_HeadIMG));
        return userProfile;
    }

    public void setUserProfile(Auth_UserProfile userProfile) {
        SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_UserProfile_Login, userProfile.getLogin());
        SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_UserProfile_CNName, userProfile.getCNName());
        SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_UserProfile_Tel, userProfile.getTel());
        SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_UserProfile_ID, userProfile.getID());
        SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_UserProfile_HeadIMG, userProfile.getHeadIMG());
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.theme, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private void initX5() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.d("X5内核加载" + b);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        LitePal.initialize(getApplicationContext());
//        //内存泄露工具
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        refWatcher = LeakCanary.install(this);

        //一键换肤
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //子线程初始化第三方组件

                try {
                    Thread.sleep(3000);//建议延迟初始化，可以发现是否影响其它功能，或者是崩溃！
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(mContext));

                FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                        .tag("BaseApplication")
                        .build();
                //打印日志
                Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

//        保存日志到本地文件logger
//        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));

                Logger.d("Logger初始化成功");

                initX5();

                //是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
                Bugly.init(mContext, "76506509d0", false);


            }
        }).start();

        instance = this;

    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public Map<String, String> getCommonParts() {
        if (commonparts == null) {
            commonparts = new HashMap<String, String>();
        }
        return commonparts;
    }

    public void setCommonParts(String token, String accountID) {
        if (commonparts == null) {
            commonparts = new HashMap<>();
        }
        commonparts.put("Token", token);
        commonparts.put("AccountID", accountID);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public Context getAppContext() {
        return this;
    }

    /**
     * 退出
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
