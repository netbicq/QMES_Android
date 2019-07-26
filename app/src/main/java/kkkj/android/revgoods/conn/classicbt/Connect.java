package kkkj.android.revgoods.conn.classicbt;

import android.bluetooth.BluetoothSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kkkj.android.revgoods.conn.classicbt.listener.TransferProgressListener;

/**
 * @author AllenLiu
 * @version 1.0
 * @date 2019/5/8
 */
public class Connect {

    private ExecutorService executor;
    private ConnectedThread readThread;
    private ConnectedThread writeThread;

    public Connect(BluetoothSocket socket) {
        executor = Executors.newFixedThreadPool(2);
        writeThread = new ConnectedThread(socket, ConnectedThread.WRITE, null);
        readThread = new ConnectedThread(socket, ConnectedThread.READ, null);
        executor.execute(writeThread);
        executor.execute(readThread);

    }

    public void setWriteProgressListener(TransferProgressListener transferProgressListener) {
        writeThread.setTransferProgressListener(transferProgressListener);
    }

    public void setReadProgressListener(TransferProgressListener transferProgressListener) {
        readThread.setTransferProgressListener(transferProgressListener);
    }

    public void write(byte[] bytes,TransferProgressListener transferProgressListener) {
        writeThread.setTransferProgressListener(transferProgressListener);
        writeThread.write(bytes);
    }

    public void read(TransferProgressListener transferProgressListener) {
        setReadProgressListener(transferProgressListener);

    }

    public void cancel() {
        executor.shutdownNow();

    }
}
