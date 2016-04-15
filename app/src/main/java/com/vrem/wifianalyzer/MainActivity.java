/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.vendor.model.Database;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.ConnectionView;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;
import com.vrem.wifianalyzer.wifi.scanner.Transformer;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener, OnNavigationItemSelectedListener {
    private static final String WI_FI_ANALYZER_BETA = "WiFi Analyzer BETA";

    private ThemeStyle currentThemeStyle;
    private NavigationMenuView navigationMenuView;
    private boolean subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMainContext(this);

        MainContext.INSTANCE.getSettings().initializeDefaultValues();
        currentThemeStyle = MainContext.INSTANCE.getSettings().getThemeStyle();
        setTheme(currentThemeStyle.themeAppCompatStyle());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainContext.INSTANCE.getSettings().registerOnSharedPreferenceChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new WiFiBandToggle());
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationMenuView = new NavigationMenuView(this);
        onNavigationItemSelected(navigationMenuView.defaultMenuItem());

        new ConnectionView(this, MainContext.INSTANCE.getScanner());
    }

    private void initializeMainContext(@NonNull Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Handler handler = new Handler();
        Settings settings = new Settings();
        Configuration configuration = getConfiguration(context);

        MainContext.INSTANCE.setConfiguration(configuration);
        MainContext.INSTANCE.setContext(context);
        MainContext.INSTANCE.setResources(context.getResources());
        MainContext.INSTANCE.setDatabase(new Database(context));
        MainContext.INSTANCE.setSettings(settings);
        MainContext.INSTANCE.setVendorService(new VendorService());
        MainContext.INSTANCE.setLayoutInflater((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        MainContext.INSTANCE.setLogger(new Logger());

        MainContext.INSTANCE.setScanner(new Scanner(wifiManager, handler, settings, new Transformer(configuration)));
    }

    private Configuration getConfiguration(@NonNull Context context) {
        boolean isDevelopmentMode = WI_FI_ANALYZER_BETA.equals(context.getString(R.string.app_name));
        Locale locale = isDevelopmentMode ? WiFiChannelCountry.WORLD_LOCALE : context.getResources().getConfiguration().locale;
        return new Configuration(
                locale,
                isLargeScreenLayout(),
                WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs(locale).get(0),
                isDevelopmentMode);
    }

    private boolean isLargeScreenLayout() {
        int screenLayoutSize = getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayoutSize == android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE || screenLayoutSize == android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isThemeChanged()) {
            reloadActivity();
        } else {
            MainContext.INSTANCE.getScanner().update();
            updateSubTitle();
        }
    }

    private boolean isThemeChanged() {
        ThemeStyle settingThemeStyle = MainContext.INSTANCE.getSettings().getThemeStyle();
        if (!currentThemeStyle.equals(settingThemeStyle)) {
            currentThemeStyle = settingThemeStyle;
            return true;
        }
        return false;
    }

    private void reloadActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        NavigationMenu item = navigationMenuView.selectedMenuItem(menuItem.getItemId());
        Fragment fragment = item.getFragment();
        if (fragment == null) {
            startActivity(new Intent(this, item.getActivity()));
        } else {
            subTitle = item.isSubTitle();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item.getFragment()).commit();
            setTitle(menuItem.getTitle());
            updateSubTitle();
        }
        return true;
    }

    @Override
    protected void onPause() {
        MainContext.INSTANCE.getScanner().pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainContext.INSTANCE.getScanner().resume();
    }

    private void updateSubTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subTitle ? MainContext.INSTANCE.getSettings().getWiFiBand().getBand() : StringUtils.EMPTY);
        }
    }

    public NavigationMenuView getNavigationMenuView() {
        return navigationMenuView;
    }

    protected ThemeStyle getCurrentThemeStyle() {
        return currentThemeStyle;
    }

    protected void wiFiBandToggle() {
        MainContext.INSTANCE.getSettings().toggleWiFiBand();
    }

    private class WiFiBandToggle implements OnClickListener {
        @Override
        public void onClick(View view) {
            wiFiBandToggle();
        }
    }
}
