package kkkj.android.revgoods.ui.chooseSpecs;

import org.litepal.LitePal;

import java.util.List;

import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSpecs
 * Author: Admin
 * Time: 2019/7/17 13:51
 * Describe: describe
 */
public class ChooseSpecsModel extends MvpModel<ChooseSpecsModel.Request,ChooseSpecsModel.Response> {

    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        /**
         * 在MainActivity中已经向服务器请求了最新数据，并覆写了数据库
         * 这里就直接从数据库取数据
         */
//        try {
//            Response response = new Response();
//
//            int matterId = Integer.parseInt(request.getMatterId());
//            Matter matter = LitePal.find(Matter.class,matterId,true);
//            List<Specs> specsList = matter.getSpecs();
//
//            if (specsList.size() > 0) {
//                response.setData(specsList);
//                callback.onSuccess(response);
//            }else {
//                callback.onFailure("未查询到相关数据");
//            }
//        } catch (Exception e) {
//            callback.onError(e);
//        }
    }

    public static class Request extends RevGRequest {
        //品类ID
        private String matterId;

        public String getMatterId() {
            return matterId;
        }

        public void setMatterId(String matterId) {
            this.matterId = matterId;
        }
    }

    public static class Response extends RevGResponse {
        //某个品类下的规格集合
        private List<Specs> data;

        public List<Specs> getData() {
            return data;
        }

        public void setData(List<Specs> data) {
            this.data = data;
        }
    }
}
