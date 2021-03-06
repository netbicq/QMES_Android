package kkkj.android.revgoods.ui.chooseMatter;

import kkkj.android.revgoods.mvpInterface.MvpCallback;

public class ChooseMatterPresenter extends ChooseMatterContract.Presenter{
    @Override
    public void getMatter() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        //调用Model请求数据
        ChooseMatterModel chooseMatterModel = new ChooseMatterModel();
        ChooseMatterModel.Request request = new ChooseMatterModel.Request();
        chooseMatterModel.getResponse(request, new MvpCallback<ChooseMatterModel.Response>(getView()) {
            @Override
            public void onSuccess(ChooseMatterModel.Response data) {
                getView().getMatterSuc(data.getData());
            }
        });
    }
}
