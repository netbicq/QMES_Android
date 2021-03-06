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
        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.theme, android.R.color.white);//????????????????????????
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header
            }
        });
        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //???????????????Footer???????????? BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private void initX5() {
        //???wifi????????????????????????x5??????
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.d("X5????????????" + b);
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
//        //??????????????????
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        refWatcher = LeakCanary.install(this);

        //????????????
        SkinCompatManager.withoutActivity(this)                         // ???????????????????????????
                .addInflater(new SkinMaterialViewInflater())            // material design ?????????????????????[??????]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout ?????????????????????[??????]
                .addInflater(new SkinCardViewInflater())                // CardView v7 ?????????????????????[??????]
                .setSkinStatusBarColorEnable(false)                     // ????????????????????????????????????[??????]
                .setSkinWindowBackgroundEnable(false)                   // ??????windowBackground?????????????????????[??????]
                .loadSkin();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //???????????????????????????????????????????????????
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //?????????????????????????????????

                try {
                    Thread.sleep(3000);//?????????????????????????????????????????????????????????????????????????????????
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(mContext));

                FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                        .tag("BaseApplication")
                        .build();
                //????????????
                Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

//        ???????????????????????????logger
//        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));

                Logger.d("Logger???????????????");

                initX5();

                //????????????debug?????????true????????????debug?????????false????????????????????????
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
     * ??????
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
