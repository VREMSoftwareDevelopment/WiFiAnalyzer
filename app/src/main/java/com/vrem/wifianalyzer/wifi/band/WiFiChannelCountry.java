/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WiFiChannelCountry {
    public static final String UNKNOWN = "Unknown";

    protected static final List<Integer> DEFAULT_CHANNELS_GHZ2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    protected static final List<Integer> DEFAULT_CHANNELS_GHZ5 = Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64);

    private static final WiFiChannelCountry[] COUNTRY_CHANNELS = new WiFiChannelCountry[]{
        new WiFiChannelCountry("AE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("AG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("AL",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("AM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)
        ),
        new WiFiChannelCountry("AN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("AR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("AS",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("AT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("AU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("AW",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("AZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("BB",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BD",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("BG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("BH",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("BN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BS",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("BT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("BY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("BZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CH",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CL",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("CU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161)
        ),
        new WiFiChannelCountry("CV",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("CS",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("CY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("CZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("DE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("DK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("DM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("DO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("DZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("EC",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("EE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("EG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("ES",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("FI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("FK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("FM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("FR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GB",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GF",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GP",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("GT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("GU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("HK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("HN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("HR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("HT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("HU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("ID",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161)
        ),
        new WiFiChannelCountry("IE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("IL",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)
        ),
        new WiFiChannelCountry("IM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("IN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("IR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("IS",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("IT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("JE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("JM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("JO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("JP",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
            Arrays.asList(8, 12, 16, 34, 36, 38, 40, 42, 44, 46, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 184, 188, 192, 196)
        ),
        new WiFiChannelCountry("KE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48)
        ),
        new WiFiChannelCountry("KI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("KP",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161)
        ),
        new WiFiChannelCountry("KR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("KW",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("KY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("KZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("LA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("LB",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("LI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("LK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("LS",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("LT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("LU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("LV",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48)
        ),
        new WiFiChannelCountry("MC",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)
        ),
        new WiFiChannelCountry("MK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("MP",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("MQ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("MV",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("MW",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("MX",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("MY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("NG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("NI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("NL",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("NO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("NZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("OM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("PA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PH",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PL",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("PM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("PR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("PT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("QA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("RE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("RO",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("RU",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("SA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161)
        ),
        new WiFiChannelCountry("SE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("SG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("SI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("SK",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("SV",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("SY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161)
        ),
        new WiFiChannelCountry("TH",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("TJ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("TN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64)
        ),
        new WiFiChannelCountry("TR",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("TT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("TW",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("TZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(149, 153, 157, 161)
        ),
        new WiFiChannelCountry("UA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("UM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("US",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("UY",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("UZ",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("VA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("VE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("VG",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("VI",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("VN",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("YE",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
        new WiFiChannelCountry("YT",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("ZA",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165)
        ),
        new WiFiChannelCountry("ZM",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)
        ),
        new WiFiChannelCountry("ZW",
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
            new ArrayList<Integer>()
        ),
    };

    private final String countryCode;
    private final List<Integer> channelsGHZ2;
    private final List<Integer> channelsGHZ5;

    public WiFiChannelCountry(@NonNull String countryCode) {
        this(countryCode, DEFAULT_CHANNELS_GHZ2, DEFAULT_CHANNELS_GHZ5);
    }

    private WiFiChannelCountry(@NonNull String countryCode, @NonNull List<Integer> channelsGHZ2, List<Integer> channelsGHZ5) {
        this.countryCode = countryCode;
        this.channelsGHZ2 = channelsGHZ2;
        this.channelsGHZ5 = channelsGHZ5.isEmpty() ? DEFAULT_CHANNELS_GHZ5 : channelsGHZ5;
    }

    public static WiFiChannelCountry find(String countryCode) {
        for (WiFiChannelCountry wiFiChannelCountry : COUNTRY_CHANNELS) {
            if (wiFiChannelCountry.getCountryCode().equalsIgnoreCase(countryCode)) {
                return wiFiChannelCountry;
            }
        }
        return new WiFiChannelCountry(countryCode);
    }

    public static List<WiFiChannelCountry> getAll() {
        return Collections.unmodifiableList(Arrays.asList(COUNTRY_CHANNELS));
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        String countryName = new Country().getCountry(countryCode).getDisplayCountry();
        return countryCode.equals(countryName) ? UNKNOWN : countryName;
    }

    public List<Integer> getChannelsGHZ2() {
        return Collections.unmodifiableList(channelsGHZ2);
    }

    public List<Integer> getChannelsGHZ5() {
        return Collections.unmodifiableList(channelsGHZ5);
    }

    protected boolean isChannelAvailableGHZ2(int channel) {
        return channelsGHZ2.contains(channel);
    }

    protected boolean isChannelAvailableGHZ5(int channel) {
        return channelsGHZ5.contains(channel);
    }
}
