package kkkj.android.revgoods.ui.addDeductionCategory;

import org.litepal.LitePal;

import java.util.List;

import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.ui.chooseSupplier.ChooseSupplierModel;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.addDeductionCategory
 * Author: Admin
 * Time: 2019/8/5 15:32
 * Describe: describe
 */
public class DeductionCategoryModel extends MvpModel<DeductionCategoryModel.Request, DeductionCategoryModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        if (!NetUtils.checkNetWork()) {
            try {

                DeductionCategoryModel.Response response = new DeductionCategoryModel.Response();
                List<DeductionCategory> deductionCategoryList = LitePal.findAll(DeductionCategory.class);
                if (deductionCategoryList.size() > 0) {
                    response.setData(deductionCategoryList);
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

        private List<DeductionCategory> data;

        public List<DeductionCategory> getData() {
            return data;
        }

        public void setData(List<DeductionCategory> data) {
            this.data = data;
        }
    }
}
