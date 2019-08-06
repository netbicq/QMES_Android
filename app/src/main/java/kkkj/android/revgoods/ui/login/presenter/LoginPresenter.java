package kkkj.android.revgoods.ui.login.presenter;


import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.ui.login.contract.LoginContract;
import kkkj.android.revgoods.ui.login.model.SignInModel;

public class LoginPresenter extends LoginContract.Presenter{
    @Override
    public void signin(SignInModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        //显示正在加载进度条
        getView().showLoading();
        // 调用Model请求数据
        SignInModel signInModel = new SignInModel();
        signInModel.getResponse(request, new MvpCallback<SignInModel.Response>(getView()) {
            @Override
            public void onSuccess(SignInModel.Response data) {
                if (isViewAttached()){
                    getView().signSuc(data);
                }
            }
        });
    }
}
