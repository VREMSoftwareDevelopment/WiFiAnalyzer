/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation.options;

import android.view.View;

import java.util.Arrays;
import java.util.List;

public class NavigationOptionFactory {
    public final static NavigationOption WIFI_SWITCH_ON = new WiFiSwitchOn();
    public final static NavigationOption SCANNER_SWITCH_ON = new ScannerSwitchOn();
    public final static NavigationOption NEXT_PREV_ON = new NextPrevNavigation(View.VISIBLE);

    public final static NavigationOption WIFI_SWITCH_OFF = new WiFiSwitchOff();
    public final static NavigationOption SCANNER_SWITCH_OFF = new ScannerSwitchOff();
    public final static NavigationOption NEXT_PREV_OFF = new NextPrevNavigation(View.INVISIBLE);

    public final static List<NavigationOption> ALL_ON = Arrays.asList(WIFI_SWITCH_ON, SCANNER_SWITCH_ON, NEXT_PREV_ON);
    public final static List<NavigationOption> ALL_OFF = Arrays.asList(WIFI_SWITCH_OFF, SCANNER_SWITCH_OFF, NEXT_PREV_OFF);
}
