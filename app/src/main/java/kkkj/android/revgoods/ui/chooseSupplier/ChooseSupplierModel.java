package kkkj.android.revgoods.ui.chooseSupplier;

import org.litepal.LitePal;

import java.util.List;

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

        }
    }

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {
        //某个供应商下的品类集合
        private List<Supplier> data;

        public List<Supplier> getData() {
            return data;
        }

        public void setData(List<Supplier> data) {
            this.data = data;
        }
    }
}
