/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.vendor.model;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RemoteResultTest {
    private static final String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    private static final String JSON = "{\"mac_address\":\"" + MAC_ADDRESS + "\",\"vendor_name\":\"" + VENDOR_NAME + "\"}";

    @Before
    public void setUp() throws Exception {
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