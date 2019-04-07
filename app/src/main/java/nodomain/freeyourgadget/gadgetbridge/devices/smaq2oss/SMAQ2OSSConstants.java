package nodomain.freeyourgadget.gadgetbridge.devices.smaq2oss;

import java.util.UUID;

import static nodomain.freeyourgadget.gadgetbridge.service.btle.AbstractBTLEDeviceSupport.BASE_UUID;


public class SMAQ2OSSConstants {

    public static final UUID UUID_SERVICE_SMAQ2OSS = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
//    public static final UUID UUID_SERVICE_SMAQ2OSS = UUID.fromString(String.format(BASE_UUID, "2a04"));
    public static final UUID UUID_CHARACTERISTIC_WRITE_NORMAL = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static final UUID UUID_CHARACTERISTIC_NOTIFY_NORMAL = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");

    public static final byte CMD_SET_TIME = 0x01;
}
