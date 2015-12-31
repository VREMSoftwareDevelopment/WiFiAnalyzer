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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.Scanner;
import com.vrem.wifianalyzer.wifi.WiFiFragment;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity
        implements OnSharedPreferenceChangeListener, OnNavigationItemSelectedListener {

    private WiFiFragment wiFiFragment;
    private Settings settings;
    private int themeAppCompatStyle;
    private VendorFragment vendorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = new Settings(this);
        settings.initializeDefaultValues();
        themeAppCompatStyle = settings.themeStyle().themeAppCompatStyle();
        setTheme(themeAppCompatStyle);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        wiFiFragment = new WiFiFragment();
        vendorFragment = new VendorFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, wiFiFragment).commit();

        settings.sharedPreferences().registerOnSharedPreferenceChangeListener(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                wiFiFragment.refresh();
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
            Scanner scanner = wiFiFragment.getScanner();
            scanner.scanInterval(settings.scanInterval());
            scanner.groupBy(settings.groupBy());
            scanner.hideWeakSignal(settings.hideWeakSignal());
            wiFiFragment.refresh();
        }
    }

    private void reloadActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
            case R.id.action_wifi_list:
                setTitle(R.string.action_wifi_list);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, wiFiFragment).commit();
                break;
            case R.id.action_vendors:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, vendorFragment).commit();
                setTitle(R.string.action_vendors);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
