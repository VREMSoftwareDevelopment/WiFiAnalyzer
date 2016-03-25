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

import java.util.Arrays;
import java.util.Locale;

public class WiFiChannelCountry {
    private final static WiFiChannelCountry[] COUNTRY_CHANNELS = new WiFiChannelCountry[]{
            new WiFiChannelCountry("AE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // United Arab Emirates
            new WiFiChannelCountry("AG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Antigua and Barbuda
            new WiFiChannelCountry("AN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Netherlands Antilles
            new WiFiChannelCountry("AR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                                           // Argentina
            new WiFiChannelCountry("AS", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // American Samoa
            new WiFiChannelCountry("AT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Austria
            new WiFiChannelCountry("AU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Australia
            new WiFiChannelCountry("AW", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Aruba
            new WiFiChannelCountry("AZ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Azerbaijan
            new WiFiChannelCountry("BA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Bosnia
            new WiFiChannelCountry("BB", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Barbados
            new WiFiChannelCountry("BD", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161, 165),                                                                                           // Bangladesh
            new WiFiChannelCountry("BE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Belgium
            new WiFiChannelCountry("BG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Bulgaria
            new WiFiChannelCountry("BH", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Bahrain
            new WiFiChannelCountry("BM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Bermuda
            new WiFiChannelCountry("BN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Brunei
            new WiFiChannelCountry("BO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Bolivia
            new WiFiChannelCountry("BR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Brazil
            new WiFiChannelCountry("BS", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Bahamas
            new WiFiChannelCountry("BT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Bhutan
            new WiFiChannelCountry("BY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Belarus
            new WiFiChannelCountry("CA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140, 149, 153, 157, 161, 165),                           // Canada
            new WiFiChannelCountry("CH", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Switzerland
            new WiFiChannelCountry("CL", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Chile
            new WiFiChannelCountry("CN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161, 165),                                                                                           // China
            new WiFiChannelCountry("CO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Colombia
            new WiFiChannelCountry("CR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Costa Rica
            new WiFiChannelCountry("CU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Cuba
            new WiFiChannelCountry("CV", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Cape Verde
            new WiFiChannelCountry("CY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Cyprus
            new WiFiChannelCountry("CZ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Czech Republic
            new WiFiChannelCountry("DE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Germany
            new WiFiChannelCountry("DK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Denmark
            new WiFiChannelCountry("DM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Dominica
            new WiFiChannelCountry("DO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Dominican Republic
            new WiFiChannelCountry("EC", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Ecuador
            new WiFiChannelCountry("EE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Estonia
            new WiFiChannelCountry("EG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Egypt
            new WiFiChannelCountry("ES", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Spain
            new WiFiChannelCountry("FI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Finland
            new WiFiChannelCountry("FK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Falkland Islands
            new WiFiChannelCountry("FM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Federated States of Micronesia
            new WiFiChannelCountry("FR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // France
            new WiFiChannelCountry("GB", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // United Kingdom
            new WiFiChannelCountry("GF", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // French Guiana
            new WiFiChannelCountry("GG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Guernsey
            new WiFiChannelCountry("GI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Gibralter
            new WiFiChannelCountry("GP", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Guadeloupe
            new WiFiChannelCountry("GR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Greece
            new WiFiChannelCountry("GT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Guatemala
            new WiFiChannelCountry("GU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Guam
            new WiFiChannelCountry("HK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Hong Kong
            new WiFiChannelCountry("HN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161, 165),                                                                                           // Honduras
            new WiFiChannelCountry("HR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Croatia
            new WiFiChannelCountry("HT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Haiti
            new WiFiChannelCountry("HU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Hungary
            new WiFiChannelCountry("ID", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Indonesia
            new WiFiChannelCountry("IE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Ireland
            new WiFiChannelCountry("IL", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64),                                                                                    // Israel
            new WiFiChannelCountry("IM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Isle of Man
            new WiFiChannelCountry("IN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // India
            new WiFiChannelCountry("IR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Iran
            new WiFiChannelCountry("IS", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Iceland
            new WiFiChannelCountry("IT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Italy
            new WiFiChannelCountry("JE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Jersey
            new WiFiChannelCountry("JM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161, 165),                                                                                           // Jamaica
            new WiFiChannelCountry("JO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48),                                                                                                    // Jordan
            new WiFiChannelCountry("JP", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                         // Japan
            new WiFiChannelCountry("KE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48),                                                                                                    // Kenya
            new WiFiChannelCountry("KI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Kiribati
            new WiFiChannelCountry("KR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Korea Republic
            new WiFiChannelCountry("KW", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Kuwait
            new WiFiChannelCountry("KY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Cayman Islands
            new WiFiChannelCountry("LA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // LAO People's Democratic Republic
            new WiFiChannelCountry("LB", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Lebanon
            new WiFiChannelCountry("LI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Liechtenstein
            new WiFiChannelCountry("LK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Sri Lanka
            new WiFiChannelCountry("LS", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Lesotho
            new WiFiChannelCountry("LT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Lithuania
            new WiFiChannelCountry("LU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Luxembourg
            new WiFiChannelCountry("LV", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Latvia
            new WiFiChannelCountry("MA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48),                                                                                                    // Morocco
            new WiFiChannelCountry("MC", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64),                                                                                    // Monaco
            new WiFiChannelCountry("MK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Macedonia
            new WiFiChannelCountry("MO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Macao
            new WiFiChannelCountry("MP", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Northern Mariana Islands
            new WiFiChannelCountry("MQ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Martinique
            new WiFiChannelCountry("MR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Mauritania
            new WiFiChannelCountry("MT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Malta
            new WiFiChannelCountry("MU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Mauritius
            new WiFiChannelCountry("MV", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Maldives
            new WiFiChannelCountry("MW", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Malawi
            new WiFiChannelCountry("MX", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64),                                                                                    // Mexico
            new WiFiChannelCountry("MY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Malaysia
            new WiFiChannelCountry("NG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Nigeria
            new WiFiChannelCountry("NI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Nicaragua
            new WiFiChannelCountry("NL", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Netherlands
            new WiFiChannelCountry("NO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Norway
            new WiFiChannelCountry("NZ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // New Zealand
            new WiFiChannelCountry("OM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Oman
            new WiFiChannelCountry("PA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Panama
            new WiFiChannelCountry("PE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Peru
            new WiFiChannelCountry("PG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Papua New Guinea
            new WiFiChannelCountry("PH", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Philippines
            new WiFiChannelCountry("PK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161, 165),                                                                                           // Pakistan
            new WiFiChannelCountry("PL", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Poland
            new WiFiChannelCountry("PM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64),                                                                                    // St. Pierre and Miquelon
            new WiFiChannelCountry("PR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Puerto Rico
            new WiFiChannelCountry("PT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Portugal
            new WiFiChannelCountry("RE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Reunion
            new WiFiChannelCountry("RO", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Romania
            new WiFiChannelCountry("RU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Russia
            new WiFiChannelCountry("SA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161),                                                                // Saudi Arabia
            new WiFiChannelCountry("SE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Sweden
            new WiFiChannelCountry("SG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Singapore
            new WiFiChannelCountry("SI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Slovenia
            new WiFiChannelCountry("SK", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Slovak Republic
            new WiFiChannelCountry("SV", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // El Salvador
            new WiFiChannelCountry("SY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Syria
            new WiFiChannelCountry("TH", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Thailand
            new WiFiChannelCountry("TJ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Tajikistan
            new WiFiChannelCountry("TN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Tunisia
            new WiFiChannelCountry("TR", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 132, 136, 140),                                            // Turkey
            new WiFiChannelCountry("TT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Trinidad & Tobago
            new WiFiChannelCountry("TW", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 56, 60, 64, 149, 153, 157, 161, 165),                                                                                       // Taiwan
            new WiFiChannelCountry("TZ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 149, 153, 157, 161),                                                                                                // Tanzania
            new WiFiChannelCountry("UA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Ukraine
            new WiFiChannelCountry("UM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // United States (Minor Outlying Islands)
            new WiFiChannelCountry("US", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // United States
            new WiFiChannelCountry("UY", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),    // Uruguay
            new WiFiChannelCountry("UZ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Uzbekistan
            new WiFiChannelCountry("VA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),                                                                                                                    // Holy See (Vatican City State)
            new WiFiChannelCountry("VE", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Venezuela
            new WiFiChannelCountry("VG", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Virgin Islands (British)
            new WiFiChannelCountry("VI", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 149, 153, 157, 161, 165),            // Virgin Islands (United States)
            new WiFiChannelCountry("VN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 149, 153, 157, 161, 165),                                                           // Vietnam
            new WiFiChannelCountry("YT", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // Mayotte
            new WiFiChannelCountry("ZA", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140),                             // South Africa
            new WiFiChannelCountry("ZM", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140)                              // Zambia
    };

    private final String countryCode;
    private final int[] channels;

    private WiFiChannelCountry(@NonNull String countryCode, @NonNull int... channels) {
        this.countryCode = countryCode;
        this.channels = channels;
    }

        public static boolean isChannelAvailable(@NonNull Locale locale, boolean bandGHZ_5, int channel) {
        String country = locale.getCountry();
        for (WiFiChannelCountry wiFiChannelCountry : COUNTRY_CHANNELS) {
            if (wiFiChannelCountry.countryCode.equalsIgnoreCase(country)) {
                    return wiFiChannelCountry.isChannelAvailable(channel) || bandGHZ_5;
            }
        }
        return false;
    }

    private boolean isChannelAvailable(int channel) {
        return Arrays.binarySearch(channels, channel) >= 0;
    }

}
