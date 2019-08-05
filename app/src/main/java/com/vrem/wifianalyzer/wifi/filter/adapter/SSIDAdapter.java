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

import com.vrem.wifianalyzer.settings.Settings;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;

public class SSIDAdapter extends BasicFilterAdapter<String> {
    SSIDAdapter(@NonNull Set<String> values) {
        super(values);
    }

    @Override
    public void setValues(@NonNull Set<String> values) {
        super.setValues(new HashSet<>(CollectionUtils.select(values, new SSIDPredicate())));
    }

    @Override
    public boolean isActive() {
        return !getValues().isEmpty();
    }

    @Override
    public void reset() {
        setValues(new HashSet<>());
    }

    @Override
    public void save(@NonNull Settings settings) {
        settings.saveSSIDs(getValues());
    }

    private class SSIDPredicate implements Predicate<String> {
        @Override
        public boolean evaluate(String object) {
            return StringUtils.isNotBlank(StringUtils.trim(object));
        }
    }

}
