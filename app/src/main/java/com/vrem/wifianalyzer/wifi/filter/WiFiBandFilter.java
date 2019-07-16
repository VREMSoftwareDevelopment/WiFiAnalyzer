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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.Dialog;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.filter.adapter.WiFiBandAdapter;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

class WiFiBandFilter extends EnumFilter<WiFiBand, WiFiBandAdapter> {
    static final Map<WiFiBand, Integer> ids = new HashMap<>();

    static {
        ids.put(WiFiBand.GHZ2, R.id.filterWifiBand2);
        ids.put(WiFiBand.GHZ5, R.id.filterWifiBand5);
    }

    WiFiBandFilter(@NonNull WiFiBandAdapter wiFiBandAdapter, @NonNull Dialog dialog) {
        super(ids, wiFiBandAdapter, dialog, R.id.filterWiFiBand);
    }
}
