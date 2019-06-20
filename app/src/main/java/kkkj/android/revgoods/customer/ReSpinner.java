package kkkj.android.revgoods.customer;
import android.content.Context;
import android.util.AttributeSet;

/**
 * 自定义Spinner
 * 解决两次选择相同Item不执行onItemSelected方法
 */
public class ReSpinner extends android.support.v7.widget.AppCompatSpinner {

    private int lastPosition = 0;

    public ReSpinner(Context context) {
        super(context);
    }

    public ReSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ReSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 一个 item 选中的时候，总是会触发 setSelection 方法
    // 所以在这个方法中，我们记录并检查上一次的selection position 就行了，如果是相同的，手动调用监听即可
    @Override
    public void setSelection(int position, boolean animate) {
        super.setSelection(position, animate);
        if (position == lastPosition){
            getOnItemSelectedListener().onItemSelected(this,null,position,0);
        }
        lastPosition = position;
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (position == lastPosition){
            getOnItemSelectedListener().onItemSelected(this,null,position,0);
        }
        lastPosition = position;
    }


}
