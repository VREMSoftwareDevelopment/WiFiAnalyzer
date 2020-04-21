/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation.options;

import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;

enum OptionAction {
    NO_ACTION(-1, new NoAction()),
    SCANNER(R.id.action_scanner, new ScannerAction()),
    FILTER(R.id.action_filter, new FilterAction()),
    WIFI_BAND(R.id.action_wifi_band, new WiFiBandAction());

    private final int key;
    private final Action action;

    OptionAction(int key, @NonNull Action action) {
        this.key = key;
        this.action = action;
    }

    @NonNull
    public static OptionAction findOptionAction(int key) {
        for (OptionAction value : values()) {
            if (value.key == key) return value;
        }
        return NO_ACTION;
    }

    int getKey() {
        return key;
    }

    @NonNull
    Action getAction() {
        return action;
    }

    static class NoAction implements Action {
        @Override
        public void execute() {
            // do nothing
        }
    }

}
