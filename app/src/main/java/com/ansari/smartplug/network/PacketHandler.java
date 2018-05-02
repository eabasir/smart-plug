package com.ansari.smartplug.network;

public class PacketHandler {

    public static enum PacketDataType
    {
        SRC_Add(2),
        Dest_Add(2),
        CMD(2),
        DeviceDateTime(8),
        Gas(4),
        Hum(4),
        Temp(4),
        Light(1),
        PIR(1);


        PacketDataType (int i)
        {
            this.type = i;
        }

        private int type;

        public int getNumericType()
        {
            return type;
        }
    }


}
