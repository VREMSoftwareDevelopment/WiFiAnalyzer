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
package com.vrem.wifianalyzer.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.Database;
import com.vrem.wifianalyzer.vendor.VendorService;

public class WiFiFragment extends Fragment {

    private Scanner scanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Settings settings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        settings = new Settings(activity);

        View view = inflater.inflate(R.layout.main_content, container, false);
        View headerView = view.findViewById(R.id.contentHeader);

        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.listView);
        WiFiListViewAdapter wifiListViewAdapter = new WiFiListViewAdapter(headerView, activity);
        wifiListViewAdapter.setExpandableListView(expandableListView);
        expandableListView.setAdapter(wifiListViewAdapter);

        scanner = Scanner.performPeriodicScan(wifi(activity), new Handler(), wifiListViewAdapter, settings.scanInterval());
        return view;
    }

    @NonNull
    private WiFi wifi(FragmentActivity activity) {
        Database database = new Database(activity);
        VendorService vendorService = new VendorService(database);
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        return new WiFi(wifiManager, vendorService, settings.groupBy(), settings.hideWeakSignal());
    }


    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        scanner.update();
        swipeRefreshLayout.setRefreshing(false);
    }

    public Scanner getScanner() {
        return scanner;
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }

}
