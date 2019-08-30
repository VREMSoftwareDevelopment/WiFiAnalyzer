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

import android.os.Build;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class VendorDBTest {
    private static final String VENDOR_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "00:23:AB:8C:DF:10";
    private static final String VENDOR_NAME_INVALID = "XXXXX";
    private static final String MAC_ADDRESS_INVALID = "XX:XX:XX";
    private static final int VENDOR_SIZE = 17463;
    private static final int MACS_SIZE = 26773;
    private static final int MACS_CISCO_SIZE = 888;
    private static final String FILTER_VENDOR = "1394 ";
    private static final String FILTER_MAC = "00:A0:2";
    private static final String EXPECTED_VENDOR_NAME1 = "1394 TRADE ASSOCIATION";
    private static final String EXPECTED_VENDOR_NAME2 = "TRANSITIONS RESEARCH CORP";
    private static final String EXPECTED_VENDOR_NAME3 = "1394 PRINTER WORKING GROUP";
    private static final String EXPECTED_MAC1 = "00:00:0C";
    private static final String EXPECTED_MAC2 = "FC:FB:FB";
    private static final String EXPECTED_MAC3 = "00:BC:60";

    private VendorDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new VendorDB(mainActivity.getResources());
    }

    @Test
    public void testFindVendorName() {
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS);
        // validate
        assertEquals(VENDOR_NAME, actual);
    }

    @Test
    public void testFindVendorNameUsingLowerCase() {
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS.toLowerCase());
        // validate
        assertEquals(VENDOR_NAME, actual);
    }

    @Test
    public void testFindVendorNameWithNull() {
        // execute
        String actual = fixture.findVendorName(null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindVendorNameWithInvalidMac() {
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS_INVALID);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindMacAddresses() {
        // setup
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME);
        // validate
        assertEquals(MACS_CISCO_SIZE, actual.size());

        assertEquals(EXPECTED_MAC1, actual.get(0));
        assertEquals(EXPECTED_MAC2, actual.get(MACS_CISCO_SIZE - 1));
        assertEquals(EXPECTED_MAC3, actual.get(MACS_CISCO_SIZE / 2));
    }

    @Test
    public void testFindMacAddressesUsingLowerCase() {
        // setup
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME.toLowerCase());
        // validate
        assertEquals(MACS_CISCO_SIZE, actual.size());

        assertEquals(EXPECTED_MAC1, actual.get(0));
        assertEquals(EXPECTED_MAC2, actual.get(MACS_CISCO_SIZE - 1));
        assertEquals(EXPECTED_MAC3, actual.get(MACS_CISCO_SIZE / 2));
    }

    @Test
    public void testFindMacAddressesWithNull() {
        // execute
        List<String> actual = fixture.findMacAddresses(null);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindMacAddressesWithInvalidName() {
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME_INVALID);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindVendors() {
        // execute
        List<String> actual = fixture.findVendors();
        // validate
        assertEquals(VENDOR_SIZE, actual.size());
    }

    @Test
    public void testFindVendorsWithEmptyVendorFilter() {
        // execute
        List<String> actual = fixture.findVendors(StringUtils.EMPTY);
        // validate
        assertEquals(VENDOR_SIZE, actual.size());
    }

    @Test
    public void testFindVendorsWithVendorFilter() {
        // execute
        List<String> actual = fixture.findVendors(FILTER_VENDOR);
        // validate
        assertEquals(2, actual.size());
        assertEquals(EXPECTED_VENDOR_NAME3, actual.get(0));
        assertEquals(EXPECTED_VENDOR_NAME1, actual.get(1));
    }

    @Test
    public void testFindVendorsWithVendorFilterUsingLowerCase() {
        // execute
        List<String> actual = fixture.findVendors(FILTER_VENDOR.toLowerCase());
        // validate
        assertEquals(2, actual.size());
        assertEquals(EXPECTED_VENDOR_NAME3, actual.get(0));
        assertEquals(EXPECTED_VENDOR_NAME1, actual.get(1));
    }

    @Test
    public void testFindVendorsWithMacFilter() {
        // execute
        List<String> actual = fixture.findVendors(FILTER_MAC);
        // validate
        assertEquals(16, actual.size());
        assertEquals(EXPECTED_VENDOR_NAME1, actual.get(0));
        assertEquals(EXPECTED_VENDOR_NAME2, actual.get(15));
    }

    @Test
    public void testFindVendorsWithMacFilterUsingLowerCase() {
        // execute
        List<String> actual = fixture.findVendors(FILTER_MAC.toLowerCase());
        // validate
        assertEquals(16, actual.size());
        assertEquals(EXPECTED_VENDOR_NAME1, actual.get(0));
        assertEquals(EXPECTED_VENDOR_NAME2, actual.get(15));
    }

    @Test
    public void testGetVendors() {
        // execute & validate
        assertEquals(VENDOR_SIZE, fixture.getVendors().size());
    }

    @Test
    public void testGetMacs() {
        // execute & validate
        assertEquals(MACS_SIZE, fixture.getMacs().size());
    }

}