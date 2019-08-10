package kkkj.android.revgoods.ui.chooseSupplier;

import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.ui.saveBill.BillModel;

public class ChooseSupplierPresenter extends ChooseSupplierContract.Presenter{
    @Override
    public void getSupplier() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        //调用Model请求数据
        ChooseSupplierModel chooseSupplierModel = new ChooseSupplierModel();
        ChooseSupplierModel.Request request = new ChooseSupplierModel.Request();
        chooseSupplierModel.getResponse(request, new MvpCallback<ChooseSupplierModel.Response>(getView()) {
            @Override
            public void onSuccess(ChooseSupplierModel.Response data) {
                getView().getSupplierSuc(data.getData());
            }
        });
    }

    @Override
    public void addBill(BillModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        BillModel billModel = new BillModel();
        billModel.getResponse(request, new MvpCallback<BillModel.Response>(getView()) {
            @Override
            public void onSuccess(BillModel.Response data) {
                getView().addBillSuc(data.isData());
            }
        });
    }
}
