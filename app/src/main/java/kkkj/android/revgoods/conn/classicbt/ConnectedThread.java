package kkkj.android.revgoods.conn.classicbt;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import kkkj.android.revgoods.conn.classicbt.listener.TransferProgressListener;

/**
 * @author AllenLiu
 * @version 1.0
 * @date 2019/5/8
 */
public class ConnectedThread implements Runnable {
    public static final int READ = 0;
    public static final int WRITE = 1;
    private int mode = WRITE;
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private volatile LinkedBlockingQueue<byte[]> queue;

    private Handler handler;
    private TransferProgressListener transferProgressListener;


    public ConnectedThread(BluetoothSocket socket, int mode, TransferProgressListener transferProgressListener) {
        queue = new LinkedBlockingQueue<byte[]>();
        this.transferProgressListener = transferProgressListener;
        init(socket, mode);
    }

    public void setTransferProgressListener(TransferProgressListener transferProgressListener) {
        this.transferProgressListener = transferProgressListener;
    }

    public synchronized void write(byte[] bytes) {
        CLog.e("put bytes to queue");
        try {
            queue.put(bytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
            handleFailed(e);
        }
    }


    private void init(BluetoothSocket socket, int mode) {
        handler = new Handler(Looper.getMainLooper());
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        this.mode = mode;
    }

    public void run() {
        if (mode == READ) {
            read();
        } else {
            write();
        }
    }


    private void read() {

        //byte[] buffer = new byte[1024];
        byte[] buffer = new byte[8];// buffer store for the stream
        int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                long count = 0;
                int progress = 0;
                while (count == 0) {
                    count = mmInStream.available();
                }
                CLog.e("total:" + count);
                float current = 0;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                StringBuffer sbu = new StringBuffer();
                do {
                    bytes = mmInStream.read(buffer);

                    List<String> stringList = new ArrayList<>();
                    //10进制ASCII码转化成String（48 -> "48"）
                    for (int i = 0; i < buffer.length; i++) {
                        stringList.add(buffer[i] + "");
                    }
                    ////10进制ASCII码转化成String（"48" -> "0"）
                    for (int i = 0; i < stringList.size(); i++) {
                        sbu.append(dec2Str(stringList.get(i)));
                    }

                    CLog.e("read bytes:" + bytes);
                    if (bytes > 0) {
                        current += bytes;
                        progress = (int) ((current / count) * 100);
                        byteArrayOutputStream.write(buffer);
                        handleTransfering(progress);
                    } else {
                        break;
                    }

                } while (mmInStream.available() > 0);
                CLog.e("read success:" + bytes);
                handleSuccessed(sbu.toString());
                //handleSuccessed(byteArrayOutputStream.toString());
            } catch (IOException e) {
                e.printStackTrace();
                handleFailed(e);
                break;
            }

        }
    }


    private void handleTransfering(int progress) {
        handler.post(() -> {
            if (transferProgressListener != null)
                transferProgressListener.transfering(progress);
        });
    }

    private void handleSuccessed(String s) {
        handler.post(() -> {
            if (transferProgressListener != null)
                transferProgressListener.transferSuccess(s);
        });
    }

    private void handleFailed(Exception message) {
        handler.post(() -> {
            if (transferProgressListener != null)
                transferProgressListener.transferFailed(message);
        });
    }

    /* Call this from the main activity to send data to the remote device */
    private void write() {

        while (true) {
            try {

                HashSet<byte[]> set = new HashSet<>();
                int size = queue.drainTo(set);
                if (size > 0) {
                    int index = 0;
                    for (byte[] bytes : set) {
                        mmOutStream.write(bytes);
                        handleTransfering((int) ((((++index) / (float) size)) * 100));
                    }
//                    mmOutStream.close();
                    handleSuccessed(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleFailed(e);
                break;
            }
        }

    }

    //十进制ASCII码转String
    private static String dec2Str(String ascii) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ascii.length() - 1; i += 2) {
            String h = ascii.substring(i, (i + 2));
            // 这里第二个参数传10表10进制
            int decimal = Integer.parseInt(h, 10);
            sb.append((char) decimal);
        }
        return sb.toString();
    }


}
