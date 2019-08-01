package kkkj.android.revgoods.customer;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class MyToasty {

    private Context mContext;

    public MyToasty(Context mContext) {
        this.mContext = mContext;
    }

    public void showError(String msg)
    {
        Toasty.error(mContext,msg,Toast.LENGTH_LONG,true).show();
    }

    public void showSuccess(String msg)
    {
        Toasty.success(mContext, msg, Toast.LENGTH_LONG, true).show();
    }

    public void showInfo(String msg)
    {
        Toasty.info(mContext, msg, Toast.LENGTH_LONG, true).show();
    }

    public void showWarning(String msg)
    {
        Toasty.warning(mContext, msg, Toast.LENGTH_LONG, true).show();
    }
}
