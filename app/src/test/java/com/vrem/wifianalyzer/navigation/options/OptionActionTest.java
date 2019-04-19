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

package com.vrem.wifianalyzer.navigation.options;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionActionTest {
    @Test
    public void testOptionAction() {
        assertEquals(4, OptionAction.values().length);
    }

    @Test
    public void testGetKey() {
        assertEquals(-1, OptionAction.NO_ACTION.getKey());
        assertEquals(R.id.action_scanner, OptionAction.SCANNER.getKey());
        assertEquals(R.id.action_filter, OptionAction.FILTER.getKey());
        assertEquals(R.id.action_wifi_band, OptionAction.WIFI_BAND.getKey());
    }

    @Test
    public void testGetAction() {
        assertTrue(OptionAction.NO_ACTION.getAction() instanceof OptionAction.NoAction);
        assertTrue(OptionAction.SCANNER.getAction() instanceof ScannerAction);
        assertTrue(OptionAction.FILTER.getAction() instanceof FilterAction);
        assertTrue(OptionAction.WIFI_BAND.getAction() instanceof WiFiBandAction);
    }

    @Test
    public void testGetOptionAction() {
        assertEquals(OptionAction.NO_ACTION, OptionAction.findOptionAction(OptionAction.NO_ACTION.getKey()));
        assertEquals(OptionAction.SCANNER, OptionAction.findOptionAction(OptionAction.SCANNER.getKey()));
        assertEquals(OptionAction.FILTER, OptionAction.findOptionAction(OptionAction.FILTER.getKey()));
        assertEquals(OptionAction.WIFI_BAND, OptionAction.findOptionAction(OptionAction.WIFI_BAND.getKey()));
    }

    @Test
    public void testGetOptionActionInvalidKey() {
        assertEquals(OptionAction.NO_ACTION, OptionAction.findOptionAction(-99));
        assertEquals(OptionAction.NO_ACTION, OptionAction.findOptionAction(99));
    }

}