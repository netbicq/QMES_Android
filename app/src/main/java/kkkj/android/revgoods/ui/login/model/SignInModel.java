package kkkj.android.revgoods.ui.login.model;
import android.annotation.SuppressLint;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.UserView;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterModel;
import kkkj.android.revgoods.ui.chooseSpecs.ChooseSpecsModel;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierModel;
import kkkj.android.revgoods.ui.chooseSupplier.DeductionModel;
import kkkj.android.revgoods.ui.chooseSupplier.MatterLevelModel;
import kkkj.android.revgoods.ui.chooseSupplier.PriceModel;
import kkkj.android.revgoods.ui.chooseSupplier.ProduceLineModel;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

import static kkkj.android.revgoods.utils.NetUtils.checkNetWork;


public class SignInModel extends MvpModel<SignInModel.Request, SignInModel.Response> {
    @SuppressLint("CheckResult")
    @Override
    public void getResponse(Request request, final MvpCallback<Response> callback) {
        if(!checkNetWork())
        {
            try {
                SignInModel.Response response = new SignInModel.Response();
                callback.onSuccess(response);
                callback.onComplete();
            } catch (Exception e) {
                callback.onError(e);
            }
        }
        else {
            apiAuth.signin(request)
                    .subscribeOn(Schedulers.io())//IO线程加载数据
                    .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Response response) {
                            if (response.getState() == RESPONSE_OK) {
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_Token, response.getData().getUserInfo().getToken());
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_AccountID, response.getData().getAccountID());
                                callback.onSuccess(response);
                            } else {
                                callback.onFailure(response.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            callback.onError(t);
                        }

                        @Override
                        public void onComplete() {
                            callback.onComplete();
                        }
                    });

        }


    }

    public static class Response extends RevGResponse {
        UserView data;

        public UserView getData() {
            if(data==null)
            {
                return new UserView();
            }
            return data;
        }
    }

    public static class Request extends RevGRequest {
        private String AccountCode = "";
        private String Login="";
        private String Pwd="";

        public String getAccountCode() {
            return AccountCode;
        }

        public String getLogin() {
            return Login;
        }

        public String getPwd() {
            return Pwd;
        }

        public void setAccountCode(String accountCode) {
            AccountCode = accountCode;
        }

        public void setLogin(String login) {
            Login = login;
        }

        public void setPwd(String pwd) {
            Pwd = pwd;
        }
    }
}
