/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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
        listView.setAdapter(new ListViewAdapter(this, wifiScan()));
        listView.setOnItemClickListener(makeListViewOnItemClickListener());

        handler = new Handler();
        handler.postDelayed(updateListView(), DELAY_MILLIS);
    }

    private OnItemClickListener makeListViewOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),
                    listView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
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
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private WifiManager getWifiManager() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "WiFi is disabled! Making it enabled...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        return wifiManager;
    }

    private List<WifiInfo> wifiScan() {
        List<WifiInfo> results = new ArrayList<>();
        Toast.makeText(this, "Scanning WiFi ... ", Toast.LENGTH_SHORT).show();

        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (scanResults != null) {
            for (ScanResult scanResult: scanResults) {
                results.add(WifiInfo.make(scanResult));
            }
            Collections.sort(results);
        }
        return results;
    }

    private Runnable updateListView() {
        return new Runnable() {
            @Override
            public void run() {
                ListViewAdapter listViewAdapter = (ListViewAdapter) listView.getAdapter();
                listViewAdapter.clear();
                listViewAdapter.addAll(wifiScan());
                listViewAdapter.notifyDataSetChanged();
                handler.postDelayed(updateListView(), DELAY_MILLIS);
            }
        };
    }
}
