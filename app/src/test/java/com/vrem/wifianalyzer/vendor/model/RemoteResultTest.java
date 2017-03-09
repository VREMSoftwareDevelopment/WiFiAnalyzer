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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class RemoteResultTest {
    private static final String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";

    private RemoteResult fixture;

    @Before
    public void setUp() {
        fixture = new RemoteResult(MAC_ADDRESS, VENDOR_NAME);
    }

    @Test
    public void testRemoteResultEmpty() throws Exception {
        // validate
        assertEquals(StringUtils.EMPTY, RemoteResult.EMPTY.getMacAddress());
        assertEquals(StringUtils.EMPTY, RemoteResult.EMPTY.getVendorName());
    }

    @Test
    public void testRemoteResult() throws Exception {
        // validate
        assertEquals(MAC_ADDRESS, fixture.getMacAddress());
        assertEquals(VENDOR_NAME, fixture.getVendorName());
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        RemoteResult other = new RemoteResult(MAC_ADDRESS, VENDOR_NAME);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        RemoteResult other = new RemoteResult(MAC_ADDRESS, VENDOR_NAME);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

}