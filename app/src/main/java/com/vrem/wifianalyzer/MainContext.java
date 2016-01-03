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

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.Database;
import com.vrem.wifianalyzer.vendor.VendorService;
import com.vrem.wifianalyzer.wifi.Scanner;

public enum MainContext {
    INSTANCE;

    private Settings settings;
    private Scanner scanner;
    private Handler handler;
    private VendorService vendorService;
    private WifiManager wifiManager;
    private LayoutInflater layoutInflater;
    private Database database;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(@NonNull Settings settings) {
        this.settings = settings;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(@NonNull Handler handler) {
        this.handler = handler;
    }

    public VendorService getVendorService() {
        return vendorService;
    }

    public void setVendorService(@NonNull VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void setScanner(@NonNull Scanner scanner) {
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setWifiManager(@NonNull WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setDatabase(@NonNull Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
