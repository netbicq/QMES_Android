package kkkj.android.revgoods.utils;

import android.content.Context;
import android.content.SharedPreferences;

import kkkj.android.revgoods.app.BaseApplication;


public class SharedPreferenceUtil {
    private static final String SHAREDPREFERENCES_NAME = "NDRestructure_sp";
    public static final String SP_USER_LANG = "SP_USER_LANG";
    public static final String SP_ISFIRST = "isFirst";
    public static final String SP_USER_NAME = "SP_USER_NAME";
    public static final String SP_USER_PWD = "SP_USER_PWD";
    public static final String SP_USER_ACCOUNTCODE = "SP_USER_ACCOUNTCODE";

    public static final String SP_AUTO_LOGIN = "SP_AUTO_LOGIN";

    public static final String SP_INTERVAL_TIME = "Interval_Time";
    public static final String SP_MATTER = "Matter";
    public static final String SP_MATTER_LEVEL = "Matter_Level";

    /**
     * 采样单位
     * 1. 系统默认单位kg
     * 2. g
     */
    public static final String SP_SAMPLING_UNIT = "Sampling_Unit";

    public static final String SP_REMEBER_PWD = "SP_REMEBER_PWD";

    public static final String SP_APK_URL = "SP_APK_URL";

    /**
     * UserProfile
     */
    public static final String SP_UserProfile_Login = "SP_UserProfile_Login";
    public static final String SP_UserProfile_CNName = "SP_UserProfile_CNName";
    public static final String SP_UserProfile_Tel = "SP_UserProfile_Tel";
    public static final String SP_UserProfile_ID = "SP_UserProfile_ID";
    public static final String SP_UserProfile_HeadIMG = "SP_UserProfile_HeadIMG";

    /**
     * Commonparts
     */
    public static final String SP_Commonparts_Token = "SP_Commonparts_Token";
    public static final String SP_Commonparts_AccountID = "SP_Commonparts_AccountID";

    public static final String SP_PIECE_WEIGHT = "SP_PIECE_WEIGHT";


    public static String getString(String key)
    {
        return getAppSp().getString(key,"");
    }
    public static void setString(String key,String value)
    {
        getAppSp().edit().putString(key, value).commit();
    }
    public static int getInt(String key,int value)
    {
        return getAppSp().getInt(key,value);
    }
    public static void setInt(String key,int value)
    {
        getAppSp().edit().putInt(key, value).commit();
    }
    public static boolean getBoolean(String key)
    {
        return getAppSp().getBoolean(key,false);
    }
    public static void setBoolean(String key,boolean value)
    {
        getAppSp().edit().putBoolean(key, value).commit();
    }
    private static SharedPreferences getAppSp()
    {
        return BaseApplication.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
