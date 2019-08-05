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

package com.vrem.wifianalyzer.wifi.filter.adapter;

import com.vrem.util.EnumUtils;

import java.util.Set;

import androidx.annotation.NonNull;

public abstract class EnumFilterAdapter<T extends Enum> extends BasicFilterAdapter<T> {
    private final Class<T> enumType;

    EnumFilterAdapter(@NonNull Class<T> enumType, @NonNull Set<T> values) {
        super(values);
        this.enumType = enumType;
    }

    @Override
    public boolean isActive() {
        return getValues().size() != EnumUtils.values(enumType).size();
    }

    public boolean toggle(@NonNull T object) {
        boolean toggle = false;
        if (contains(object)) {
            if (getValues().size() > 1) {
                toggle = getValues().remove(object);
            }
        } else {
            toggle = getValues().add(object);
        }
        return toggle;
    }

    @Override
    public void reset() {
        setValues(EnumUtils.values(enumType));
    }

    public abstract int getColor(@NonNull T object);

    boolean contains(@NonNull T object) {
        return getValues().contains(object);
    }

}
