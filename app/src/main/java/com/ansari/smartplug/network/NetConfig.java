package com.ansari.smartplug.network;

/**
 * Created by Eabasir on 3/30/2016.
 */
public class  NetConfig {


    public final static String DEVICE_IP = "192.168.11.254";
    public final static int DEVICE_PORT = 3000;


    // first 4 bytes (00000000) SRC and DEST address
    // next 1 bytes for command type (Read: R = 52 and Write: W = 57)
    // next 2 byte for start of data address e.g: 2 for device date time => 0200

    // DeviceSettings => 1 (0100)
    // DeviceTimeDate => 2(0200)
    // Dev_Data => 3 (0300)
    // Dev_Sensor[0] => 4 (0400)
    // Dev_Sensor[1] => 5 (0500)
    // Dev_Sensor[2] => 6 (0600)
    // Dev_Sensor[3] => 7 (0700)
    // Dev_Relay[0] => 8 (0800)
    // Dev_Relay[1] => 9 (09 00)

    public final static String ADD_DEVICE_SETTING = "00000000520100";
    public final static String ADD_DEVICE_DATA_READ = "00000000520300";
    public final static String ADD_DEVICE_DATE_TIME_READ = "00000000520200";
    public final static String ADD_DEVICE_DATE_TIME_WRITE = "00000000570200";
    public final static String ADD_RELAY_WRITE = "00000000570800";
    public final static String ADD_RELAY_READ = "00000000520800";


}
