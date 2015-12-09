package com.vrem.wifianalyzer;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 60000;

    private ListView listView;
    private WifiManager wifiManager;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = getWifiManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ListViewAdapter(this, scanResults()));
        listView.setOnItemClickListener(makeListViewOnItemClickListener());

        handler = new Handler();
        handler.postDelayed(updateListView(), DELAY_MILLIS);
    }

    private OnItemClickListener makeListViewOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult itemValue = (ScanResult) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "SSID: " + itemValue.SSID +
                        "\nBSSID: " + itemValue.BSSID +
                        "\nLEVEL: " + itemValue.level +
                        "\nFREQUENCY: " + itemValue.frequency +
                        "\n" + itemValue.capabilities
                        , Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private WifiManager getWifiManager() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "WiFi is disabled! Making it enabled...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        return wifiManager;
    }

    private List<ScanResult> scanResults() {
        Toast.makeText(this, "Scanning WiFi ... ", Toast.LENGTH_SHORT).show();

        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();
        if (results == null) {
            return new ArrayList<>();
        }
        Collections.sort(results, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                int result = rhs.level - lhs.level;
                if (result == 0) {
                    result = rhs.SSID.compareTo(lhs.SSID);
                    if (result == 0) {
                        result = rhs.BSSID.compareTo(lhs.BSSID);
                    }
                }
                return result;
            }
        });
        return results;
    }

    private Runnable updateListView() {
        return new Runnable() {
            @Override
            public void run() {
                ListViewAdapter listViewAdapter = (ListViewAdapter) listView.getAdapter();
                listViewAdapter.clear();
                listViewAdapter.addAll(scanResults());
                listViewAdapter.notifyDataSetChanged();
                handler.postDelayed(updateListView(), DELAY_MILLIS);
            }
        };
    }
}
