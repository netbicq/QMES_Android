package kkkj.android.revgoods.ui.chooseSupplier;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * 供应商
 */
public class ChooseSupplierModel extends MvpModel<ChooseSupplierModel.Request,ChooseSupplierModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        if (!NetUtils.checkNetWork()) {
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

        }else {
            /**
             * 从服务器请求数据
             */
            apiApp.getSuppliers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            if (response.getState() == RESPONSE_OK) {
                                if (response.getData().size() > 0) {

                                    LitePal.deleteAll(Supplier.class);
                                    List<Supplier> supplierList = response.getData();
                                    for (int i = 0; i < supplierList.size(); i++) {
                                        Supplier supplier = new Supplier();
                                        supplier.setKeyID(supplierList.get(i).getKeyID());
                                        supplier.setName(supplierList.get(i).getName());
                                        supplier.save();
                                    }

                                }

                                callback.onSuccess(response);
                                callback.onFailure(response.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            callback.onComplete();
                        }
                    });

        }
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
