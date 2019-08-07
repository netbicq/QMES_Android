package kkkj.android.revgoods.ui.chooseMatter;

import java.util.List;

import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;

public class ChooseMatterContract {

    public interface View extends MvpView
    {
        void getMatterSuc(List<Matter> data);
    }
    public static abstract class Presenter extends MvpPresenter<View> {

        public abstract void getMatter();
    }
}

