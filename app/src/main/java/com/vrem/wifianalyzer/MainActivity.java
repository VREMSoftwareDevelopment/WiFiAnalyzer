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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.vendor.Database;
import com.vrem.wifianalyzer.vendor.VendorService;
import com.vrem.wifianalyzer.wifi.GroupBy;
import com.vrem.wifianalyzer.wifi.Scanner;
import com.vrem.wifianalyzer.wifi.WiFi;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {

    private Scanner scanner;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.listView);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        listViewAdapter.setExpandableListView(expandableListView);
        expandableListView.setAdapter(listViewAdapter);

        scanner = Scanner.performPeriodicScan(wifi(preferences), new Handler(), listViewAdapter, scanInterval(preferences));
    }

    private int scanInterval(SharedPreferences preferences) {
        int defaultValue = getResources().getInteger(R.integer.scan_interval_default);
        return preferences.getInt(getString(R.string.scan_interval_key), defaultValue);
    }

    private boolean hideWeakSignal(SharedPreferences preferences) {
        boolean defaultValue = getResources().getBoolean(R.bool.hide_weak_signal_default);
        return preferences.getBoolean(getString(R.string.hide_weak_signal_key), defaultValue);
    }

    @NonNull
    private GroupBy groupBy(SharedPreferences preferences) {
        String defaultValue = getResources().getString(R.string.group_by_default);
        return GroupBy.find(preferences.getString(getString(R.string.group_by_key), defaultValue));
    }

    @NonNull
    private WiFi wifi(SharedPreferences preferences) {
        Database database = new Database(this);
        VendorService vendorService = new VendorService(database);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return new WiFi(wifiManager, vendorService, groupBy(preferences), hideWeakSignal(preferences));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        scanner.scanInterval(scanInterval(sharedPreferences));
        scanner.groupBy(groupBy(sharedPreferences));
        scanner.hideWeakSignal(hideWeakSignal(sharedPreferences));
        refresh();
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        scanner.update();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
