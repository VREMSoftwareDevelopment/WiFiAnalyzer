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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.Dialog;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.filter.adapter.StrengthAdapter;
import com.vrem.wifianalyzer.wifi.model.Strength;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

class StrengthFilter extends EnumFilter<Strength, StrengthAdapter> {
    static final Map<Strength, Integer> ids = new HashMap<>();

    static {
        ids.put(Strength.ZERO, R.id.filterStrength0);
        ids.put(Strength.ONE, R.id.filterStrength1);
        ids.put(Strength.TWO, R.id.filterStrength2);
        ids.put(Strength.THREE, R.id.filterStrength3);
        ids.put(Strength.FOUR, R.id.filterStrength4);
    }

    StrengthFilter(@NonNull StrengthAdapter strengthAdapter, @NonNull Dialog dialog) {
        super(ids, strengthAdapter, dialog, R.id.filterStrength);
    }
}
