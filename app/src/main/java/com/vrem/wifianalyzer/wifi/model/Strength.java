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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;

public enum Strength {
    ZERO(R.drawable.ic_signal_wifi_0_bar, R.color.error),
    ONE(R.drawable.ic_signal_wifi_1_bar, R.color.warning),
    TWO(R.drawable.ic_signal_wifi_2_bar, R.color.warning),
    THREE(R.drawable.ic_signal_wifi_3_bar, R.color.success),
    FOUR(R.drawable.ic_signal_wifi_4_bar, R.color.success);

    private final int imageResource;
    private final int colorResource;

    Strength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    @NonNull
    public static Strength calculate(int level) {
        int index = WiFiUtils.calculateSignalLevel(level, values().length);
        return Strength.values()[index];
    }

    @NonNull
    public static Strength reverse(Strength strength) {
        int index = Strength.values().length - strength.ordinal() - 1;
        return Strength.values()[index];
    }

    public int colorResource() {
        return colorResource;
    }

    public int colorResourceDefault() {
        return R.color.regular;
    }

    public int imageResource() {
        return imageResource;
    }

    public boolean weak() {
        return ZERO.equals(this);
    }

}
