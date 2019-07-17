package kkkj.android.revgoods.ui.chooseSupplier;

import kkkj.android.revgoods.mvpInterface.MvpCallback;

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
}
