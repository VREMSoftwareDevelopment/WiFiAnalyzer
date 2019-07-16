/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.WifiManager;
import android.os.Handler;

import com.vrem.wifianalyzer.settings.Settings;

import androidx.annotation.NonNull;

public class ScannerServiceFactory {
    private ScannerServiceFactory() {
        throw new IllegalStateException("Factory class");
    }

    @NonNull
    public static ScannerService makeScannerService
        (@NonNull WifiManager wifiManager, @NonNull Handler handler, @NonNull Settings settings) {
        return new Scanner(wifiManager, handler, settings);
    }
}
