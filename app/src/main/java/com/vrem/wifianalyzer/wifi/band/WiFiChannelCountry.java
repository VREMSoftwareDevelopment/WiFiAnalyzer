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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class WiFiChannelCountry {
    public static final String UNKNOWN = "Unknown";

    private static final SortedSet<String> COUNTRY_CODES = new TreeSet<>(Arrays.asList(
        "AE",
        "AG",
        "AL",
        "AM",
        "AN",
        "AR",
        "AS",
        "AT",
        "AU",
        "AW",
        "AZ",
        "BA",
        "BB",
        "BD",
        "BE",
        "BG",
        "BH",
        "BM",
        "BN",
        "BO",
        "BR",
        "BS",
        "BT",
        "BY",
        "BZ",
        "CA",
        "CH",
        "CL",
        "CN",
        "CO",
        "CR",
        "CU",
        "CV",
        "CS",
        "CY",
        "CZ",
        "DE",
        "DK",
        "DM",
        "DO",
        "DZ",
        "EC",
        "EE",
        "EG",
        "ES",
        "FI",
        "FK",
        "FM",
        "FR",
        "GB",
        "GE",
        "GF",
        "GG",
        "GI",
        "GP",
        "GR",
        "GT",
        "GU",
        "HK",
        "HN",
        "HR",
        "HT",
        "HU",
        "ID",
        "IE",
        "IL",
        "IM",
        "IN",
        "IR",
        "IS",
        "IT",
        "JE",
        "JM",
        "JO",
        "JP",
        "KE",
        "KI",
        "KP",
        "KR",
        "KW",
        "KY",
        "KZ",
        "LA",
        "LB",
        "LI",
        "LK",
        "LS",
        "LT",
        "LU",
        "LV",
        "MA",
        "MC",
        "MK",
        "MO",
        "MP",
        "MQ",
        "MR",
        "MT",
        "MU",
        "MV",
        "MW",
        "MX",
        "MY",
        "NG",
        "NI",
        "NL",
        "NO",
        "NZ",
        "OM",
        "PA",
        "PE",
        "PG",
        "PH",
        "PK",
        "PL",
        "PM",
        "PR",
        "PT",
        "QA",
        "RE",
        "RO",
        "RU",
        "SA",
        "SE",
        "SG",
        "SI",
        "SK",
        "SV",
        "SY",
        "TH",
        "TJ",
        "TN",
        "TR",
        "TT",
        "TW",
        "TZ",
        "UA",
        "UM",
        "US",
        "UY",
        "UZ",
        "VA",
        "VE",
        "VG",
        "VI",
        "VN",
        "YE",
        "YT",
        "ZA",
        "ZM",
        "ZW"
    ));

    private final String countryCode;

    private WiFiChannelCountry(@NonNull String countryCode) {
        this.countryCode = StringUtils.capitalize(countryCode);
    }

    public static WiFiChannelCountry find(String countryCode) {
        return new WiFiChannelCountry(countryCode);
    }

    public static List<WiFiChannelCountry> getAll() {
        List<WiFiChannelCountry> results = new ArrayList<>();
        for (String countryCode : COUNTRY_CODES) {
            results.add(find(countryCode));
        }
        return results;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        String countryName = new Country().getCountry(countryCode).getDisplayCountry();
        return countryCode.equals(countryName) ? UNKNOWN : countryName;
    }

    public SortedSet<Integer> getChannelsGHZ2() {
        return WiFiChannelCountryGHZ2.INSTANCE.findChannels(countryCode);
    }

    public SortedSet<Integer> getChannelsGHZ5() {
        return WiFiChannelCountryGHZ5.INSTANCE.findChannels(countryCode);
    }

    boolean isChannelAvailableGHZ2(int channel) {
        return getChannelsGHZ2().contains(channel);
    }

    boolean isChannelAvailableGHZ5(int channel) {
        return getChannelsGHZ5().contains(channel);
    }
}
