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
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Scanner {
    public static final int DELAY_MILLIS = 30000;

    private final WiFi wifi;
    private final Update update;
    private final Handler handler;
    private final DateFormat timeFormat;

    private Scanner(@NonNull WifiManager wifiManager, @NonNull Update update) {
        this.wifi = new WiFi(wifiManager);
        this.update = update;
        this.handler = new Handler();
        this.timeFormat = new SimpleDateFormat("mm:ss.SSS");
    }

    public static Scanner performPeriodicScans(@NonNull WifiManager wifiManager, @NonNull Update update) {
        Scanner scanner = new Scanner(wifiManager, update);
        scanner.update();
        scanner.handler.removeCallbacks(scanner.performPeriodicScan());
        scanner.handler.postDelayed(scanner.performPeriodicScan(), DELAY_MILLIS);
        return scanner;
    }

    private Runnable performPeriodicScan() {
        return new Runnable() {
            @Override
            public void run() {
                Log.i(">>>", "Scanner.performPeriodicScan");
                update();
                handler.removeCallbacks(this);
                handler.postDelayed(this, DELAY_MILLIS);
            }
        };
    }

    public void update() {
        update.update(scan());
    }

    private Info scan() {
        Info results = new Info();
        wifi.enable();
        List<ScanResult> scanResults = wifi.scan();
        if (scanResults != null) {
            for (ScanResult scanResult: scanResults) {
                results.add(Details.make(scanResult));
            }
        }
        return results;
    }

}
