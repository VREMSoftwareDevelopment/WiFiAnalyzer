/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorDBTest {
    private static final String VENDOR_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "00:23:AB:8C:DF:10";
    private static final String VENDOR_NAME_INVALID = "XXXXX";
    private static final String MAC_ADDRESS_INVALID = "XX:XX:XX";
    private static final int VENDOR_SIZE = 16886;
    private static final int MACS_SIZE = 24597;

    private VendorDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new VendorDB(mainActivity.getResources());
    }

    @Test
    public void testFindVendorName() throws Exception {
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS);
        // validate
        assertEquals(VENDOR_NAME, actual);
    }

    @Test
    public void testFindVendorNameWithNull() throws Exception {
        // execute
        String actual = fixture.findVendorName(null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindVendorNameWithInvalidMac() throws Exception {
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS_INVALID);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindMacAddresses() throws Exception {
        // setup
        int expectedSize = 801;
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME);
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("00:00:0C", actual.get(0));
        assertEquals("FC:FB:FB", actual.get(expectedSize - 1));
        assertEquals("00:87:31", actual.get(expectedSize / 2));
    }

    @Test
    public void testFindMacAddressesWithNull() throws Exception {
        // execute
        List<String> actual = fixture.findMacAddresses(null);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindMacAddressesWithInvalidName() throws Exception {
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME_INVALID);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindVendors() throws Exception {
        // execute
        List<String> actual = fixture.findVendors();
        // validate
        assertEquals(VENDOR_SIZE, actual.size());
    }

    @Test
    public void testFindVendorsWithVendorFilter() throws Exception {
        // execute
        List<String> actual = fixture.findVendors("1394 ");
        // validate
        assertEquals(2, actual.size());
        assertEquals("1394 PRINTER WORKING GROUP", actual.get(0));
        assertEquals("1394 TRADE ASSOCIATION", actual.get(1));
    }

    @Test
    public void testFindVendorsWithMacFilter() throws Exception {
        // execute
        List<String> actual = fixture.findVendors("00:A0:2");
        // validate
        assertEquals(16, actual.size());
        assertEquals("1394 TRADE ASSOCIATION", actual.get(0));
        assertEquals("TRANSITIONS RESEARCH CORP", actual.get(15));
    }

    @Test
    public void testGetVendors() throws Exception {
        // execute & validate
        assertEquals(VENDOR_SIZE, fixture.getVendors().size());
    }

    @Test
    public void testGetMacs() throws Exception {
        // execute & validate
        assertEquals(MACS_SIZE, fixture.getMacs().size());
    }

}