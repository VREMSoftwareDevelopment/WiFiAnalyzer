/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.vendor.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MacAddressTest {
    @Test
    public void testClean() throws Exception {
        assertEquals("34AF", MacAddress.clean("34aF"));
        assertEquals("34AF0B", MacAddress.clean("34aF0B"));
        assertEquals("34AA0B", MacAddress.clean("34:aa:0b"));
        assertEquals("34AC0B", MacAddress.clean(MacAddress.clean("34:ac:0B:A0")));
    }
}