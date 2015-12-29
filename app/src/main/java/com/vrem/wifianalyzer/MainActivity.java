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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.Database;
import com.vrem.wifianalyzer.vendor.VendorActivity;
import com.vrem.wifianalyzer.vendor.VendorService;
import com.vrem.wifianalyzer.wifi.Scanner;
import com.vrem.wifianalyzer.wifi.WiFi;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity
        implements OnSharedPreferenceChangeListener, OnNavigationItemSelectedListener {

    private Scanner scanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Settings settings;
    private int themeAppCompatStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = new Settings(this);
        settings.initializeDefaultValues();
        themeAppCompatStyle = settings.themeStyle().themeAppCompatStyle();
        setTheme(themeAppCompatStyle);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.listView);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        listViewAdapter.setExpandableListView(expandableListView);
        expandableListView.setAdapter(listViewAdapter);

        settings.sharedPreferences().registerOnSharedPreferenceChangeListener(this);

        scanner = Scanner.performPeriodicScan(wifi(), new Handler(), listViewAdapter, settings.scanInterval());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @NonNull
    private WiFi wifi() {
        Database database = new Database(this);
        VendorService vendorService = new VendorService(database);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return new WiFi(wifiManager, vendorService, settings.groupBy(), settings.hideWeakSignal());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (themeAppCompatStyle != settings.themeStyle().themeAppCompatStyle()) {
            reloadActivity();
        } else {
            scanner.scanInterval(settings.scanInterval());
            scanner.groupBy(settings.groupBy());
            scanner.hideWeakSignal(settings.hideWeakSignal());
            refresh();
        }
    }

    private void reloadActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        scanner.update();
        swipeRefreshLayout.setRefreshing(false);
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.action_vendors:
                startActivity(new Intent(this, VendorActivity.class));
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
