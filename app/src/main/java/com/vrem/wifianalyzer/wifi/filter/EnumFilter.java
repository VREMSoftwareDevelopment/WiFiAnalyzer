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

import com.vrem.util.EnumUtils;

import java.util.Set;

abstract class EnumFilter<T extends Enum> implements Filter {
    private final Class<T> enumType;
    private Set<T> values;

    EnumFilter(@NonNull Class<T> enumType, @NonNull Set<T> values) {
        this.enumType = enumType;
        this.values = values;
    }

    @Override
    public boolean isActive() {
        return values.size() != EnumUtils.values(enumType).size();
    }

    boolean toggle(@NonNull T object) {
        boolean toggle = false;
        if (contains(object)) {
            if (values.size() > 1) {
                toggle = values.remove(object);
            }
        } else {
            toggle = values.add(object);
        }
        return toggle;
    }

    @Override
    public void reset() {
        values = EnumUtils.values(enumType);
    }

    abstract int getColor(@NonNull T object);

    boolean contains(@NonNull T object) {
        return values.contains(object);
    }

    Set<T> getValues() {
        return values;
    }
}
