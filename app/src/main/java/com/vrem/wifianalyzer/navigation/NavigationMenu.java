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
package com.vrem.wifianalyzer.navigation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.WiFiFragment;

public enum NavigationMenu {
    WIFI_LIST(R.mipmap.ic_launcher, R.string.action_wifi_list, new WiFiFragment()),
    VENDOR_LIST(R.drawable.ic_list_grey_500_48dp, R.string.action_vendors, new VendorFragment()),
    SETTINGS(R.drawable.ic_settings_grey_500_48dp, R.string.action_settings, SettingActivity.class);

    private int icon;
    private int title;
    private Fragment fragment;
    private Class<? extends Activity> activity;

    NavigationMenu(int icon, int title, @NonNull Fragment fragment) {
        this.icon = icon;
        this.title = title;
        this.fragment = fragment;
    }

    NavigationMenu(int icon, int title, @NonNull Class<? extends Activity> activity) {
        this.icon = icon;
        this.title = title;
        this.activity = activity;
    }

    static NavigationMenu find(int index) {
        try {
            return values()[index];
        } catch (Exception e) {
            return NavigationMenu.WIFI_LIST;
        }
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public int getTitle() {
        return title;
    }

    int getIcon() {
        return icon;
    }
}
