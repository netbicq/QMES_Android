package kkkj.android.revgoods.fileUpload;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.http.RetrofitServiceManager;
import kkkj.android.revgoods.http.api.APIAttachfile;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.utils
 * 创建者:   Bpldbt
 * 创建时间: 2019/8/14 20:37
 * 描述:    TODO
 */
public class FileUploadUtils {

    private List<SamplingDetails> samplingDetailsList;

    private FileCallback mCallback;

    public FileUploadUtils(List<SamplingDetails> samplingDetailsList, FileCallback callback) {

        this.samplingDetailsList = samplingDetailsList;
        this.mCallback = callback;

        uplodaFiles();

    }

    APIAttachfile apiAttachfile = RetrofitServiceManager.getInstance().create(APIAttachfile.class);

    private void uplodaFiles() {

//        Observable.just(samplingDetailsList.get(0))
//                .map(new Function<SamplingDetails, List<GetPicModel>>() {
//                    @Override
//                    public List<GetPicModel> apply(SamplingDetails samplingDetails) throws Exception {
//                        return null;
//                    }
//                })

        for (int i = 0; i < samplingDetailsList.size(); i++) {

            int id = samplingDetailsList.get(i).getId();
            SamplingDetails samplingDetails = LitePal.find(SamplingDetails.class, id, true);
            List<GetPicModel> getPicModelList = samplingDetails.getModelList();

            for (int j = 0; j < getPicModelList.size(); j++) {

                GetPicModel getPicModel = getPicModelList.get(j);
                int type = getPicModel.getType();

                switch (type) {

                    case 0:



                        break;

                    case 1:



                        break;

                    default:
                }

            }
        }
    }
}
