package kkkj.android.revgoods.ui.chooseSupplier;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import kkkj.android.revgoods.http.ProgressRequestBody;
import kkkj.android.revgoods.http.ResultException;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;
import kkkj.android.revgoods.http.api.UploadCallbacks;
import kkkj.android.revgoods.mvpInterface.MvpCallback;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UpLoadFileModel extends MvpModel<UpLoadFileModel.Request, UpLoadFileModel.Response> {
    @Override
    public void getResponse(Request request, MvpCallback<Response> callback) {
        apiAttachfile.uploadfile(request.getFiles())
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(UpLoadFileModel.Response response) {
                        if (response.getState() == RESPONSE_OK) {
                            callback.onSuccess(response);
                        }
                        else {
                            callback.onFailure(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }
                });
    }

    @Override
    public Response getResponseSync(Request request) {
        try {
            return apiAttachfile.uploadfileSynchro(request.getFiles()).execute().body();
        } catch (IOException e) {
            throw new ResultException(9999, "文件上传失败");
        }
    }

    public static class Response extends RevGResponse {
        String data = "";

        public String getData() {
            if(data==null)
            {
                return "";
            }
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class Request extends RevGRequest {
        File file;
        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        String mediaType;//video  image
        public UploadCallbacks getListener() {
            return listener;
        }

        public void setListener(UploadCallbacks listener) {
            this.listener = listener;
        }
        UploadCallbacks listener;
        public Map<String, RequestBody> getFiles() {
            ProgressRequestBody body = new ProgressRequestBody(file,mediaType+"/*",listener);
            Map<String, RequestBody> bodys = new HashMap<String, RequestBody>();
            if(file!=null)
            {
                String suffix = file.getName().substring(file.getName().lastIndexOf(".")+1);
                Logger.d(suffix);
                bodys.put(String.format("file\"; filename=\"" + file.getName()),
                        RequestBody.create(MediaType.parse(mediaType+"/"+suffix), file));
            }
            bodys.put("file", body);
            return bodys;
        }
        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}
