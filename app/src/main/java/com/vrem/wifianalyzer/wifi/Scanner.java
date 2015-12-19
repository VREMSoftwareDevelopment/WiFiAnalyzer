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

import android.os.Handler;
import android.support.annotation.NonNull;

public class Scanner {
    static final int DELAY_INITIAL = 1;
    static final int DELAY_INTERVAL = 60000 * 5;    // every 5 minutes

    private final WiFi wifi;
    private final Updater updater;
    private PerformPeriodicScan performPeriodicScan;

    private Scanner(@NonNull WiFi wifi, @NonNull Updater updater) {
        this.wifi = wifi;
        this.updater = updater;
    }

    public static Scanner performPeriodicScans(@NonNull WiFi wifi, @NonNull Handler handler, @NonNull Updater updater) {
        Scanner scanner = new Scanner(wifi, updater);
        scanner.performPeriodicScan = new PerformPeriodicScan(scanner, handler);
        handler.postDelayed(scanner.performPeriodicScan, DELAY_INITIAL);
        return scanner;
    }

    public void update() {
        wifi.enable();
        updater.update(wifi.scan());
    }

    PerformPeriodicScan getPerformPeriodicScan() {
        return performPeriodicScan;
    }

    static class PerformPeriodicScan implements Runnable {
        private final Scanner scanner;
        private final Handler handler;

        PerformPeriodicScan(@NonNull Scanner scanner, @NonNull Handler handler) {
            this.scanner = scanner;
            this.handler = handler;
        }

        @Override
        public void run() {
            scanner.update();
            handler.removeCallbacks(this);
            handler.postDelayed(this, DELAY_INTERVAL);
        }
    }

}
