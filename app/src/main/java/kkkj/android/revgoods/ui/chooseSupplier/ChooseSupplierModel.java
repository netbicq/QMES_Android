package kkkj.android.revgoods.ui.chooseSupplier;

import android.annotation.SuppressLint;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.ui.chooseMatter.ChooseMatterModel;
import kkkj.android.revgoods.ui.chooseSpecs.ChooseSpecsModel;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * 供应商
 */
public class ChooseSupplierModel extends MvpModel<ChooseSupplierModel.Request,ChooseSupplierModel.Response> {

    @SuppressLint("CheckResult")
    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
//        if (NetUtils.checkNetWork()) {
//            /**
//             * 从服务器请求数据
//             * 获取供应商
//             */
//            apiApp.getSuppliers()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Response>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(Response response) {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0) {
//
//                                    List<Supplier> supplierList = response.getData();
//                                    for (int i = 0; i < supplierList.size(); i++) {
//                                        String keyId = supplierList.get(i).getKeyID();
//                                        supplierList.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                    }
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                            try {
//
//                                ChooseSupplierModel.Response response = new ChooseSupplierModel.Response();
//                                List<Supplier> supplierList = LitePal.findAll(Supplier.class);
//                                if (supplierList.size() > 0) {
//                                    response.setData(supplierList);
//                                    callback.onSuccess(response);
//                                } else {
//                                    callback.onFailure("未查询到相关数据");
//                                }
//                                callback.onComplete();
//
//                            }catch (Exception e) {
//                                callback.onError(e);
//                            }
//
//                        }
//                    });

//            /**
//             * 获取品类
//             */
//            apiApp.getMatters()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ChooseMatterModel.Response>() {
//                        @Override
//                        public void accept(ChooseMatterModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//
//                                    List<Matter> list = response.getData();
//                                    for (int i = 0;i<list.size();i++) {
//                                        String keyId = list.get(i).getKeyID();
//                                        list.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(list.get(i).toString());
//                                    }
//                                }
//                            }
//                        }
//                    });
//
//            /**
//             * 获取规格
//             */
//            apiApp.getSpecses()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ChooseSpecsModel.Response>() {
//                        @Override
//                        public void accept(ChooseSpecsModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//
//                                    List<Specs> specsList = response.getData();
//                                    for (int i = 0;i<specsList.size();i++) {
//                                        String keyId = specsList.get(i).getKeyID();
//                                        specsList.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(specsList.get(i).toString());
//                                    }
//                                }
//                            }
//                        }
//                    });
//
//
//            /**
//             * 获取词典：扣重类别 (dicttype =2)
//             */
//            apiApp.getDeductionCategory()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<DeductionModel.Response>() {
//                        @Override
//                        public void accept(DeductionModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//
//                                    List<DeductionCategory> deductionCategoryList = response.getData();
//                                    for (int i = 0;i<deductionCategoryList.size();i++) {
//                                        String keyId = deductionCategoryList.get(i).getKeyID();
//                                        boolean is = deductionCategoryList.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(deductionCategoryList.get(i).toString() + is);
//                                    }
//                                }
//                            }
//                        }
//                    });
//            /**
//             * 品类等级(dicttype =3)
//             */
//            apiApp.getMatterLevel()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<MatterLevelModel.Response>() {
//                        @Override
//                        public void accept(MatterLevelModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//                                    List<MatterLevel> matterLevelList = response.getData();
//                                    for (int i = 0;i<matterLevelList.size();i++) {
//                                        String keyId = matterLevelList.get(i).getKeyID();
//                                        boolean is = matterLevelList.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(matterLevelList.get(i).toString() + is);
//                                    }
//                                }
//                            }
//                        }
//                    });
//
//            /**
//             * 获取生产线配置
//             */
//            apiApp.getProduceLines()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ProduceLineModel.Response>() {
//                        @Override
//                        public void accept(ProduceLineModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//
//                                    List<ProduceLine> list = response.getData();
//                                    for (int i = 0;i<list.size();i++) {
//                                        String keyId = list.get(i).getKeyID();
//                                        list.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(list.get(i).toString());
//                                        //反序列化
////                                        String sPower = list.get(i).getSapmle();
////                                        Logger.d( "---------->" + sPower);
////                                        Sapmle power = new Gson().fromJson(sPower,Sapmle.class);
////                                        Logger.d(power.toString());
//
//                                    }
//                                }
//                            }
//                        }
//                    });
//
//            /**
//             * 获取价格配置表
//             */
//            apiApp.getPrice()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<PriceModel.Response>() {
//                        @Override
//                        public void accept(PriceModel.Response response) throws Exception {
//                            if (response.getState() == RESPONSE_OK) {
//                                if (response.getData().size() > 0){
//
//                                    List<Price> priceList = response.getData();
//                                    for (int i = 0;i<priceList.size();i++) {
//                                        String keyId = priceList.get(i).getKeyID();
//                                        priceList.get(i).saveOrUpdate("KeyID = ?",keyId);
//                                        Logger.d(priceList.get(i).toString());
//                                    }
//                                }
//                            }
//                        }
//                    });


//        } else {
            //无网络情况下
            try {

                ChooseSupplierModel.Response response = new ChooseSupplierModel.Response();
                List<Supplier> supplierList = LitePal.findAll(Supplier.class);
                if (supplierList.size() > 0) {
                    response.setData(supplierList);
                    callback.onSuccess(response);
                } else {
                    callback.onFailure("未查询到相关数据");
                }
                callback.onComplete();

            }catch (Exception e) {
                callback.onError(e);
            }
//        }


    }

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<Supplier> data;

        public List<Supplier> getData() {
            return data;
        }

        public void setData(List<Supplier> data) {
            this.data = data;
        }
    }
}
