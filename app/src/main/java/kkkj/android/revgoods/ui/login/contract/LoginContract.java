package kkkj.android.revgoods.ui.login.contract;


import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;
import kkkj.android.revgoods.ui.login.model.SignInModel;

public class LoginContract {
    public interface View extends MvpView
    {
        void signSuc(SignInModel.Response response);
    }
    public static abstract class Presenter extends MvpPresenter<View> {
        public abstract void signin(SignInModel.Request request);
    }
}
