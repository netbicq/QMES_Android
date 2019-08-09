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
//            apiAuth.signin(request)
//                    .subscribeOn(Schedulers.io())//IO线程加载数据
//                    .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
//                    .subscribe(new Observer<Response>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                        }
//
//                        @Override
//                        public void onNext(Response response) {
//                            if (response.getState() == RESPONSE_OK) {
//                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_Token, response.getData().getUserInfo().getToken());
//                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_AccountID, response.getData().getAccountID());
//                                callback.onSuccess(response);
//                            } else {
//                                callback.onFailure(response.getMsg());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable t) {
//                            callback.onError(t);
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            callback.onComplete();
//                        }
//                    });

            apiAuth.signin(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<Response>() {
                        @Override
                        public void accept(Response response) throws Exception {
                            if (response.getState() == RESPONSE_OK) {
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_Token, response.getData().getUserInfo().getToken());
                                SharedPreferenceUtil.setString(SharedPreferenceUtil.SP_Commonparts_AccountID, response.getData().getAccountID());
                                callback.onSuccess(response);
                            } else {
                                callback.onFailure(response.getMsg());
                            }
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<Response, ObservableSource<ChooseSupplierModel.Response>>() { //获取供应商
                        @Override
                        public ObservableSource<ChooseSupplierModel.Response> apply(Response response) throws Exception {
                            return apiApp.getSuppliers();
                        }
                    }).flatMap(new Function<ChooseSupplierModel.Response, ObservableSource<ChooseMatterModel.Response>>() {
                @Override
                public ObservableSource<ChooseMatterModel.Response> apply(ChooseSupplierModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0) {

                            List<Supplier> supplierList = response.getData();
                            for (int i = 0; i < supplierList.size(); i++) {
                                String keyId = supplierList.get(i).getKeyID();
                                supplierList.get(i).saveOrUpdate("KeyID = ?",keyId);
                            }
                        }

                    }
                    return apiApp.getMatters();
                }
            }).flatMap(new Function<ChooseMatterModel.Response, ObservableSource<ChooseSpecsModel.Response>>() {
                @Override
                public ObservableSource<ChooseSpecsModel.Response> apply(ChooseMatterModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){

                            List<Matter> list = response.getData();
                            for (int i = 0;i<list.size();i++) {
                                String keyId = list.get(i).getKeyID();
                                list.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(list.get(i).toString());
                            }
                        }
                    }
                    return apiApp.getSpecses();
                }
            }).flatMap(new Function<ChooseSpecsModel.Response, ObservableSource<DeductionModel.Response>>() {
                @Override
                public ObservableSource<DeductionModel.Response> apply(ChooseSpecsModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){

                            List<Specs> specsList = response.getData();
                            for (int i = 0;i<specsList.size();i++) {
                                String keyId = specsList.get(i).getKeyID();
                                specsList.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(specsList.get(i).toString());
                            }
                        }
                    }
                    return apiApp.getDeductionCategory();
                }
            }).flatMap(new Function<DeductionModel.Response, ObservableSource<MatterLevelModel.Response>>() {
                @Override
                public ObservableSource<MatterLevelModel.Response> apply(DeductionModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){

                            List<DeductionCategory> deductionCategoryList = response.getData();
                            for (int i = 0;i<deductionCategoryList.size();i++) {
                                String keyId = deductionCategoryList.get(i).getKeyID();
                                boolean is = deductionCategoryList.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(deductionCategoryList.get(i).toString() + is);
                            }
                        }
                    }
                    return apiApp.getMatterLevel();
                }
            }).flatMap(new Function<MatterLevelModel.Response, ObservableSource<ProduceLineModel.Response>>() {
                @Override
                public ObservableSource<ProduceLineModel.Response> apply(MatterLevelModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){
                            List<MatterLevel> matterLevelList = response.getData();
                            for (int i = 0;i<matterLevelList.size();i++) {
                                String keyId = matterLevelList.get(i).getKeyID();
                                boolean is = matterLevelList.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(matterLevelList.get(i).toString() + is);
                            }
                        }
                    }
                    return apiApp.getProduceLines();
                }
            }).flatMap(new Function<ProduceLineModel.Response, ObservableSource<PriceModel.Response>>() {
                @Override
                public ObservableSource<PriceModel.Response> apply(ProduceLineModel.Response response) throws Exception {
                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){

                            List<ProduceLine> list = response.getData();
                            for (int i = 0;i<list.size();i++) {
                                String keyId = list.get(i).getKeyID();
                                list.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(list.get(i).toString());
                                //反序列化
//                                        String sPower = list.get(i).getSapmle();
//                                        Logger.d( "---------->" + sPower);
//                                        Sapmle power = new Gson().fromJson(sPower,Sapmle.class);
//                                        Logger.d(power.toString());

                            }
                        }
                    }
                    return apiApp.getPrice();
                }
            }).subscribe(new Consumer<PriceModel.Response>() {
                @Override
                public void accept(PriceModel.Response response) throws Exception {

                    if (response.getState() == RESPONSE_OK) {
                        if (response.getData().size() > 0){

                            List<Price> priceList = response.getData();
                            for (int i = 0;i<priceList.size();i++) {
                                String keyId = priceList.get(i).getKeyID();
                                priceList.get(i).saveOrUpdate("KeyID = ?",keyId);
                                Logger.d(priceList.get(i).toString());
                            }
                        }
                    }
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
