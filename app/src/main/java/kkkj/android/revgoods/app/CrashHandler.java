package kkkj.android.revgoods.app;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;
    public CrashHandler(Context context){
        this.context=context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        showToast(t);
    }

    /**
     * 操作
     * @param thread
     */
    private void showToast(Thread thread) {
//        final Dialog dialog=new Dialog(context);
//        dialog.setContentView(R.layout.dialog_layout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "程序异常，重新启动", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        try {
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //restartApp();
    }

    /**
     * 重启应用
     */
    private void restartApp(){
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
        BaseApplication.getInstance().exitApp();
        android.os.Process.killProcess(android.os.Process.myPid());//再此之前可以做些退出等操作
    }
}