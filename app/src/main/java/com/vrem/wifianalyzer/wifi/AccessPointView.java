/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.R;

public enum AccessPointView {
    COMPLETE(R.layout.access_point_view_complete),
    COMPACT(R.layout.access_point_view_compact);

    private final int layout;

    AccessPointView(int layout) {
        this.layout = layout;
    }

    public static AccessPointView find(int index) {
        if (index < 0 || index >= values().length) {
            return COMPLETE;
        }
        return values()[index];
    }

    int getLayout() {
        return layout;
    }

    boolean isCompact() {
        return COMPACT.equals(this);
    }

    boolean isFull() {
        return COMPLETE.equals(this);
    }
}
