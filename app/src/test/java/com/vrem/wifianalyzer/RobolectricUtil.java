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
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.view.LayoutInflater;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.Database;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.robolectric.Robolectric;

public enum RobolectricUtil {
    INSTANCE;

    private MainActivity mainActivity;
    private Settings settings;
    private Context context;
    private Resources resources;
    private Scanner scanner;
    private Handler handler;
    private VendorService vendorService;
    private WifiManager wifiManager;
    private LayoutInflater layoutInflater;
    private Database database;
    private Logger logger;

    public MainActivity getMainActivity() {
        if (mainActivity == null) {
            mainActivity = Robolectric.setupActivity(MainActivity.class);
            storeMainContext();
        }
        return mainActivity;
    }

    private void storeMainContext() {
        MainContext mainContext = MainContext.INSTANCE;
        settings = mainContext.getSettings();
        context = mainContext.getContext();
        resources = mainContext.getResources();
        scanner = mainContext.getScanner();
        handler = mainContext.getHandler();
        vendorService = mainContext.getVendorService();
        wifiManager = mainContext.getWifiManager();
        layoutInflater = mainContext.getLayoutInflater();
        database = mainContext.getDatabase();
        logger = mainContext.getLogger();
    }

    public void restoreMainContext() {
        MainContext mainContext = MainContext.INSTANCE;
        mainContext.setSettings(settings);
        mainContext.setContext(context);
        mainContext.setResources(resources);
        mainContext.setScanner(scanner);
        mainContext.setHandler(handler);
        mainContext.setVendorService(vendorService);
        mainContext.setWifiManager(wifiManager);
        mainContext.setLayoutInflater(layoutInflater);
        mainContext.setDatabase(database);
        mainContext.setLogger(logger);
    }
}