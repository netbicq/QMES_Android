package kkkj.android.revgoods.ui.chooseMatter;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.utils.NetUtils;

/**
 * 品类
 */
public class ChooseMatterModel extends MvpModel<ChooseMatterModel.Request,ChooseMatterModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        /**
         * 在MainActivity中已经向服务器请求了最新数据，并覆写了数据库
         * 这里就直接从数据库取数据
         */
        try {
            Response response = new Response();
            List<Matter> matterList = LitePal.findAll(Matter.class);

            if (matterList.size() > 0) {
                response.setData(matterList);
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

        private List<Matter> data;

        public List<Matter> getData() {
            return data;
        }

        public void setData(List<Matter> data) {
            this.data = data;
        }
    }
}
