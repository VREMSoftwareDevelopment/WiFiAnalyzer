/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SecurityTest {

    @Test
    public void testGetImageResource() throws Exception {
        assertEquals(R.drawable.ic_lock_open_black_18dp, Security.NONE.getImageResource());
        assertEquals(R.drawable.ic_lock_outline_red_900_18dp, Security.WPS.getImageResource());
        assertEquals(R.drawable.ic_lock_outline_red_900_18dp, Security.WEP.getImageResource());
        assertEquals(R.drawable.ic_lock_black_18dp, Security.WPA.getImageResource());
        assertEquals(R.drawable.ic_lock_black_18dp, Security.WPA2.getImageResource());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Security> expected = Arrays.asList(Security.WPS, Security.WEP, Security.WPA, Security.WPA2);
        assertEquals(expected, Security.findAll("WPA-WPA2-WPA-WEP-WPS-WPA2"));
    }

    @Test
    public void testFindOne() throws Exception {
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