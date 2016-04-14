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

import com.vrem.wifianalyzer.settings.Settings;

public class PeriodicScan implements Runnable {
    protected static final int DELAY_INITIAL = 1;
    protected static final int DELAY_INTERVAL = 1000;

    private final Scanner scanner;
    private final Handler handler;
    private final Settings settings;
    private boolean running;

    public PeriodicScan(@NonNull Scanner scanner, @NonNull Handler handler, @NonNull Settings settings) {
        this.scanner = scanner;
        this.handler = handler;
        this.settings = settings;
        start();
    }

    public void stop() {
        handler.removeCallbacks(this);
        running = false;
    }

    public void start() {
        nextRun(DELAY_INITIAL);
    }

    private void nextRun(int delayInitial) {
        stop();
        handler.postDelayed(this, delayInitial);
        running = true;
    }

    @Override
    public void run() {
        scanner.update();
        nextRun(settings.getScanInterval() * DELAY_INTERVAL);
    }

    public boolean isRunning() {
        return running;
    }
}
