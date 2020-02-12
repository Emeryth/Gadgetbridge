package nodomain.freeyourgadget.gadgetbridge.service.devices.smaq2oss;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.net.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.nio.ByteBuffer;

import nodomain.freeyourgadget.gadgetbridge.deviceevents.GBDeviceEventMusicControl;
import nodomain.freeyourgadget.gadgetbridge.devices.smaq2oss.SMAQ2OSSConstants;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.model.Alarm;
import nodomain.freeyourgadget.gadgetbridge.model.CalendarEventSpec;
import nodomain.freeyourgadget.gadgetbridge.model.CallSpec;
import nodomain.freeyourgadget.gadgetbridge.model.CannedMessagesSpec;
import nodomain.freeyourgadget.gadgetbridge.model.MusicSpec;
import nodomain.freeyourgadget.gadgetbridge.model.MusicStateSpec;
import nodomain.freeyourgadget.gadgetbridge.model.NotificationSpec;
import nodomain.freeyourgadget.gadgetbridge.model.Weather;
import nodomain.freeyourgadget.gadgetbridge.model.WeatherSpec;
import nodomain.freeyourgadget.gadgetbridge.service.btle.AbstractBTLEDeviceSupport;
import nodomain.freeyourgadget.gadgetbridge.service.btle.GattService;
import nodomain.freeyourgadget.gadgetbridge.service.btle.TransactionBuilder;
import nodomain.freeyourgadget.gadgetbridge.service.btle.actions.SetDeviceStateAction;
import nodomain.freeyourgadget.gadgetbridge.service.devices.no1f1.No1F1Support;
import nodomain.freeyourgadget.gadgetbridge.SMAQ2OSSProtos;

public class SMAQ2OSSSupport extends AbstractBTLEDeviceSupport {
    private static final Logger LOG = LoggerFactory.getLogger(SMAQ2OSSSupport.class);

    public BluetoothGattCharacteristic normalWriteCharacteristic = null;

