/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.RobolectricUtil;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RemoteResultTest {
    private static final String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    private static final String JSON = "{\"mac_address\":\"" + MAC_ADDRESS + "\",\"vendor_name\":\"" + VENDOR_NAME + "\"}";

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getMainActivity();
    }

    @Test
    public void testToJson() throws Exception {
        // setup
        RemoteResult fixture = new RemoteResult(MAC_ADDRESS, VENDOR_NAME);
        // execute
        String actual = fixture.toJson();
        // validate
        assertEquals(JSON, actual);
    }

    @Test
    public void testFromJson() throws Exception {
        // execute
        RemoteResult fixture = new RemoteResult(JSON);
        // validate
        assertEquals(MAC_ADDRESS, fixture.getMacAddress());
        assertEquals(VENDOR_NAME, fixture.getVendorName());
    }

    @Test(expected = JSONException.class)
    public void testFromJsonWithInvalidJson() throws Exception {
        new RemoteResult("");
    }
}