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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.Transformer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.NonNull;

public enum Security {
    NONE(R.drawable.ic_lock_open),
    WPS(R.drawable.ic_lock_outline),
    WEP(R.drawable.ic_lock_outline),
    WPA(R.drawable.ic_lock),
    WPA2(R.drawable.ic_lock),
    WPA3(R.drawable.ic_lock, Constants.RSN);

    private final int imageResource;
    private final String additional;

    Security(int imageResource) {
        this(imageResource, null);
    }

    Security(int imageResource, String additional) {
        this.imageResource = imageResource;
        this.additional = additional;
    }

    @NonNull
    public static Set<Security> findAll(String capabilities) {
        if (capabilities == null) {
            return new TreeSet<>();
        }
        return new TreeSet<>(
            CollectionUtils.select(
                CollectionUtils.collect(parseCapabilities(capabilities), new SecurityTransformer()),
                PredicateUtils.notNullPredicate()
            )
        );
    }

    private static List<String> parseCapabilities(String capabilities) {
        return Arrays.asList(capabilities
            .toUpperCase(Locale.getDefault())
            .replace("][", "-")
            .replace("]", "")
            .replace("[", "")
            .split("-")
        );
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
        private final Set<Security> securities;

        private SecurityPredicate(@NonNull Set<Security> securities) {
            this.securities = securities;
        }

        @Override
        public boolean evaluate(Security security) {
            return securities.contains(security);
        }
    }

    private static class SecurityAdditionalPredicate implements Predicate<Security> {
        private final String value;

        private SecurityAdditionalPredicate(@NonNull String value) {
            this.value = value;
        }

        @Override
        public boolean evaluate(Security security) {
            return value.equals(security.additional);
        }
    }

    private static class SecurityTransformer implements Transformer<String, Security> {
        @Override
        public Security transform(String input) {
            try {
                return Security.valueOf(input);
            } catch (Exception e) {
                return IterableUtils.find(EnumUtils.values(Security.class), new SecurityAdditionalPredicate(input));
            }
        }
    }

    private static class Constants {
        static final String RSN = "RSN";
    }
}
