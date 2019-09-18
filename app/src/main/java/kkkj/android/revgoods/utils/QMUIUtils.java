package kkkj.android.revgoods.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import kkkj.android.revgoods.MainActivity;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.utils
 * Author: Admin
 * Time: 2019/9/12 16:18
 * Describe: describe
 */
public class QMUIUtils {

    public static void showNormalPopup(Context context,View v, int preferredDirection,String msg,int offset) {
        QMUIPopup mNormalPopup = new QMUIPopup(context, QMUIPopup.DIRECTION_NONE);
        TextView textView = new TextView(context);
        textView.setLayoutParams(mNormalPopup.generateLayoutParam(QMUIDisplayHelper.dp2px(context, 250),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(context, 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(context, 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(msg);
//        textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.app_color_description));
        mNormalPopup.setContentView(textView);
        mNormalPopup.setOnDismissListener(() -> {
//            Toast.makeText(context, "onDismiss", Toast.LENGTH_SHORT).show();
        });

        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mNormalPopup.setPositionOffsetX(offset);//设置浮层位置偏移量
        mNormalPopup.setPreferredDirection(preferredDirection);//QMUIPopup.DIRECTION_TOP、DIRECTION_BOTTOM、DIRECTION_NONE
        mNormalPopup.show(v);
    }


}
