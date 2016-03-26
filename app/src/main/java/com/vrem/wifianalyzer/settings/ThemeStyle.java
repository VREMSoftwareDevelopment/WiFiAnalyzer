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

package com.vrem.wifianalyzer.settings;

import com.vrem.wifianalyzer.R;

public enum ThemeStyle {
    DARK(R.style.ThemeAppCompatDark, R.style.ThemeDeviceDefaultDark),
    LIGHT(R.style.ThemeAppCompatLight, R.style.ThemeDeviceDefaultLight);

    private final int themeAppCompatStyle;
    private final int themeDeviceDefaultStyle;

    ThemeStyle(int themeAppCompatStyle, int themeDeviceDefaultStyle) {
        this.themeAppCompatStyle = themeAppCompatStyle;
        this.themeDeviceDefaultStyle = themeDeviceDefaultStyle;
    }

    public static ThemeStyle find(int index) {
        try {
            return values()[index];
        } catch (Exception e) {
            return DARK;
        }
    }

    public int themeAppCompatStyle() {
        return themeAppCompatStyle;
    }

    public int themeDeviceDefaultStyle() {
        return themeDeviceDefaultStyle;
    }
}
