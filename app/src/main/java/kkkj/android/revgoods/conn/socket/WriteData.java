package kkkj.android.revgoods.conn.socket;

import com.xuhao.didi.core.iocore.interfaces.ISendable;


public class WriteData implements ISendable {
    private byte[] body;

    public WriteData(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] parse() {
        byte[] header = new byte[6];
        Integer len = body.length;
        for(int i = 0 ; i<5;i++)
        {
            header[i] = 0x00;
        }
        header[5] = len.byteValue();
        return byteMerger(header,body);
    }
    //System.arraycopy()方法, 将bt1与bt2拼接
    private static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
