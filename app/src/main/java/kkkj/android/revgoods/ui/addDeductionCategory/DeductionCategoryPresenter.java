package kkkj.android.revgoods.ui.addDeductionCategory;

import kkkj.android.revgoods.mvpInterface.MvpCallback;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.addDeductionCategory
 * Author: Admin
 * Time: 2019/8/8 10:50
 * Describe: describe
 */
public class DeductionCategoryPresenter extends DeductionCategoryContract.Presenter{
    @Override
    public void getDeductionCategory() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        //调用Model请求数据
        DeductionCategoryModel model = new DeductionCategoryModel();
        DeductionCategoryModel.Request request = new DeductionCategoryModel.Request();
        model.getResponse(request, new MvpCallback<DeductionCategoryModel.Response>(getView()) {
            @Override
            public void onSuccess(DeductionCategoryModel.Response data) {
                getView().getDeductionCategorySuc(data.getData());
            }
        });


    }

    @Override
    public void addDeductionCategory(AddDeductionCategoryModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        AddDeductionCategoryModel model = new AddDeductionCategoryModel();
        model.getResponse(request, new MvpCallback<AddDeductionCategoryModel.Response>(getView()) {
            @Override
            public void onSuccess(AddDeductionCategoryModel.Response data) {
                getView().addDeductionCategorySuc(data.getData());
            }
        });

    }

    @Override
    public void deleteDeductionCategory(DeleteDeductionCategoryModel.Request request) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }

        DeleteDeductionCategoryModel model = new DeleteDeductionCategoryModel();
        model.getResponse(request, new MvpCallback<DeleteDeductionCategoryModel.Response>(getView()) {
            @Override
            public void onSuccess(DeleteDeductionCategoryModel.Response data) {
                getView().deleteDeductionCategorySuc(data);
            }

            @Override
            public void onFailure(String msg) {
                getView().deleteDeductionCategoryFail(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                getView().deleteDeductionCategoryFail(throwable.getMessage());
            }
        });
    }

}
