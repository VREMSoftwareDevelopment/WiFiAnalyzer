/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.model;

import android.support.annotation.NonNull;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public enum Security {
    NONE(R.drawable.ic_lock_open_black_18dp),
    WPS(R.drawable.ic_lock_outline_black_18dp),
    WEP(R.drawable.ic_lock_outline_black_18dp),
    WPA(R.drawable.ic_lock_black_18dp),
    WPA2(R.drawable.ic_lock_black_18dp);

    private final int imageResource;

    Security(int imageResource) {
        this.imageResource = imageResource;
    }

    @NonNull
    public static List<Security> findAll(String capabilities) {
        Set<Security> results = new TreeSet<>();
        if (capabilities != null) {
            String[] values = capabilities.toUpperCase()
                .replace("][", "-").replace("]", "").replace("[", "").split("-");
            for (String value : values) {
                try {
                    results.add(Security.valueOf(value));
                } catch (Exception e) {
                    // skip getCapabilities that are not getSecurity
                }
            }
        }
        return new ArrayList<>(results);
    }

    @NonNull
    public static Security findOne(String capabilities) {
        Security result = IterableUtils.find(EnumUtils.values(Security.class), new SecurityPredicate(findAll(capabilities)));
        return result == null ? Security.NONE : result;
    }

    public int getImageResource() {
        return imageResource;
    }

    private static class SecurityPredicate implements Predicate<Security> {
        private final List<Security> securities;

        private SecurityPredicate(@NonNull List<Security> securities) {
            this.securities = securities;
        }

        @Override
        public boolean evaluate(Security security) {
            return securities.contains(security);
        }
    }

}
