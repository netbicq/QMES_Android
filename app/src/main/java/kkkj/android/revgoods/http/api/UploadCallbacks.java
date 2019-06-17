package kkkj.android.revgoods.http.api;

public interface UploadCallbacks {
    void onProgressUpdate(int percentage);

    void onError();

    void onFinish();
}
