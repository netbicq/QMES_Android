package kkkj.android.revgoods.relay.wifi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * WiFi继电器指令集
 */
public class Order {
    public static byte[] TURN_ON_1 = {0x01,0x05,0x00,0x00, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_2 = {0x01,0x05,0x00,0x01, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_3 = {0x01,0x05,0x00,0x02, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_4 = {0x01,0x05,0x00,0x03, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_5 = {0x01,0x05,0x00,0x04, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_6 = {0x01,0x05,0x00,0x05, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_7 = {0x01,0x05,0x00,0x06, (byte) 0xFF,0x00};
    public static byte[] TURN_ON_8 = {0x01,0x05,0x00,0x07, (byte) 0xFF,0x00};


    public static byte[] TURN_OFF_1 = {0x01,0x05,0x00,0x00 ,0x00,0x00};
    public static byte[] TURN_OFF_2 = {0x01,0x05,0x00,0x01 ,0x00,0x00};
    public static byte[] TURN_OFF_3 = {0x01,0x05,0x00,0x02 ,0x00,0x00};
    public static byte[] TURN_OFF_4 = {0x01,0x05,0x00,0x03 ,0x00,0x00};
    public static byte[] TURN_OFF_5 = {0x01,0x05,0x00,0x04 ,0x00,0x00};
    public static byte[] TURN_OFF_6 = {0x01,0x05,0x00,0x05 ,0x00,0x00};
    public static byte[] TURN_OFF_7 = {0x01,0x05,0x00,0x06 ,0x00,0x00};
    public static byte[] TURN_OFF_8 = {0x01,0x05,0x00,0x07 ,0x00,0x00};

    public static byte[] TURN_ON_ALL = {0x01,0x0F,0x00,0x00,0x00,0x10,0x02, (byte) 0xFF,0x00};

    public static byte[] TURN_OFF_ALL = {0x01,0x0F,0x00,0x00,0x00,0x10,0x02,0x00,0x00};

    public static byte[] GET_STATE = {0x01,0x01,0x00,0x00,0x00,0x10};

    public static List<byte[]> turnOn = new ArrayList<>();
    public static List<byte[]> turnOff = new ArrayList<>();

    public static List<byte[]> getTurnOn() {
        if (turnOn.size() > 0) {
            return turnOn;
        } else {
            turnOn.add(TURN_ON_1);
            turnOn.add(TURN_ON_2);
            turnOn.add(TURN_ON_3);
            turnOn.add(TURN_ON_4);
            turnOn.add(TURN_ON_5);
            turnOn.add(TURN_ON_6);
            turnOn.add(TURN_ON_7);
            turnOn.add(TURN_ON_8);
            return turnOn;
        }

    }

    public static List<byte[]> getTurnOff() {
        if (turnOff.size() > 0) {
            return turnOff;
        } else {
            turnOff.add(TURN_OFF_1);
            turnOff.add(TURN_OFF_2);
            turnOff.add(TURN_OFF_3);
            turnOff.add(TURN_OFF_4);
            turnOff.add(TURN_OFF_5);
            turnOff.add(TURN_OFF_6);
            turnOff.add(TURN_OFF_7);
            turnOff.add(TURN_OFF_8);
            return turnOff;
        }

    }
}
