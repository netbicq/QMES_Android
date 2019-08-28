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
 * Time: 2019/8/26 13:43
 * Describe: describe
 */
public class DeleteDeductionCategoryModel extends MvpModel<DeleteDeductionCategoryModel.Request, DeleteDeductionCategoryModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        if (NetUtils.checkNetWork()) {
            apiApp.deleteDict(request.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            callback.onSuccess(response);
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

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Response extends RevGResponse {
        private boolean data;

        public boolean isData() {
            return data;
        }

        public void setData(boolean data) {
            this.data = data;
        }
    }
}
