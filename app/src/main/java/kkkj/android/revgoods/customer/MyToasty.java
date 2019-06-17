package kkkj.android.revgoods.customer;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class MyToasty {

    Context mContext;

    public MyToasty(Context mContext) {
        this.mContext = mContext;
    }

    public void showError(String msg)
    {
        Toasty.error(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void showSuccess(String msg)
    {
        Toasty.success(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void showInfo(String msg)
    {
        Toasty.info(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void showWarning(String msg)
    {
        Toasty.warning(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }
}
