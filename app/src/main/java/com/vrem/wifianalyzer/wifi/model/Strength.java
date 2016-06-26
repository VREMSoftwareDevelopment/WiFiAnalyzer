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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

public enum Strength {
    ZERO(R.drawable.ic_signal_wifi_0_bar_black_36dp, R.color.error_color),
    ONE(R.drawable.ic_signal_wifi_1_bar_black_36dp, R.color.warning_color),
    TWO(R.drawable.ic_signal_wifi_2_bar_black_36dp, R.color.warning_color),
    THREE(R.drawable.ic_signal_wifi_3_bar_black_36dp, R.color.success_color),
    FOUR(R.drawable.ic_signal_wifi_4_bar_black_36dp, R.color.success_color);

    private final int imageResource;
    private final int colorResource;

    Strength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    public static Strength calculate(int level) {
        return Strength.values()[WiFiUtils.calculateSignalLevel(level, values().length)];
    }

    public static Strength reverse(Strength strength) {
        return Strength.values()[Strength.values().length - strength.ordinal() - 1];
    }

    public int colorResource() {
        return colorResource;
    }

    public int imageResource() {
        return imageResource;
    }

    public boolean weak() {
        return ZERO.equals(this);
    }

}
