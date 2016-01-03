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

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.Database;
import com.vrem.wifianalyzer.vendor.VendorService;

public class WiFiFragment extends Fragment {

    private MainContext mainContext = MainContext.INSTANCE;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        View view = inflater.inflate(R.layout.main_content, container, false);
        View headerView = view.findViewById(R.id.contentHeader);

        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        WiFiListViewAdapter wifiListViewAdapter = new WiFiListViewAdapter(headerView, activity);

        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.listView);
        expandableListView.setAdapter(wifiListViewAdapter);

        return view;
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        mainContext.getScanner().update();
        swipeRefreshLayout.setRefreshing(false);
    }


    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }

}
