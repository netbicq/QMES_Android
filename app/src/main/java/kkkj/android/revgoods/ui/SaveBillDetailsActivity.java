package kkkj.android.revgoods.ui;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillDetailsAdapter;
import kkkj.android.revgoods.bean.BillDetails;
import kkkj.android.revgoods.customer.MyLinearLayoutManager;
import kkkj.android.revgoods.utils.LangUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

public class SaveBillDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillDetailsAdapter adapter;
    private List<BillDetails> billDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_save_bill_details);

        /**
         * 沉浸式
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        billDetailsList = new ArrayList<>();
        for (int i=0;i<8;i++) {
            BillDetails billDetails = new BillDetails();
            billDetails.setSpecs("1.0 ~ 2.3");
            billDetails.setPrice("10.0");
            billDetails.setProportion("0.45");
            billDetails.setWeight(100.34d);
            billDetails.setTotalPrice(295.2d);
            billDetailsList.add(billDetails);
        }
        adapter = new BillDetailsAdapter(R.layout.item_price_details,billDetailsList);


        //tvTitle.setText("单据明细");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LangUtils.getAttachBaseContext(newBase, SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_USER_LANG)));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


}
