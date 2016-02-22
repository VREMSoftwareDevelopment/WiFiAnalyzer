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

package com.vrem.wifianalyzer.wifi.scanner;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.settings.Settings;

class PeriodicScan implements Runnable {
    static final int DELAY_INITIAL = 1;
    static final int DELAY_INTERVAL = 1000;

    private final MainContext mainContext = MainContext.INSTANCE;
    private final Scanner scanner;

    PeriodicScan(@NonNull Scanner scanner) {
        this.scanner = scanner;
        nextRun(DELAY_INITIAL);
    }

    private void nextRun(int delayInitial) {
        Handler handler = mainContext.getHandler();
        handler.removeCallbacks(this);
        handler.postDelayed(this, delayInitial);
    }

    @Override
    public void run() {
        scanner.update();
        Settings settings = mainContext.getSettings();
        nextRun(settings.getScanInterval() * DELAY_INTERVAL);
    }
}
