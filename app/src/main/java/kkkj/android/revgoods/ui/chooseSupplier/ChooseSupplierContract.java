package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;

public class ChooseSupplierContract {

    public interface View extends MvpView
    {
        void getSupplierSuc(List<Supplier> data);
    }
    public static abstract class Presenter extends MvpPresenter<ChooseSupplierContract.View> {

        public abstract void getSupplier();
    }
}
