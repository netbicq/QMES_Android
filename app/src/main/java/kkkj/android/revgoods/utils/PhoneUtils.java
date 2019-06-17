package kkkj.android.revgoods.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class PhoneUtils {
    private static Intent t = new Intent();
    // Intent.ACTION_DIAL: 激活拨号界面
    // Intent.ACTION_CALL: 直接拨打电话
    public static void dial(Activity mActivity, String phoneNum)
    {
        t.setAction(Intent.ACTION_DIAL);
        // Data: 数据.具体的动作所需要的附加数据
        //封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
        t.setData(Uri.parse("tel:" + phoneNum));
        // 通知系统你去帮我干活吧
        mActivity.startActivity(t);
    }
    public static void call(Activity mActivity, String phoneNum)
    {
        t.setAction(Intent.ACTION_CALL);
        // Data: 数据.具体的动作所需要的附加数据
        //封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
        t.setData(Uri.parse("tel:" + phoneNum));
        // 通知系统你去帮我干活吧
        mActivity.startActivity(t);
    }
}
