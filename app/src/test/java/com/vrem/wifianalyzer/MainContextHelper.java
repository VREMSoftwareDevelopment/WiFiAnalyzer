/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

    public MainActivity getMainActivity() {
        MainActivity result = (MainActivity) save(MainActivity.class, mainContext.getMainActivity());
        mainContext.setMainActivity(result);
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
            } else if (clazz.equals(MainActivity.class)) {
                mainContext.setMainActivity((MainActivity) result);
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
