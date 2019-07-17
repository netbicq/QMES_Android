package kkkj.android.revgoods.app;

import android.app.Activity;
import android.content.Context;

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


public class BaseApplication extends ZApplication {
    private static BaseApplication instance;
    private Set<Activity> allActivities;
    private static Context mContext;
    private Map<String, String> commonparts;

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
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(mContext));
        LitePal.initialize(this);

        instance = this;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("BaseApplication")
                .build();
        //打印日志
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
//                .tag("NDRestructure")
//                .build()
//        保存日志到本地文件logger
//        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
//        Bugly.init(getApplicationContext(), "3dee6816b9", false);
        Logger.d("Logger初始化成功");
        //是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
        Bugly.init(mContext, "76506509d0", false);
        initX5();
    }
    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public Map<String, String> getCommonParts() {
        if(commonparts==null)
        {
            commonparts = new HashMap<String, String>();
        }
        return commonparts;
    }

    public void setCommonParts(String token, String accountID) {
        if(commonparts==null)
        {
            commonparts = new HashMap<>();
        }
        commonparts.put("Token", token);
        commonparts.put("AccountID",accountID);
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
