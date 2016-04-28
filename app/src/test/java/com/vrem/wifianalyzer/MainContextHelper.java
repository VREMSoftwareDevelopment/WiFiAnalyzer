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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.Database;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

public enum MainContextHelper {
    INSTANCE;

    private Settings settings;
    private Context context;
    private Resources resources;
    private Scanner scanner;
    private VendorService vendorService;
    private LayoutInflater layoutInflater;
    private Database database;
    private Logger logger;
    private Configuration configuration;
    private MainContext mainContext;

    MainContextHelper() {
        mainContext = MainContext.INSTANCE;
    }

    public void setSettings(@NonNull Settings settings) {
        this.settings = mainContext.getSettings();
        mainContext.setSettings(settings);
    }

    public void setVendorService(@NonNull VendorService vendorService) {
        this.vendorService = mainContext.getVendorService();
        mainContext.setVendorService(vendorService);
    }

    public void setScanner(@NonNull Scanner scanner) {
        this.scanner = mainContext.getScanner();
        mainContext.setScanner(scanner);
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = mainContext.getLayoutInflater();
        mainContext.setLayoutInflater(layoutInflater);
    }

    public void setDatabase(@NonNull Database database) {
        this.database = mainContext.getDatabase();
        mainContext.setDatabase(database);
    }

    public void setResources(Resources resources) {
        this.resources = mainContext.getResources();
        mainContext.setResources(resources);
    }

    public void setContext(@NonNull Context context) {
        this.context = mainContext.getContext();
        mainContext.setContext(context);
    }

    public void setLogger(@NonNull Logger logger) {
        this.logger = mainContext.getLogger();
        mainContext.setLogger(logger);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = mainContext.getConfiguration();
        mainContext.setConfiguration(configuration);
    }

    public void restore() {
        if (settings != null) {
            mainContext.setSettings(settings);
        }
        if (context != null) {
            mainContext.setContext(context);
        }
        if (resources != null) {
            mainContext.setResources(resources);
        }
        if (scanner != null) {
            mainContext.setScanner(scanner);
        }
        if (vendorService != null) {
            mainContext.setVendorService(vendorService);
        }
        if (layoutInflater != null) {
            mainContext.setLayoutInflater(layoutInflater);
        }
        if (database != null) {
            mainContext.setDatabase(database);
        }
        if (logger != null) {
            mainContext.setLogger(logger);
        }
        if (configuration != null) {
            mainContext.setConfiguration(configuration);
        }
    }
}
