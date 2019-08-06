package kkkj.android.revgoods.customer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.customer
 * Author: Admin
 * Time: 2019/8/6 15:15
 * Describe: describe
 */
public class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * 禁止RecyclerView滑动
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
