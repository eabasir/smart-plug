package com.ansari.smartplug.network;

import android.os.AsyncTask;

import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnDeviceDateTimeWriteListener;
import com.ansari.smartplug.interfaces.OnRelayConfigWriteListener;
import com.ansari.smartplug.struct.DeviceData;
import com.ansari.smartplug.struct.DeviceDateTime;
import com.ansari.smartplug.struct.Relay;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SearchAsyncClient extends AsyncTask<Void, Void, byte[]> {

    String dstAddress;
    int dstPort;
    byte[] dtsData;
    String job = "";


    public SearchAsyncClient(String addr, int port, String job, byte[] data) {
        this.dstAddress = addr;
        this.dstPort = port;
        this.dtsData = data;
        this.job = job;

    }

    @Override
    protected byte[] doInBackground(Void... arg0) {

        Socket socket = null;
        OutputStream out = null;
        DataOutputStream dos = null;
        BufferedInputStream input = null;
        byte[] recData = new byte[1024];

        try {
            socket = new Socket(dstAddress, dstPort);
            socket.setSoTimeout(10000);

            out = socket.getOutputStream();
            dos = new DataOutputStream(out);


            dos.write(dtsData, 0, dtsData.length);


            input = new BufferedInputStream(socket.getInputStream());
            input.read(recData);

			/*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

			int bytesRead;
			InputStream inputStream = socket.getInputStream();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}*/

            return recData;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }

                if (dos != null) {
                    dos.flush();
                    dos.close();
                }


                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);

        if (this.job.equals(NetConfig.ADD_DEVICE_DATE_TIME_WRITE)) {

                for (OnDeviceDateTimeWriteListener onDeviceDateTimeWriteListener : App
                        .getInstance().getUIListeners(
                                OnDeviceDateTimeWriteListener.class))
                    onDeviceDateTimeWriteListener.onDeviceDateTimeWrite();


            return;

        }
        if (this.job.equals(NetConfig.ADD_DEVICE_DATE_TIME_READ)) {
            new DeviceDateTime(result);
            return;

        }
        if (this.job.equals(NetConfig.ADD_RELAY_READ)) {
            new Relay(result);
            return;
        }
        if (this.job.equals(NetConfig.ADD_RELAY_WRITE)) {
            for (OnRelayConfigWriteListener onRelayConfigWriteListener : App
                    .getInstance().getUIListeners(
                            OnRelayConfigWriteListener.class))
                onRelayConfigWriteListener.onRelayConfigWrite();

            return;
        }

        if (this.job.equals(NetConfig.ADD_DEVICE_DATA_READ)) {
            new DeviceData(result);

            return;
        }

    }

}
