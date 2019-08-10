package kkkj.android.revgoods.ui.addDeductionCategory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.addDeductionCategory
 * Author: Admin
 * Time: 2019/8/8 10:35
 * Describe: describe
 */
public class AddDeductionCategoryModel extends MvpModel<AddDeductionCategoryModel.Request, AddDeductionCategoryModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        if (NetUtils.checkNetWork()) {
            apiApp.addDict(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            if (response.getState() == RESPONSE_OK) {
                                callback.onSuccess(response);
                            }else {
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
        /**
         *
         * DictName : sample string 1
         */

        private String DictName;


        public String getDictName() {
            return DictName;
        }

        public void setDictName(String DictName) {
            this.DictName = DictName;
        }

    }

    public static class Response extends RevGResponse {
        private DeductionCategory data;

        public DeductionCategory getData() {
            return data;
        }

        public void setData(DeductionCategory data) {
            this.data = data;
        }
    }
}
