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
    private static final String VENDOR_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "0023AB";

    private VendorDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new VendorDB(mainActivity.getResources());
    }

    @Test
    public void testFindVendorName() throws Exception {
        // execute & validate
        assertEquals(VENDOR_NAME, fixture.findVendorName(MAC_ADDRESS));
    }

    @Test
    public void testFindMacAddresses() throws Exception {
        // setup
        int expectedSize = 782;
        // execute
        List<String> actual = fixture.findMacAddresses(VENDOR_NAME);
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("00000C", actual.get(0));
        assertEquals("FCFBFB", actual.get(expectedSize - 1));
        assertEquals("0062EC", actual.get(expectedSize / 2));
    }

    @Test
    public void testVendorDataIsValid() throws Exception {
        // execute & validate
        assertEquals(16749, fixture.getVendors().size());
    }

    @Test
    public void testMacDataIsValid() throws Exception {
        // execute & validate
        assertEquals(24013, fixture.getMacs().size());
    }
}