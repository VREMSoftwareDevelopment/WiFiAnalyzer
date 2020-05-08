/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.R;

import java.util.Set;

import androidx.annotation.NonNull;

public abstract class EnumFilterAdapter<T extends Enum> extends BasicFilterAdapter<T> {
    private final Set<T> valuesDefault;

    EnumFilterAdapter(@NonNull Set<T> values) {
        super(values);
        this.valuesDefault = values;
    }

    @Override
    public boolean isActive() {
        return getValues().size() != valuesDefault.size();
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
        setValues(valuesDefault);
    }

    public int getColor(@NonNull T object) {
        return contains(object) ? R.color.selected : R.color.regular;
    }

    boolean contains(@NonNull T object) {
        return getValues().contains(object);
    }

}
