package kkkj.android.revgoods.utils;

import android.content.Context;

import kkkj.android.revgoods.app.RevGoods;


public class SizeTransform {
    public static int dip2px(Context context, float dpValue) {
        if(context==null)
        {
            context = RevGoods.getInstance().getAppContext();
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px( float dpValue) {
        final float scale = RevGoods.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
