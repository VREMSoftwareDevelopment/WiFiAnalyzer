/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.wifi.filter;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class SSIDFilter implements Filter {
    private static final char SEPARATOR_CHAR = ' ';
    private Set<String> values;

    SSIDFilter(@NonNull Set<String> values) {
        setValues(values);
    }

    @Override
    public boolean isActive() {
        return !values.isEmpty();
    }

    public String getValue() {
        return StringUtils.join(values, SEPARATOR_CHAR);
    }

    public void setValue(@NonNull String value) {
        this.values = new HashSet<>(Arrays.asList(StringUtils.split(value, SEPARATOR_CHAR)));
    }

    public void setValues(@NonNull Set<String> values) {
        this.values = values;
    }

    @Override
    public void reset() {
        setValues(new HashSet<String>());
    }

    @Override
    public void save(@NonNull Settings settings) {
        settings.saveSSIDFilter(values);
    }
}
