package kkkj.android.revgoods.http.api;


import java.util.Map;

import io.reactivex.Observable;
import kkkj.android.revgoods.http.ApiConfig;
import kkkj.android.revgoods.ui.chooseSupplier.UpLoadFileModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface APIAttachfile {
//    //api/file/post 文件上传
    @Multipart
    @POST(ApiConfig.BASE_URL +"api/file/post")
    Observable<UpLoadFileModel.Response> uploadfile(@PartMap Map<String, RequestBody> files);

    @Multipart
    @POST(ApiConfig.BASE_URL +"api/attachfile/uploadfile")
    Call<UpLoadFileModel.Response> uploadfileSynchro(@PartMap Map<String, RequestBody> files);
//
//    //APIAuth - api/attachfile/getfiles 根据结果ID，删除检查结果
//    @GET(ApiConfig.BASE_URL +"api/attachfile/getfiles/{businessid}")
//    Observable<DownLoadFileModel.Response> getfiles(@Path("businessid") String subresultid);
}
