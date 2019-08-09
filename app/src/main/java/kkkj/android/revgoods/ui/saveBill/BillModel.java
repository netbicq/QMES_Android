package kkkj.android.revgoods.ui.saveBill;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.utils.NetUtils;
import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;


/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.saveBill
 * Author: Admin
 * Time: 2019/8/8 15:34
 * Describe: describe
 */
public class BillModel extends MvpModel<BillModel.Request, BillModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        if (NetUtils.checkNetWork()) {
            apiApp.addBill(request)
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

        private BillMaster BillMaster;
        private List<DelWeights> DelWeights;
        private List<kkkj.android.revgoods.bean.bill.Scales> Scales;
        private List<kkkj.android.revgoods.bean.bill.PurPrices> PurPrices;
        private List<kkkj.android.revgoods.bean.bill.PurSamples> PurSamples;

        public kkkj.android.revgoods.bean.bill.BillMaster getBillMaster() {
            return BillMaster;
        }

        public void setBillMaster(kkkj.android.revgoods.bean.bill.BillMaster BillMaster) {
            this.BillMaster = BillMaster;
        }

        public List<kkkj.android.revgoods.bean.bill.DelWeights> getDelWeights() {
            return DelWeights;
        }

        public void setDelWeights(List<kkkj.android.revgoods.bean.bill.DelWeights> DelWeights) {
            this.DelWeights = DelWeights;
        }

        public List<kkkj.android.revgoods.bean.bill.Scales> getScales() {
            return Scales;
        }

        public void setScales(List<kkkj.android.revgoods.bean.bill.Scales> Scales) {
            this.Scales = Scales;
        }

        public List<kkkj.android.revgoods.bean.bill.PurPrices> getPurPrices() {
            return PurPrices;
        }

        public void setPurPrices(List<kkkj.android.revgoods.bean.bill.PurPrices> PurPrices) {
            this.PurPrices = PurPrices;
        }

        public List<kkkj.android.revgoods.bean.bill.PurSamples> getPurSamples() {
            return PurSamples;
        }

        public void setPurSamples(List<kkkj.android.revgoods.bean.bill.PurSamples> PurSamples) {
            this.PurSamples = PurSamples;
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
