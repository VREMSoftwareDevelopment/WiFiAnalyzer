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

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorDBTest {

    private VendorDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new VendorDB(mainActivity.getResources());
    }

    @Test
    public void testFindByAddress() throws Exception {
        // setup
        String macAddress = "0023AB";
        String vendorName = "CISCO SYSTEMS INC";
        // execute
        List<VendorData> actual = fixture.findByAddress(macAddress);
        // validate
        assertEquals(1, actual.size());
        assertEquals(macAddress, actual.get(0).getMac());
        assertEquals(vendorName, actual.get(0).getName());
    }

    @Test
    public void testFindByName() throws Exception {
        // setup
        int expectedSize = 762;
        String vendorName = "CISCO SYSTEMS INC";
        // execute
        List<VendorData> actual = fixture.findByName(vendorName);
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("00000C", actual.get(0).getMac());
        assertEquals(vendorName, actual.get(0).getName());

        assertEquals("FCFBFB", actual.get(expectedSize - 1).getMac());
        assertEquals(vendorName, actual.get(expectedSize - 1).getName());

        assertEquals("005F86", actual.get(expectedSize / 2).getMac());
        assertEquals(vendorName, actual.get(expectedSize / 2).getName());
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        int expectedSize = 23383;
        // execute
        List<VendorData> actual = fixture.findAll();
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("000000", actual.get(0).getMac());
        assertEquals("XEROX CORPORATION", actual.get(0).getName());

        assertEquals("FCFFAA", actual.get(expectedSize - 1).getMac());
        assertEquals("IEEE REGISTRATION AUTHORITY", actual.get(expectedSize - 1).getName());

        assertEquals("00A03E", actual.get(expectedSize / 2).getMac());
        assertEquals("ATM FORUM", actual.get(expectedSize / 2).getName());
    }
}