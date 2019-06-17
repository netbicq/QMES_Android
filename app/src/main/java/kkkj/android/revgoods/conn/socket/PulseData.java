package kkkj.android.revgoods.conn.socket;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PulseData implements IPulseSendable {
    @Override
    public byte[] parse() {
        byte[] body = {0x00, 0x00 ,0x00, 0x00 , 0x00 , 0x06 , 0x00 , 0x02 , 0x00 , 0x00 , 0x00 , 0x01};
        return body;
    }
}
