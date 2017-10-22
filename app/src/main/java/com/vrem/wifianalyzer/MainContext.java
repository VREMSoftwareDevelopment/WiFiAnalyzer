/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.vrem.wifianalyzer.settings.Repository;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.filter.adapter.FilterAdapter;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

public enum MainContext {
    INSTANCE;

    private Settings settings;
    private MainActivity mainActivity;
    private Scanner scanner;
    private VendorService vendorService;
    private Configuration configuration;
    private FilterAdapter filterAdapter;

    public Settings getSettings() {
        return settings;
    }

    void setSettings(Settings settings) {
        this.settings = settings;
    }

    public VendorService getVendorService() {
        return vendorService;
    }

    void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public Scanner getScanner() {
        return scanner;
    }

    void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Context getContext() {
        return mainActivity.getApplicationContext();
    }

    public Resources getResources() {
        return getContext().getResources();
    }

    public LayoutInflater getLayoutInflater() {
        return mainActivity.getLayoutInflater();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public FilterAdapter getFilterAdapter() {
        return filterAdapter;
    }

    void setFilterAdapter(FilterAdapter filterAdapter) {
        this.filterAdapter = filterAdapter;
    }

    void initialize(@NonNull MainActivity mainActivity, boolean largeScreen) {
        Context applicationContext = mainActivity.getApplicationContext();
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        Handler handler = new Handler();
        Settings settings = new Settings(applicationContext, new Repository(applicationContext));
        Configuration configuration = new Configuration(largeScreen);

        setMainActivity(mainActivity);
        setConfiguration(configuration);
        setSettings(settings);
        setVendorService(new VendorService(mainActivity.getResources()));
        setScanner(new Scanner(wifiManager, handler, settings));
        setFilterAdapter(new FilterAdapter(settings));
    }

}
