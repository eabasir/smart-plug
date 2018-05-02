package com.ansari.smartplug.interfaces;

/**
 * Created by Eabasir on 5/24/2016.
 */
public interface OnDeviceDataReadListener {

    public void onDeviceDataRead(float light , float humidity , float temprature , float gas , boolean relay);

}
