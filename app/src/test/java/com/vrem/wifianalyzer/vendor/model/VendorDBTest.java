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

import org.apache.commons.lang3.StringUtils;
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
    private static final String VENDOR_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "0023AB";
    private static final int VENDOR_INDEX = 2846;

    private VendorDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new VendorDB(mainActivity.getResources());
    }

    @Test
    public void testFindVendorIndex() throws Exception {
        // execute & validate
        assertEquals(VENDOR_INDEX, fixture.findVendorIndex(MAC_ADDRESS));
    }

    @Test
    public void testFindVendorName() throws Exception {
        // execute & validate
        assertEquals(VENDOR_NAME, fixture.findVendorName(VENDOR_INDEX));
    }

    @Test
    public void testFindByName() throws Exception {
        // setup
        int expectedSize = 762;
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_INDEX);
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("00000C", actual.get(0));
        assertEquals("FCFBFB", actual.get(expectedSize - 1));
        assertEquals("005F86", actual.get(expectedSize / 2));
    }

    @Test
    public void testGetMacs() throws Exception {
        // setup
        int expectedSize = 23383;
        // execute
        String[] actual = fixture.getMacs();
        // validate
        assertEquals(expectedSize, actual.length);
        assertEquals("00000016191", actual[0]);
        assertEquals("FCFFAA6761", actual[expectedSize - 1]);
    }

    @Test
    public void testGetVendors() throws Exception {
        // setup
        int expectedSize = 16576;
        // execute
        String[] actual = fixture.getVendors();
        // validate
        assertEquals(expectedSize, actual.length);
        assertEquals("01DB METRAVIB", actual[0]);
        assertEquals("ZYXEL COMMUNICATIONS CORPORATION", actual[expectedSize - 1]);
    }

    @Test
    public void testToAddress() throws Exception {
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.toAddress("12345"));
        assertEquals("123456", fixture.toAddress("123456"));
        assertEquals("123456", fixture.toAddress("1234567"));
    }

    @Test
    public void testToIndex() throws Exception {
        // execute & validate
        assertEquals(VendorDB.ID_INVALID, fixture.toIndex("123456"));
        assertEquals(7, fixture.toIndex("1234567"));
        assertEquals(VendorDB.ID_INVALID, fixture.toIndex("123456A"));
    }

}