package kkkj.android.revgoods.utils;

import android.content.Context;

import kkkj.android.revgoods.app.BaseApplication;


public class SizeTransform {
    public static int dip2px(Context context, float dpValue) {
        if(context==null)
        {
            context = BaseApplication.getInstance().getAppContext();
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px( float dpValue) {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *  fontScale (DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *  fontScale (DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
