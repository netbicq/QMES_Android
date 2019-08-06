package kkkj.android.revgoods.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillDetailsAdapter;
import kkkj.android.revgoods.bean.BillDetails;
import kkkj.android.revgoods.customer.MyLinearLayoutManager;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.fragment
 * Author: Admin
 * Time: 2019/8/6 10:18
 * Describe: describe
 */
public class TestFragment extends BaseFullScreenDialogFragment {

    private RecyclerView recyclerView;
    private BillDetailsAdapter adapter;
    private List<BillDetails> billDetailsList;

    @Override
    public void initData() {
        billDetailsList = new ArrayList<>();
        for (int i=0;i<4;i++) {
            BillDetails billDetails = new BillDetails();
            billDetails.setSpecs("1.0 ~ 2.3");
            billDetails.setPrice("10.0");
            billDetails.setProportion("0.45");
            billDetails.setWeight(100.34d);
            billDetails.setTotalPrice(295.2d);
            billDetailsList.add(billDetails);
        }
        adapter = new BillDetailsAdapter(R.layout.item_price_details,billDetailsList);
    }

    @Override
    public void initView(View view) {
        tvTitle.setText("单据明细");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_save_bill_details;
    }
}
