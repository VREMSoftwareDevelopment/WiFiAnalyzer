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

package com.vrem.wifianalyzer.wifi.filter;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.Strength;

import java.util.Set;

class StrengthFilter extends EnumFilter<Strength> {
    StrengthFilter(@NonNull Set<Strength> values) {
        super(Strength.class, values);
    }

    @Override
    int getColor(@NonNull Strength object) {
        return contains(object) ? object.colorResource() : object.colorResourceDefault();
    }

    @Override
    public void save(@NonNull Settings settings) {
        settings.saveStrengthFilter(getValues());
    }
}