    public SMAQ2OSSSupport() {
        super(LOG);
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ACCESS);
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ATTRIBUTE);
        addSupportedService(SMAQ2OSSConstants.UUID_SERVICE_SMAQ2OSS);
    }

    @Override
    protected TransactionBuilder initializeDevice(TransactionBuilder builder) {
        normalWriteCharacteristic = getCharacteristic(SMAQ2OSSConstants.UUID_CHARACTERISTIC_WRITE_NORMAL);

        builder.add(new SetDeviceStateAction(getDevice(), GBDevice.State.INITIALIZING, getContext()));

        setTime(builder)
                .setInitialized(builder);

        getDevice().setFirmwareVersion("N/A");
        getDevice().setFirmwareVersion2("N/A");

        builder.notify(getCharacteristic(SMAQ2OSSConstants.UUID_CHARACTERISTIC_NOTIFY_NORMAL), true);

        return builder;
    }

    @Override
    public boolean onCharacteristicChanged(BluetoothGatt gatt,
                                           BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        UUID characteristicUUID = characteristic.getUuid();
        if (SMAQ2OSSConstants.UUID_CHARACTERISTIC_NOTIFY_NORMAL.equals(characteristicUUID)) {
            handleDeviceEvent(characteristic.getValue());
        }
        return true;
    }

    private void handleDeviceEvent(byte[] value){
        if (value == null || value.length == 0) {
            return;
        }

        switch (value[0]) {
            case SMAQ2OSSConstants.MSG_MUSIC_EVENT:
                LOG.info("got music control");
                handleMusicEvent(value[1]);
                break;

        }

    }

    private void handleMusicEvent(byte value){
        GBDeviceEventMusicControl deviceEventMusicControl = new GBDeviceEventMusicControl();

        switch (value){
            case SMAQ2OSSConstants.EVT_PLAY_PAUSE:
                deviceEventMusicControl.event = GBDeviceEventMusicControl.Event.PLAYPAUSE;
                break;
            case SMAQ2OSSConstants.EVT_FWD:
                deviceEventMusicControl.event = GBDeviceEventMusicControl.Event.NEXT;
                break;
            case SMAQ2OSSConstants.EVT_REV:
                deviceEventMusicControl.event = GBDeviceEventMusicControl.Event.PREVIOUS;
                break;
            case SMAQ2OSSConstants.EVT_VOL_UP:
                deviceEventMusicControl.event = GBDeviceEventMusicControl.Event.VOLUMEUP;
                break;
            case SMAQ2OSSConstants.EVT_VOL_DOWN:
                deviceEventMusicControl.event = GBDeviceEventMusicControl.Event.VOLUMEDOWN;
                break;
        }
        evaluateGBDeviceEvent(deviceEventMusicControl);

    }

    @Override
    public boolean useAutoConnect() {
        return true;
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {

    }

    @Override
    public void onDeleteNotification(int id) {

    }

    @Override
    public void onSetTime() {
        try {
            TransactionBuilder builder = performInitialized("time");
            setTime(builder);
            performConnected(builder.getTransaction());
        } catch(IOException e) {
        }
    }

    @Override
    public void onSetAlarms(ArrayList<? extends Alarm> alarms) {

    }

    @Override
    public void onSetCallState(CallSpec callSpec) {

    }

    @Override
    public void onSetCannedMessages(CannedMessagesSpec cannedMessagesSpec) {

    }

    @Override
    public void onSetMusicState(MusicStateSpec stateSpec) {

    }

    @Override
    public void onSetMusicInfo(MusicSpec musicSpec) {

    }

    @Override
    public void onEnableRealtimeSteps(boolean enable) {

    }

    @Override
    public void onInstallApp(Uri uri) {

    }

    @Override
    public void onAppInfoReq() {

    }

    @Override
    public void onAppStart(UUID uuid, boolean start) {

    }

    @Override
    public void onAppDelete(UUID uuid) {

    }

    @Override
    public void onAppConfiguration(UUID appUuid, String config, Integer id) {

    }

    @Override
    public void onAppReorder(UUID[] uuids) {

    }

    @Override
    public void onFetchRecordedData(int dataTypes) {

    }

    @Override
    public void onReset(int flags) {
//        try {
//            getQueue().clear();
//
//            TransactionBuilder builder = performInitialized("reboot");
//            builder.write(normalWriteCharacteristic, new byte[] {
//                    SMAQ2OSSConstants.CMD_ID_DEVICE_RESTART, SMAQ2OSSConstants.CMD_KEY_REBOOT
//            });
//            performConnected(builder.getTransaction());
//        } catch(Exception e) {
//        }
    }

    @Override
    public void onHeartRateTest() {

    }

    @Override
    public void onEnableRealtimeHeartRateMeasurement(boolean enable) {

    }

    @Override
    public void onFindDevice(boolean start) {

    }

    @Override
    public void onSetConstantVibration(int integer) {

    }

    @Override
    public void onScreenshotReq() {

    }

    @Override
    public void onEnableHeartRateSleepSupport(boolean enable) {

    }

    @Override
    public void onSetHeartRateMeasurementInterval(int seconds) {

    }

    @Override
    public void onAddCalendarEvent(CalendarEventSpec calendarEventSpec) {

    }

    @Override
    public void onDeleteCalendarEvent(byte type, long id) {

    }

    @Override
    public void onSendConfiguration(String config) {

    }

    @Override
    public void onReadConfiguration(String config) {

    }

    @Override
    public void onTestNewFunction() {

    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {
        try {
            TransactionBuilder builder;
            builder = performInitialized("Sending current weather");

            byte[] data=ByteBuffer.allocate(6).array();
            data[0] = SMAQ2OSSConstants.MSG_SET_WEATHER;
            data[1] = Weather.mapToPebbleCondition( weatherSpec.currentConditionCode);
            data[2] = (byte) (weatherSpec.currentTemp-273);
            data[3] = (byte) (weatherSpec.todayMinTemp-273);
            data[4] = (byte) (weatherSpec.todayMaxTemp-273);
            data[5] = (byte) weatherSpec.currentHumidity;


            builder.write(normalWriteCharacteristic, data);
            builder.queue(getQueue());
        } catch (Exception ex) {
            LOG.error("Error sending current weather", ex);
        }
    }

    private void setInitialized(TransactionBuilder builder) {
        builder.add(new SetDeviceStateAction(getDevice(), GBDevice.State.INITIALIZED, getContext()));
    }

    SMAQ2OSSSupport setTime(TransactionBuilder builder) {
        Calendar c = GregorianCalendar.getInstance();

        long offset = (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET));
        long ts =  (c.getTimeInMillis()+ offset)/1000 ;
//        byte[] data = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN).put(SMAQ2OSSConstants.MSG_SET_TIME).putInt((int) (ts & 0xffffffffL)).array();

        SMAQ2OSSProtos.SetTime.Builder settime = SMAQ2OSSProtos.SetTime.newBuilder();
        settime.setTimestamp((int) ts);

        byte[] message=settime.build().toByteArray();
        ByteBuffer buf=ByteBuffer.allocate(message.length+1);
        buf.put(SMAQ2OSSConstants.MSG_SET_TIME);
        buf.put(message);
        builder.write(normalWriteCharacteristic,buf.array());
        return this;
    }

}
