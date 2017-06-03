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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorServiceTest {
    private static final String VENDOR_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "0023AB";
    private static final String MAC_ADDRESS_SEARCH = "00:23:AB:8C:DF:10";
    private VendorDB vendorDB;

    private VendorService fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();

        vendorDB = mock(VendorDB.class);
        fixture = new VendorService(mainActivity.getResources());
        fixture.setVendorDB(vendorDB);
    }

    @Test
    public void testFindVendorName() throws Exception {
        // setup
        when(vendorDB.findVendorName(MAC_ADDRESS)).thenReturn(VENDOR_NAME);
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS_SEARCH);
        // validate
        assertEquals(VENDOR_NAME, actual);
        assertEquals(1, fixture.findVendors().size());
        verify(vendorDB).findVendorName(MAC_ADDRESS);
    }

    @Test
    public void testFindVendorNameWithNotFound() throws Exception {
        // setup
        when(vendorDB.findVendorName(MAC_ADDRESS)).thenReturn(null);
        // execute
        String actual = fixture.findVendorName(MAC_ADDRESS_SEARCH);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
        assertTrue(fixture.findVendors().isEmpty());

        verify(vendorDB).findVendorName(MAC_ADDRESS);
    }

    @Test
    public void testFindMacAddresses() throws Exception {
        // setup
        List<String> expected = Collections.emptyList();
        when(vendorDB.findMacAddresses(VENDOR_NAME)).thenReturn(expected);
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME);
        // validate
        assertEquals(expected, actual);
        verify(vendorDB).findMacAddresses(VENDOR_NAME);
    }

    @Test
    public void testFindMacAddressesWithNotFound() throws Exception {
        // setup
        List<String> expected = Collections.emptyList();
        when(vendorDB.findMacAddresses(VENDOR_NAME)).thenReturn(null);
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME);
        // validate
        assertEquals(expected, actual);
        verify(vendorDB).findMacAddresses(VENDOR_NAME);
    }

    @Test
    public void testClean() throws Exception {
        assertEquals(MAC_ADDRESS, fixture.clean(MAC_ADDRESS_SEARCH));
        assertEquals("34AF", fixture.clean("34aF"));
        assertEquals("34AF0B", fixture.clean("34aF0B"));
        assertEquals("34AA0B", fixture.clean("34:aa:0b"));
        assertEquals("34AC0B", fixture.clean("34:ac:0B:A0"));
    }

}