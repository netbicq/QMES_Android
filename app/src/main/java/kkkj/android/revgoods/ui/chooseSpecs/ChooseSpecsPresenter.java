package kkkj.android.revgoods.ui.chooseSpecs;

import kkkj.android.revgoods.mvpInterface.MvpCallback;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSpecs
 * Author: Admin
 * Time: 2019/7/17 14:04
 * Describe: describe
 */
public class ChooseSpecsPresenter extends ChooseSpecsContract.Presenter{
    @Override
    public void getSpecsByMatterId(ChooseSpecsModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        //调用Model请求数据
        ChooseSpecsModel chooseSpecsModel = new ChooseSpecsModel();
        chooseSpecsModel.getResponse(request, new MvpCallback<ChooseSpecsModel.Response>(getView()) {
            @Override
            public void onSuccess(ChooseSpecsModel.Response data) {
                getView().getSpecsSuc(data.getData());
            }
        });
    }
}
