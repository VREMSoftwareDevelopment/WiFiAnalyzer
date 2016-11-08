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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WiFiChannelCountry {
    public static final String UNKNOWN = "Unknown";

    private static final WiFiChannelCountry[] COUNTRY_CHANNELS = new WiFiChannelCountry[]{
        new WiFiChannelCountry("AE"),
        new WiFiChannelCountry("AG"),
        new WiFiChannelCountry("AL"),
        new WiFiChannelCountry("AM"),
        new WiFiChannelCountry("AN"),
        new WiFiChannelCountry("AR"),
        new WiFiChannelCountry("AS"),
        new WiFiChannelCountry("AT"),
        new WiFiChannelCountry("AU"),
        new WiFiChannelCountry("AW"),
        new WiFiChannelCountry("AZ"),
        new WiFiChannelCountry("BA"),
        new WiFiChannelCountry("BB"),
        new WiFiChannelCountry("BD"),
        new WiFiChannelCountry("BE"),
        new WiFiChannelCountry("BG"),
        new WiFiChannelCountry("BH"),
        new WiFiChannelCountry("BM"),
        new WiFiChannelCountry("BN"),
        new WiFiChannelCountry("BO"),
        new WiFiChannelCountry("BR"),
        new WiFiChannelCountry("BS"),
        new WiFiChannelCountry("BT"),
        new WiFiChannelCountry("BY"),
        new WiFiChannelCountry("BZ"),
        new WiFiChannelCountry("CA"),
        new WiFiChannelCountry("CH"),
        new WiFiChannelCountry("CL"),
        new WiFiChannelCountry("CN"),
        new WiFiChannelCountry("CO"),
        new WiFiChannelCountry("CR"),
        new WiFiChannelCountry("CU"),
        new WiFiChannelCountry("CV"),
        new WiFiChannelCountry("CS"),
        new WiFiChannelCountry("CY"),
        new WiFiChannelCountry("CZ"),
        new WiFiChannelCountry("DE"),
        new WiFiChannelCountry("DK"),
        new WiFiChannelCountry("DM"),
        new WiFiChannelCountry("DO"),
        new WiFiChannelCountry("DZ"),
        new WiFiChannelCountry("EC"),
        new WiFiChannelCountry("EE"),
        new WiFiChannelCountry("EG"),
        new WiFiChannelCountry("ES"),
        new WiFiChannelCountry("FI"),
        new WiFiChannelCountry("FK"),
        new WiFiChannelCountry("FM"),
        new WiFiChannelCountry("FR"),
        new WiFiChannelCountry("GB"),
        new WiFiChannelCountry("GE"),
        new WiFiChannelCountry("GF"),
        new WiFiChannelCountry("GG"),
        new WiFiChannelCountry("GI"),
        new WiFiChannelCountry("GP"),
        new WiFiChannelCountry("GR"),
        new WiFiChannelCountry("GT"),
        new WiFiChannelCountry("GU"),
        new WiFiChannelCountry("HK"),
        new WiFiChannelCountry("HN"),
        new WiFiChannelCountry("HR"),
        new WiFiChannelCountry("HT"),
        new WiFiChannelCountry("HU"),
        new WiFiChannelCountry("ID"),
        new WiFiChannelCountry("IE"),
        new WiFiChannelCountry("IL"),
        new WiFiChannelCountry("IM"),
        new WiFiChannelCountry("IN"),
        new WiFiChannelCountry("IR"),
        new WiFiChannelCountry("IS"),
        new WiFiChannelCountry("IT"),
        new WiFiChannelCountry("JE"),
        new WiFiChannelCountry("JM"),
        new WiFiChannelCountry("JO"),
        new WiFiChannelCountry("JP"),
        new WiFiChannelCountry("KE"),
        new WiFiChannelCountry("KI"),
        new WiFiChannelCountry("KP"),
        new WiFiChannelCountry("KR"),
        new WiFiChannelCountry("KW"),
        new WiFiChannelCountry("KY"),
        new WiFiChannelCountry("KZ"),
        new WiFiChannelCountry("LA"),
        new WiFiChannelCountry("LB"),
        new WiFiChannelCountry("LI"),
        new WiFiChannelCountry("LK"),
        new WiFiChannelCountry("LS"),
        new WiFiChannelCountry("LT"),
        new WiFiChannelCountry("LU"),
        new WiFiChannelCountry("LV"),
        new WiFiChannelCountry("MA"),
        new WiFiChannelCountry("MC"),
        new WiFiChannelCountry("MK"),
        new WiFiChannelCountry("MO"),
        new WiFiChannelCountry("MP"),
        new WiFiChannelCountry("MQ"),
        new WiFiChannelCountry("MR"),
        new WiFiChannelCountry("MT"),
        new WiFiChannelCountry("MU"),
        new WiFiChannelCountry("MV"),
        new WiFiChannelCountry("MW"),
        new WiFiChannelCountry("MX"),
        new WiFiChannelCountry("MY"),
        new WiFiChannelCountry("NG"),
        new WiFiChannelCountry("NI"),
        new WiFiChannelCountry("NL"),
        new WiFiChannelCountry("NO"),
        new WiFiChannelCountry("NZ"),
        new WiFiChannelCountry("OM"),
        new WiFiChannelCountry("PA"),
        new WiFiChannelCountry("PE"),
        new WiFiChannelCountry("PG"),
        new WiFiChannelCountry("PH"),
        new WiFiChannelCountry("PK"),
        new WiFiChannelCountry("PL"),
        new WiFiChannelCountry("PM"),
        new WiFiChannelCountry("PR"),
        new WiFiChannelCountry("PT"),
        new WiFiChannelCountry("QA"),
        new WiFiChannelCountry("RE"),
        new WiFiChannelCountry("RO"),
        new WiFiChannelCountry("RU"),
        new WiFiChannelCountry("SA"),
        new WiFiChannelCountry("SE"),
        new WiFiChannelCountry("SG"),
        new WiFiChannelCountry("SI"),
        new WiFiChannelCountry("SK"),
        new WiFiChannelCountry("SV"),
        new WiFiChannelCountry("SY"),
        new WiFiChannelCountry("TH"),
        new WiFiChannelCountry("TJ"),
        new WiFiChannelCountry("TN"),
        new WiFiChannelCountry("TR"),
        new WiFiChannelCountry("TT"),
        new WiFiChannelCountry("TW"),
        new WiFiChannelCountry("TZ"),
        new WiFiChannelCountry("UA"),
        new WiFiChannelCountry("UM"),
        new WiFiChannelCountry("US"),
        new WiFiChannelCountry("UY"),
        new WiFiChannelCountry("UZ"),
        new WiFiChannelCountry("VA"),
        new WiFiChannelCountry("VE"),
        new WiFiChannelCountry("VG"),
        new WiFiChannelCountry("VI"),
        new WiFiChannelCountry("VN"),
        new WiFiChannelCountry("YE"),
        new WiFiChannelCountry("YT"),
        new WiFiChannelCountry("ZA"),
        new WiFiChannelCountry("ZM"),
        new WiFiChannelCountry("ZW"),
    };

    private final String countryCode;
    private final List<Integer> channelsGHZ2;
    private final List<Integer> channelsGHZ5;

    public WiFiChannelCountry(@NonNull String countryCode) {
        this.countryCode = countryCode;
        this.channelsGHZ2 = WiFiChannelCountryGHZ2.INSTANCE.findChannels(countryCode);
        this.channelsGHZ5 = WiFiChannelCountryGHZ5.INSTANCE.findChannels(countryCode);
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
        return channelsGHZ2;
    }

    public List<Integer> getChannelsGHZ5() {
        return channelsGHZ5;
    }

    boolean isChannelAvailableGHZ2(int channel) {
        return channelsGHZ2.contains(channel);
    }

    boolean isChannelAvailableGHZ5(int channel) {
        return channelsGHZ5.contains(channel);
    }
}
