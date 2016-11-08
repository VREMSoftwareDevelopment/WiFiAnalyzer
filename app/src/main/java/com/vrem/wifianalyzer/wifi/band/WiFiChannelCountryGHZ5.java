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
import android.support.v4.util.Pair;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum WiFiChannelCountryGHZ5 {
    INSTANCE;

    private final List<Pair<String, List<Integer>>> countryChannels;

    WiFiChannelCountryGHZ5() {
        countryChannels = new ArrayList<>();
        countryChannels.add(new Pair<>("AG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("AM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)));
        countryChannels.add(new Pair<>("AN", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("AR", Arrays.asList(52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("AS", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("AT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("AU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("AW", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("AZ", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("BB", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BD", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("BG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("BH", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("BN", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BO", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BS", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("BY", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("BZ", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CH", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CL", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CN", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CO", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("CU", Arrays.asList(149, 153, 157, 161)));
        countryChannels.add(new Pair<>("CY", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("CZ", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("DE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("DK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("DM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("DO", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("EC", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("EE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("EG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("ES", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("FI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("FK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("FM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("FR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GB", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GF", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GP", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("GT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("GU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("HK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("HN", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("HR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("HT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("HU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("ID", Arrays.asList(149, 153, 157, 161)));
        countryChannels.add(new Pair<>("IE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("IL", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)));
        countryChannels.add(new Pair<>("IM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("IN", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("IR", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("IS", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("IT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("JE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("JM", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("JO", Arrays.asList(36, 40, 44, 48, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("JP", Arrays.asList(36, 38, 40, 42, 44, 46, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("KE", Arrays.asList(36, 40, 44, 48)));
        countryChannels.add(new Pair<>("KI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("KP", Arrays.asList(149, 153, 157, 161)));
        countryChannels.add(new Pair<>("KR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("KY", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LB", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("LI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("LS", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("LV", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MC", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)));
        countryChannels.add(new Pair<>("MK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MO", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("MP", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("MQ", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("MV", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("MW", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("MX", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("MY", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("NI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("NL", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("NO", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("NZ", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("OM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("PA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PH", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PK", Arrays.asList(149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PL", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("PM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("PR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("PT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("RE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("RO", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("RU", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("SA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161)));
        countryChannels.add(new Pair<>("SE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("SG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("SI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("SK", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("SV", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("SY", Arrays.asList(149, 153, 157, 161)));
        countryChannels.add(new Pair<>("TH", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("TN", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)));
        countryChannels.add(new Pair<>("TR", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("TT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("TW", Arrays.asList(52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("TZ", Arrays.asList(149, 153, 157, 161)));
        countryChannels.add(new Pair<>("UM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("US", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("UY", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("VE", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("VG", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("VI", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("VN", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("YT", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
        countryChannels.add(new Pair<>("ZA", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)));
        countryChannels.add(new Pair<>("ZM", Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)));
    }

    List<Integer> findChannels(@NonNull String countryCode) {
        String code = StringUtils.capitalize(countryCode);
        for (Pair countryChannel : countryChannels) {
            if (countryChannel.first.equals(code)) {
                return Collections.unmodifiableList((List<Integer>) countryChannel.second);
            }
        }
        return Collections.unmodifiableList(Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64));
    }

}
