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

package com.vrem.wifianalyzer.navigation.availability;

import java.util.Arrays;
import java.util.List;

public class NavigationOptionFactory {
    public static final NavigationOption FILTER_OFF = new FilterOff();
    public static final NavigationOption FILTER_ON = new FilterOn();
    public static final NavigationOption SCANNER_SWITCH_OFF = new ScannerSwitchOff();
    public static final NavigationOption SCANNER_SWITCH_ON = new ScannerSwitchOn();
    public static final NavigationOption WIFI_SWITCH_OFF = new WiFiSwitchOff();
    public static final NavigationOption WIFI_SWITCH_ON = new WiFiSwitchOn();
    public static final NavigationOption BOTTOM_NAV_OFF = new BottomNavOff();
    public static final NavigationOption BOTTOM_NAV_ON = new BottomNavOn();

    public static final List<NavigationOption> AP = Arrays.asList(WIFI_SWITCH_OFF, SCANNER_SWITCH_ON, FILTER_ON, BOTTOM_NAV_ON);
    public static final List<NavigationOption> OFF = Arrays.asList(WIFI_SWITCH_OFF, SCANNER_SWITCH_OFF, FILTER_OFF, BOTTOM_NAV_OFF);
    public static final List<NavigationOption> OTHER = Arrays.asList(WIFI_SWITCH_ON, SCANNER_SWITCH_ON, FILTER_ON, BOTTOM_NAV_ON);
    public static final List<NavigationOption> RATING = Arrays.asList(WIFI_SWITCH_ON, SCANNER_SWITCH_ON, FILTER_OFF, BOTTOM_NAV_ON);

    private NavigationOptionFactory() {
        throw new IllegalStateException("Factory class");
    }
}
