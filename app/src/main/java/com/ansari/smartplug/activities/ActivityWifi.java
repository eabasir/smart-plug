package com.ansari.smartplug.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ansari.smartplug.R;
import com.ansari.smartplug.adapter.AdapterWifi;

import java.util.ArrayList;
import java.util.List;


public class ActivityWifi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout mDrawerLayout;
    ListView lvWifi;
    LinearLayout layEnableWifi;
    Switch swWifi;

    List<ScanResult> wifiList;

    WifiManager wifi;
    String PreSSIDName = "";
    ProgressDialog prgSerach = null;
    ProgressDialog prgConnect = null;

    final String Device_SSID = "HTC";
    final String Device_Pass = "09127025563";


    @Override
    protected void onResume() {
        super.onResume();
        App.CurrentActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        App.CurrentActivity = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        layEnableWifi = (LinearLayout) findViewById(R.id.layEnableWifi);
//        lvWifi = (ListView) findViewById(R.id.lvWifi);
//        swWifi = (Switch) findViewById(R.id.swWifi);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(ActivityWifi.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        swWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    wifi.setWifiEnabled(true);
                    RefreshList();
                }
            }
        });

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!wifi.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            layEnableWifi.setVisibility(View.VISIBLE);

        } else {
            lvWifi.setVisibility(View.VISIBLE);
            RefreshList();
        }


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if (lvWifi.getVisibility() == View.VISIBLE) {

                    wifiList = wifi.getScanResults();
                    ArrayList<AdapterWifi.Data> lstWifi = new ArrayList<>();

                    String CurrentSSIDName = getCurrentSsid(App.context);

                    if (wifiList.size() > 0) {
                        for (int i = 0; i < wifiList.size(); i++) {
                            String ssid = wifiList.get(i).SSID;
                            if (!TextUtils.isEmpty(ssid)) {

                                boolean state = ssid.equals(CurrentSSIDName) ? true : false;

                                AdapterWifi.Data data = new AdapterWifi.Data();

                                data.Name = ssid;
                                data.State = state;
                                lstWifi.add(data);

                            }
                        }

                        lvWifi.setAdapter(new AdapterWifi(App.CurrentActivity, R.layout.list_row, lstWifi));


                        prgSerach.dismiss();

                    }
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

                    layEnableWifi.setVisibility(View.GONE);
                    lvWifi.setVisibility(View.VISIBLE);

                    if (prgConnect != null)
                        prgConnect.dismiss();
                    if (getCurrentSsid(App.context).equals(Device_SSID)) {
                        Toast.makeText(ActivityWifi.this, "Connected", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        lvWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AdapterWifi.Data data = (AdapterWifi.Data) parent.getItemAtPosition(position);

                TextView txtConnecting = (TextView) view.findViewById(R.id.txtConnecting);
                txtConnecting.setVisibility(View.VISIBLE);

                prgConnect = ProgressDialog.show(ActivityWifi.this, "Connecting to Device", "Please Wait...", true);

                new AsyncChnageWifi().execute(data.Name, null, null);

            }
        });




    }

    void RefreshList() {
        wifi.startScan();

        prgSerach = ProgressDialog.show(this, "Searching for Device", "Please Wait...", true);
    }

    @SuppressWarnings("deprecation")
    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
            return ssid.replace("\"", "");
        } else
            return "";

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_relay_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_set) {
            if (lvWifi.getVisibility() == View.VISIBLE)
                RefreshList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }


    private class AsyncChnageWifi extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... ssid) {

            String networkSSID = ssid[0];
            String networkPass = Device_Pass;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            int netId = wifi.addNetwork(conf);
            List<WifiConfiguration> list = wifi.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifi.disconnect();
                    wifi.enableNetwork(netId, true);
                    wifi.reconnect();

                    break;
                }
            }

           /* WifiConfiguration wificonfiguration = new WifiConfiguration();
            StringBuffer stringbuffer = new StringBuffer("\"");
            stringbuffer.append((new StringBuilder(String.valueOf(networkSSID))).append("\"").toString());
            wificonfiguration.SSID = stringbuffer.toString();
            wificonfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wificonfiguration.allowedAuthAlgorithms.set(0);
            wificonfiguration.status = 2;
            wificonfiguration.preSharedKey = "\"" + networkPass + "\"";

            int networkId = wifi.addNetwork(wificonfiguration);

            if (networkId > -1) {

                boolean status = wifi.enableNetwork(networkId, true);

            }
*/


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }

    }
}
