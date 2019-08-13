package kkkj.android.revgoods.ui.saveBill;

import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.ui.chooseSupplier.UpLoadFileModel;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.saveBill
 * Author: Admin
 * Time: 2019/8/8 15:43
 * Describe: describe
 */
public class BillContract {
    public interface View extends MvpView {
        void addBillSuc(boolean data);
        void uploadfileSuc(String date);
        void onCompleted();
    }

    public static abstract class Presenter extends MvpPresenter<View> {

        public abstract void addBill(BillModel.Request request);
        public abstract void uploadfile(UpLoadFileModel.Request request);
    }
}
