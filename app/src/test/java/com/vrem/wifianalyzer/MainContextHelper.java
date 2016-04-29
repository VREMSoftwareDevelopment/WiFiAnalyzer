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
import android.view.LayoutInflater;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.Database;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import java.util.HashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.mock;

public enum MainContextHelper {
    INSTANCE;

    private final Map<Class, Object> saved;
    private final MainContext mainContext;

    MainContextHelper() {
        mainContext = MainContext.INSTANCE;
        saved = new HashMap<>();
    }

    private Object save(Class clazz, Object object) {
        saved.put(clazz, object);
        return mock(clazz);
    }

    public Settings getSettings() {
        Settings result = (Settings) save(Settings.class, mainContext.getSettings());
        mainContext.setSettings(result);
        return result;
    }

    public VendorService getVendorService() {
        VendorService result = (VendorService) save(VendorService.class, mainContext.getVendorService());
        mainContext.setVendorService(result);
        return result;
    }

    public Scanner getScanner() {
        Scanner result = (Scanner) save(Scanner.class, mainContext.getScanner());
        mainContext.setScanner(result);
        return result;
    }

    public LayoutInflater getLayoutInflater() {
        LayoutInflater result = (LayoutInflater) save(LayoutInflater.class, mainContext.getLayoutInflater());
        mainContext.setLayoutInflater(result);
        return result;
    }

    public Database getDatabase() {
        Database result = (Database) save(Database.class, mainContext.getDatabase());
        mainContext.setDatabase(result);
        return result;
    }

    public Resources getResources() {
        Resources result = (Resources) save(Resources.class, mainContext.getResources());
        mainContext.setResources(result);
        return result;
    }

    public Context getContext() {
        Context result = (Context) save(Context.class, mainContext.getContext());
        mainContext.setContext(result);
        return result;
    }

    public Logger getLogger() {
        Logger result = (Logger) save(Logger.class, mainContext.getLogger());
        mainContext.setLogger(result);
        return result;
    }

    public Configuration getConfiguration() {
        Configuration result = (Configuration) save(Configuration.class, mainContext.getConfiguration());
        mainContext.setConfiguration(result);
        return result;
    }

    public void restore() {
        for (Class clazz : saved.keySet()) {
            Object result = saved.get(clazz);
            if (clazz.equals(Settings.class)) {
                mainContext.setSettings((Settings) result);
            } else if (clazz.equals(VendorService.class)) {
                mainContext.setVendorService((VendorService) result);
            } else if (clazz.equals(Scanner.class)) {
                mainContext.setScanner((Scanner) result);
            } else if (clazz.equals(Context.class)) {
                mainContext.setContext((Context) result);
            } else if (clazz.equals(Resources.class)) {
                mainContext.setResources((Resources) result);
            } else if (clazz.equals(LayoutInflater.class)) {
                mainContext.setLayoutInflater((LayoutInflater) result);
            } else if (clazz.equals(Database.class)) {
                mainContext.setDatabase((Database) result);
            } else if (clazz.equals(Logger.class)) {
                mainContext.setLogger((Logger) result);
            } else if (clazz.equals(Configuration.class)) {
                mainContext.setConfiguration((Configuration) result);
            } else {
                throw new IllegalArgumentException(clazz.getName());
            }
        }
        saved.clear();
    }
}
