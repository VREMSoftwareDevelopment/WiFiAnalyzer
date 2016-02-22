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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SecurityTest {

    @Test
    public void testSecurity() throws Exception {
        assertEquals(5, Security.values().length);
    }

    @Test
    public void testImageResource() throws Exception {
        Assert.assertEquals(R.drawable.ic_lock_open_black_18dp, Security.NONE.imageResource());
        assertEquals(R.drawable.ic_lock_outline_black_18dp, Security.WPS.imageResource());
        assertEquals(R.drawable.ic_lock_outline_black_18dp, Security.WEP.imageResource());
        assertEquals(R.drawable.ic_lock_black_18dp, Security.WPA.imageResource());
        assertEquals(R.drawable.ic_lock_black_18dp, Security.WPA2.imageResource());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Security> expected = Arrays.asList(Security.WPS, Security.WEP, Security.WPA, Security.WPA2);
        assertEquals(expected, Security.findAll("WPA-WPA2-WPA-WEP-WPS-WPA2"));
    }

    @Test
    public void testFindOne() throws Exception {
        assertEquals(Security.NONE, Security.findOne("xyz"));
        assertEquals(Security.NONE, Security.findOne(Security.NONE.name()));
        assertEquals(Security.WPS, Security.findOne(Security.WPS.name()));
        assertEquals(Security.WEP, Security.findOne(Security.WEP.name()));
        assertEquals(Security.WPA, Security.findOne(Security.WPA.name()));
        assertEquals(Security.WPA2, Security.findOne(Security.WPA2.name()));
    }

    @Test
    public void testOrder() throws Exception {
        Security[] expected = {Security.NONE, Security.WPS, Security.WEP, Security.WPA, Security.WPA2};
        assertArrayEquals(expected, Security.values());
    }
}