package kkkj.android.revgoods.conn.socket;

import com.xuhao.didi.core.protocol.IReaderProtocol;

import java.nio.ByteOrder;

public class ModbusProtocol implements IReaderProtocol {
    @Override
    public int getHeaderLength() {
        return 6;
    }

    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        if (header == null || header.length < getHeaderLength()) {
            return 0;
        }
        return header[5];
    }
}
