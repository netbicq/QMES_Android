package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSupplier
 * Author: Admin
 * Time: 2019/8/8 13:41
 * Describe: 品类等级
 */
public class MatterLevelModel {

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<MatterLevel> data;

        public List<MatterLevel> getData() {
            return data;
        }

        public void setData(List<MatterLevel> data) {
            this.data = data;
        }
    }
}
