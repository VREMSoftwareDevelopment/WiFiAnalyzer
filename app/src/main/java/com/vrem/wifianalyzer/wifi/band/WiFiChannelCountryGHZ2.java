/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum WiFiChannelCountryGHZ2 {
    INSTANCE;

    private final List<String> countries;
    private final List<Integer> channels;
    private final List<Integer> world;
    private final List<Integer> japan;

    WiFiChannelCountryGHZ2() {
        countries = Arrays.asList("AS", "AU", "CA", "FM", "GU", "MP", "PA", "PR", "TW", "UM", "US", "VI");
        channels = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        world = new ArrayList<>(channels);
        world.add(12);
        world.add(13);
        japan = new ArrayList<>(world);
        japan.add(14);
    }

    List<Integer> findChannels(@NonNull String countryCode) {
        List<Integer> result = world;
        String code = StringUtils.capitalize(countryCode);
        if ("JP".equals(code)) {
            result = japan;
        } else if (countries.contains(code)) {
            result = channels;
        }
        return Collections.unmodifiableList(result);
    }

}