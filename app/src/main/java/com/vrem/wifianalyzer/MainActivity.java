/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.util.ConfigurationUtils;
import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuControl;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;
import com.vrem.wifianalyzer.settings.Repository;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionView;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import java.util.Locale;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity
    implements OnSharedPreferenceChangeListener, OnNavigationItemSelectedListener, NavigationMenuControl {
    private MainReload mainReload;
    private DrawerNavigation drawerNavigation;
    private NavigationMenuView navigationMenuView;
    private NavigationMenu startNavigationMenu;
    private OptionMenu optionMenu;
    private String currentCountryCode;

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale = new Settings(new Repository(newBase)).getLanguageLocale();
        Context context = ConfigurationUtils.createContext(newBase, newLocale);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainContext mainContext = MainContext.INSTANCE;
        mainContext.initialize(this, isLargeScreen());

        Settings settings = mainContext.getSettings();
        settings.initializeDefaultValues();

        setTheme(settings.getThemeStyle().getThemeNoActionBar());

        setWiFiChannelPairs(mainContext);

        mainReload = new MainReload(settings);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settings.registerOnSharedPreferenceChangeListener(this);

        setOptionMenu(new OptionMenu());

        Toolbar toolbar = ActivityUtils.setupToolbar(this);
        drawerNavigation = new DrawerNavigation(this, toolbar);

        startNavigationMenu = settings.getStartMenu();
        setNavigationMenuView(new NavigationMenuView(this, startNavigationMenu));
        onNavigationItemSelected(getCurrentMenuItem());

        ConnectionView connectionView = new ConnectionView(this);
        mainContext.getScannerService().register(connectionView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerNavigation.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerNavigation.onConfigurationChanged(newConfig);
    }

    private void setWiFiChannelPairs(MainContext mainContext) {
        Settings settings = mainContext.getSettings();
        String countryCode = settings.getCountryCode();
        if (!countryCode.equals(currentCountryCode)) {
            Pair<WiFiChannel, WiFiChannel> pair = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairFirst(countryCode);
            mainContext.getConfiguration().setWiFiChannelPair(pair);
            currentCountryCode = countryCode;
        }
    }

    private boolean isLargeScreen() {
        Configuration configuration = getResources().getConfiguration();
        int screenLayoutSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
            screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        MainContext mainContext = MainContext.INSTANCE;
        if (mainReload.shouldReload(mainContext.getSettings())) {
            reloadActivity();
        } else {
            setWiFiChannelPairs(mainContext);
            update();
        }
    }

    public void update() {
        MainContext.INSTANCE.getScannerService().update();
        updateActionBar();
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
        if (!closeDrawer()) {
            if (startNavigationMenu.equals(getCurrentNavigationMenu())) {
                super.onBackPressed();
            } else {
                setCurrentNavigationMenu(startNavigationMenu);
                onNavigationItemSelected(getCurrentMenuItem());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        try {
            closeDrawer();
            NavigationMenu navigationMenu = EnumUtils.find(NavigationMenu.class, menuItem.getItemId(), NavigationMenu.ACCESS_POINTS);
            navigationMenu.activateNavigationMenu(this, menuItem);
        } catch (Exception e) {
            reloadActivity();
        }
        return true;
    }

    private boolean closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        MainContext.INSTANCE.getScannerService().pause();
        updateActionBar();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainContext.INSTANCE.getScannerService().resume();
        updateActionBar();
    }

    @Override
    protected void onStop() {
        MainContext.INSTANCE.getScannerService().setWiFiOnExit();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu.create(this, menu);
        updateActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        optionMenu.select(item);
        updateActionBar();
        return true;
    }

    public void updateActionBar() {
        getCurrentNavigationMenu().activateOptions(this);
    }

    public OptionMenu getOptionMenu() {
        return optionMenu;
    }

    void setOptionMenu(@NonNull OptionMenu optionMenu) {
        this.optionMenu = optionMenu;
    }

    @NonNull
    @Override
    public MenuItem getCurrentMenuItem() {
        return navigationMenuView.getCurrentMenuItem();
    }

    @NonNull
    @Override
    public NavigationMenu getCurrentNavigationMenu() {
        return navigationMenuView.getCurrentNavigationMenu();
    }

    @Override
    public void setCurrentNavigationMenu(@NonNull NavigationMenu navigationMenu) {
        navigationMenuView.setCurrentNavigationMenu(navigationMenu);
    }

    @NonNull
    @Override
    public NavigationView getNavigationView() {
        return navigationMenuView.getNavigationView();
    }

    public NavigationMenuView getNavigationMenuView() {
        return navigationMenuView;
    }

    void setNavigationMenuView(NavigationMenuView navigationMenuView) {
        this.navigationMenuView = navigationMenuView;
    }

    void setDrawerNavigation(DrawerNavigation drawerNavigation) {
        this.drawerNavigation = drawerNavigation;
    }
}
