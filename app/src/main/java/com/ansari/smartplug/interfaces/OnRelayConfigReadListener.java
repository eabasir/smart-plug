package com.ansari.smartplug.interfaces;

import java.util.Date;

/**
 * Created by Eabasir on 5/24/2016.
 */
public interface OnRelayConfigReadListener {

    public void onRelayConfigRead(Date startDate, Date finishDate, String repType);

}
