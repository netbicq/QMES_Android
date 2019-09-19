package kkkj.android.revgoods.ui.login.model;
import android.annotation.SuppressLint;
import android.widget.GridLayout;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

                                //登录成功之后请求数据
                                request();

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

    //请求数据

    private void request() {

        apiApp.getSuppliers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ChooseSupplierModel.Response>() {
                    @Override
                    public void accept(ChooseSupplierModel.Response response) throws Exception {
                        if (response.getState() == RESPONSE_OK) {
                            if (response.getData().size() > 0) {
                                //服务器返回的所有KeyId集合
                                List<String> keyIdList = new ArrayList<>();
                                List<Supplier> supplierList = response.getData();

                                for (int i = 0; i < supplierList.size(); i++) {
                                    String keyId = supplierList.get(i).getKeyID();
                                    keyIdList.add(keyId);
                                    supplierList.get(i).saveOrUpdate("KeyID = ?", keyId);
                                    Logger.d(supplierList.get(i).toString());
                                }

                                List<Supplier> supplierListOld = LitePal.findAll(Supplier.class);
                                for (int j=0;j<supplierListOld.size();j++) {
                                    //本地数据库KeyId
                                    String keyIdOld = supplierListOld.get(j).getKeyID();
                                    /**
                                     * 如果本地keyId不包含在服务器返回的keyId集合里面，说明服务器端已经删除了该keyId
                                     * 那本地也将该keyId删除
                                     */
                                    if (!keyIdList.contains(keyIdOld)) {
                                        List<Supplier> list = LitePal.where("KeyID = ?",keyIdOld).find(Supplier.class);
                                        int id = list.get(0).getId();
                                        LitePal.delete(Supplier.class,id);
                                    }
                                }

                            }
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .concatMap(new Function<ChooseSupplierModel.Response, ObservableSource<ChooseMatterModel.Response>>() {
                    @Override
                    public ObservableSource<ChooseMatterModel.Response> apply(ChooseSupplierModel.Response response) throws Exception {

                        return apiApp.getMatters();
                    }
                }).concatMap(new Function<ChooseMatterModel.Response, ObservableSource<ChooseSpecsModel.Response>>() {
            @Override
            public ObservableSource<ChooseSpecsModel.Response> apply(ChooseMatterModel.Response response) throws Exception {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {

                        List<String> keyIdList = new ArrayList<>();
                        List<Matter> list = response.getData();
                        for (int i = 0; i < list.size(); i++) {
                            String keyId = list.get(i).getKeyID();
                            keyIdList.add(keyId);
                            list.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(list.get(i).toString());
                        }

                        List<Matter> matterListOld = LitePal.findAll(Matter.class);
                        for (int j=0;j<matterListOld.size();j++) {
                            String keyIdOld = matterListOld.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<Matter> matterList = LitePal.where("KeyID = ?",keyIdOld).find(Matter.class);
                                int id = matterList.get(0).getId();
                                LitePal.delete(Matter.class,id);
                            }
                        }
                    }
                }
                return apiApp.getSpecses();
            }
        }).concatMap(new Function<ChooseSpecsModel.Response, ObservableSource<DeductionModel.Response>>() {
            @Override
            public ObservableSource<DeductionModel.Response> apply(ChooseSpecsModel.Response response) throws Exception {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {

                        List<String> keyIdList = new ArrayList<>();
                        List<Specs> specsList = response.getData();
                        for (int i = 0; i < specsList.size(); i++) {
                            String keyId = specsList.get(i).getKeyID();
                            keyIdList.add(keyId);
                            specsList.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(specsList.get(i).toString());
                        }

                        List<Specs> specsListOld = LitePal.findAll(Specs.class);
                        for (int j=0;j<specsListOld.size();j++) {
                            String keyIdOld = specsListOld.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<Specs> list = LitePal.where("KeyID = ?",keyIdOld).find(Specs.class);
                                int id = list.get(0).getId();
                                LitePal.delete(Specs.class,id);
                            }
                        }

                    }
                }
                return apiApp.getDeductionCategory();
            }
        }).concatMap(new Function<DeductionModel.Response, ObservableSource<MatterLevelModel.Response>>() {
            @Override
            public ObservableSource<MatterLevelModel.Response> apply(DeductionModel.Response response) throws Exception {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {

                        List<String> keyIdList = new ArrayList<>();
                        List<DeductionCategory> deductionCategoryList = response.getData();
                        for (int i = 0; i < deductionCategoryList.size(); i++) {
                            String keyId = deductionCategoryList.get(i).getKeyID();
                            keyIdList.add(keyId);
                            boolean is = deductionCategoryList.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(deductionCategoryList.get(i).toString() + is);
                        }

                        List<DeductionCategory> deductionCategoryListOld = LitePal.findAll(DeductionCategory.class);
                        for (int j=0;j<deductionCategoryListOld.size();j++) {
                            String keyIdOld = deductionCategoryListOld.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<DeductionCategory> list = LitePal.where("KeyID = ?",keyIdOld).find(DeductionCategory.class);
                                int id = list.get(0).getId();
                                LitePal.delete(DeductionCategory.class,id);
                            }
                        }
                    }
                }
                return apiApp.getMatterLevel();
            }
        }).concatMap(new Function<MatterLevelModel.Response, ObservableSource<ProduceLineModel.Response>>() {
            @Override
            public ObservableSource<ProduceLineModel.Response> apply(MatterLevelModel.Response response) throws Exception {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {
                        List<String> keyIdList = new ArrayList<>();
                        List<MatterLevel> matterLevelList = response.getData();
                        for (int i = 0; i < matterLevelList.size(); i++) {
                            String keyId = matterLevelList.get(i).getKeyID();
                            keyIdList.add(keyId);
                            boolean is = matterLevelList.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(matterLevelList.get(i).toString() + is);
                        }

                        List<MatterLevel> matterLevelListOld = LitePal.findAll(MatterLevel.class);
                        for (int j=0;j<matterLevelListOld.size();j++) {
                            String keyIdOld = matterLevelListOld.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<MatterLevel> list = LitePal.where("KeyID = ?",keyIdOld).find(MatterLevel.class);
                                int id = list.get(0).getId();
                                LitePal.delete(MatterLevel.class,id);
                            }
                        }
                    }
                }
                return apiApp.getProduceLines();
            }
        }).concatMap(new Function<ProduceLineModel.Response, ObservableSource<PriceModel.Response>>() {
            @Override
            public ObservableSource<PriceModel.Response> apply(ProduceLineModel.Response response) throws Exception {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {
                        List<String> keyIdList = new ArrayList<>();
                        List<ProduceLine> list = response.getData();
                        for (int i = 0; i < list.size(); i++) {
                            String keyId = list.get(i).getKeyID();
                            keyIdList.add(keyId);
                            list.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(list.get(i).toString());
                        }

                        List<ProduceLine> produceLineOldList = LitePal.findAll(ProduceLine.class);
                        for (int j=0;j<produceLineOldList.size();j++) {
                            String keyIdOld = produceLineOldList.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<ProduceLine> list1 = LitePal.where("KeyID = ?",keyIdOld).find(ProduceLine.class);
                                int id = list1.get(0).getId();
                                LitePal.delete(ProduceLine.class,id);
                            }
                        }

                    }
                }
                return apiApp.getPrice();
            }
        }).subscribe(new Observer<PriceModel.Response>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PriceModel.Response response) {
                if (response.getState() == RESPONSE_OK) {
                    if (response.getData().size() > 0) {

                        List<String> keyIdList = new ArrayList<>();
                        List<Price> priceList = response.getData();
                        for (int i = 0; i < priceList.size(); i++) {
                            String keyId = priceList.get(i).getKeyID();
                            keyIdList.add(keyId);
                            priceList.get(i).saveOrUpdate("KeyID = ?", keyId);
                            Logger.d(priceList.get(i).toString());
                        }

                        List<Price> priceListOld = LitePal.findAll(Price.class);
                        for (int j=0;j<priceListOld.size();j++) {
                            String keyIdOld = priceListOld.get(j).getKeyID();
                            if (!keyIdList.contains(keyIdOld)) {
                                List<Price> list = LitePal.where("KeyID = ?",keyIdOld).find(Price.class);
                                int id = list.get(0).getId();
                                LitePal.delete(Price.class,id);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });

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
