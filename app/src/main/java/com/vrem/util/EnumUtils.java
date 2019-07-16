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

package com.vrem.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

public class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static <T extends Enum> T find(@NonNull Class<T> enumType, int index, @NonNull T defaultValue) {
        T[] values = enumType.getEnumConstants();
        if (index < 0 || index >= values.length) {
            return defaultValue;
        }
        return values[index];
    }

    @NonNull
    public static <T extends Enum> T find(@NonNull Class<T> enumType, @NonNull Predicate<T> predicate, @NonNull T defaultValue) {
        List<T> results = new ArrayList<>(CollectionUtils.select(values(enumType), predicate));
        return results.isEmpty() ? defaultValue : results.get(0);
    }

    @NonNull
    public static <T extends Enum> Set<T> find(@NonNull Class<T> enumType, @NonNull Set<String> ordinals, @NonNull T defaultValue) {
        Set<T> results = new HashSet<>(CollectionUtils.collect(ordinals, new ToEnum<>(enumType, defaultValue)));
        return results.isEmpty() ? values(enumType) : results;
    }

    @NonNull
    public static <T extends Enum> Set<String> find(@NonNull Set<T> values) {
        return new HashSet<>(CollectionUtils.collect(values, new ToOrdinal<>()));
    }

    @NonNull
    public static <T extends Enum> Set<String> ordinals(@NonNull Class<T> enumType) {
        return new HashSet<>(CollectionUtils.collect(values(enumType), new ToOrdinal<>()));
    }

    @NonNull
    public static <T extends Enum> Set<T> values(@NonNull Class<T> enumType) {
        return new HashSet<>(Arrays.asList(enumType.getEnumConstants()));
    }

    @NonNull
    public static <T extends Enum, U> Predicate<U> predicate(@NonNull Class<T> enumType, @NonNull Collection<T> input, @NonNull Transformer<T, Predicate<U>> transformer) {
        if (input.size() >= values(enumType).size()) {
            return PredicateUtils.truePredicate();
        }
        return PredicateUtils.anyPredicate(CollectionUtils.collect(input, transformer));
    }

    private static class ToEnum<T extends Enum> implements Transformer<String, T> {
        private final T defaultValue;
        private final Class<T> enumType;

        private ToEnum(@NonNull Class<T> enumType, @NonNull T defaultValue) {
            this.enumType = enumType;
            this.defaultValue = defaultValue;
        }

        @Override
        public T transform(String input) {
            try {
                return EnumUtils.find(enumType, Integer.parseInt(input), defaultValue);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    private static class ToOrdinal<T extends Enum> implements Transformer<T, String> {
        @Override
        public String transform(T input) {
            return Integer.toString(input.ordinal());
        }
    }

}