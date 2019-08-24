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

package com.vrem.wifianalyzer.settings;

import com.vrem.util.BuildUtils;

import androidx.annotation.NonNull;

public class SettingsFactory {
    private SettingsFactory() {
        throw new IllegalStateException("Factory class");
    }

    @NonNull
    public static Settings make(@NonNull Repository repository) {
        if (BuildUtils.isMinVersionQ()) {
            return new SettingsAndroidQ(repository);
        } else if (BuildUtils.isVersionP()) {
            return new SettingsAndroidP(repository);
        } else {
            return new Settings(repository);
        }
    }
}