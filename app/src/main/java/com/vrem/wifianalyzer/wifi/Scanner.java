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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.vrem.wifianalyzer.ListViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scanner {
    public static final int DELAY_MILLIS = 60000;

    private final ListView listView;
    private final Handler handler;
    private final WiFi wifi;

    private Scanner(@NonNull WifiManager wifiManager, @NonNull ListView listView) {
        this.wifi = new WiFi(wifiManager);
        this.listView = listView;
        this.handler = new Handler();
    }

    public static Scanner performPeriodicScans(@NonNull WifiManager wifiManager, @NonNull ListView listView) {
        Scanner scanner = new Scanner(wifiManager, listView);
        scanner.update();
        scanner.handler.postDelayed(scanner.performPeriodicScan(), DELAY_MILLIS);
        return scanner;
    }

    private Runnable performPeriodicScan() {
        return new Runnable() {
            @Override
            public void run() {
                update();
                handler.postDelayed(performPeriodicScan(), DELAY_MILLIS);
            }
        };
    }

    public void update() {
        ListViewAdapter listViewAdapter = (ListViewAdapter) listView.getAdapter();
        listViewAdapter.clear();
        listViewAdapter.addAll(scan());
        listViewAdapter.notifyDataSetChanged();
    }

    private List<Details> scan() {
        List<Details> results = new ArrayList<>();
        wifi.enable();
        List<ScanResult> scanResults = wifi.scan();
        if (scanResults != null) {
            for (ScanResult scanResult: scanResults) {
                results.add(Details.make(scanResult));
            }
            Collections.sort(results);
        }
        return results;
    }

}
