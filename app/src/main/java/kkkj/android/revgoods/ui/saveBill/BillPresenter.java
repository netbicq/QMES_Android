package kkkj.android.revgoods.ui.saveBill;

import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.ui.chooseSupplier.UpLoadFileModel;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.saveBill
 * Author: Admin
 * Time: 2019/8/8 15:45
 * Describe: describe
 */
public class BillPresenter extends BillContract.Presenter{
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

            @Override
            public void onComplete() {
                getView().onCompleted();
            }
        });
    }

    @Override
    public void uploadfile(UpLoadFileModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        //显示正在加载进度条
        getView().showLoading();
        // 调用Model请求数据
        UpLoadFileModel loginModel = new UpLoadFileModel();
//        return loginModel.getResponseSync(request);
        loginModel.getResponse(request, new MvpCallback<UpLoadFileModel.Response>(getView()) {
            @Override
            public void onSuccess(UpLoadFileModel.Response data) {


            }
        });
    }
}
