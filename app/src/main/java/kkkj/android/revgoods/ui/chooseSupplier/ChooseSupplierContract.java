package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.ui.saveBill.BillModel;

public class ChooseSupplierContract {

    public interface View extends MvpView {

        void getSupplierSuc(List<Supplier> data);

        void addBillSuc(boolean data);
    }

    public static abstract class Presenter extends MvpPresenter<ChooseSupplierContract.View> {

        public abstract void getSupplier();

        public abstract void addBill(BillModel.Request request);
    }
}
