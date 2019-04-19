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

package com.vrem.wifianalyzer.vendor.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VendorUtilsTest {
    private static final String MAC_ADDRESS_CLEAN = "0023AB";
    private static final String MAC_ADDRESS_SHORT = "00:23:AB";
    private static final String MAC_ADDRESS_FULL = "00:23:AB:8C:DF:10";

    @Test
    public void testCleanWithNull() {
        assertEquals(StringUtils.EMPTY, VendorUtils.clean(null));
    }

    @Test
    public void testClean() {
        assertEquals(MAC_ADDRESS_CLEAN, VendorUtils.clean(MAC_ADDRESS_FULL));
        assertEquals("34AF", VendorUtils.clean("34aF"));
        assertEquals("34AF0B", VendorUtils.clean("34aF0B"));
        assertEquals("34AA0B", VendorUtils.clean("34:aa:0b"));
        assertEquals("34AC0B", VendorUtils.clean("34:ac:0B:A0"));
    }

    @Test
    public void testToMacAddressWithNull() {
        assertEquals(StringUtils.EMPTY, VendorUtils.toMacAddress(null));
    }

    @Test
    public void testToMacAddress() {
        assertEquals(MAC_ADDRESS_SHORT, VendorUtils.toMacAddress(MAC_ADDRESS_CLEAN));
        assertEquals("*34AF*", VendorUtils.toMacAddress("34AF"));
        assertEquals("34:AF:0B", VendorUtils.toMacAddress("34AF0BAC"));
    }

}