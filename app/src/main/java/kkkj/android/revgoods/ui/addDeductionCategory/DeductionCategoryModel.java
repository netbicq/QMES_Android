package kkkj.android.revgoods.ui.addDeductionCategory;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.List;

import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;

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

        /**
         * 在LoginActivity中已经向服务器请求了最新数据，并覆写了数据库
         * 这里就直接从数据库取数据
         */

        try {

            DeductionCategoryModel.Response response = new DeductionCategoryModel.Response();
            List<DeductionCategory> dictList = LitePal.findAll(DeductionCategory.class);
            for (int i=0;i<dictList.size();i++) {
                Logger.d(dictList.get(i).toString());
            }
            if (dictList.size() > 0) {
                response.setData(dictList);
                callback.onSuccess(response);
            } else {
                callback.onFailure("未查询到相关数据");
            }
            callback.onComplete();

        } catch (Exception e) {
            callback.onError(e);
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
