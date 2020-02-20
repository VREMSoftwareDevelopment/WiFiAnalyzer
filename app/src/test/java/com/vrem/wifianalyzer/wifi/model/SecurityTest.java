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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SecurityTest {

    @Test
    public void testSecurity() {
        assertEquals(6, Security.values().length);
    }

    @Test
    public void testGetImageResource() {
        assertEquals(R.drawable.ic_lock_open, Security.NONE.getImageResource());
        assertEquals(R.drawable.ic_lock_outline, Security.WPS.getImageResource());
        assertEquals(R.drawable.ic_lock_outline, Security.WEP.getImageResource());
        assertEquals(R.drawable.ic_lock, Security.WPA.getImageResource());
        assertEquals(R.drawable.ic_lock, Security.WPA2.getImageResource());
        assertEquals(R.drawable.ic_lock, Security.WPA3.getImageResource());
    }

    @Test
    public void testFindAll() {
        // setup
        String capabilities = "WPA-WPA2-WPA-WEP-YZX-WPA3-WPS-WPA2";
        Set<Security> expected = new TreeSet<>(Arrays.asList(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3));
        // execute
        Set<Security> actual = Security.findAll(capabilities);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllWithAdditional() {
        // setup
        String capabilities = "WPA-WPA2-WPA-WEP-RSN-KPG-WPS-WPA2";
        Set<Security> expected = new TreeSet<>(Arrays.asList(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3));
        // execute
        Set<Security> actual = Security.findAll(capabilities);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindOne() {
        assertEquals(Security.NONE, Security.findOne("xyz"));
        assertEquals(Security.NONE, Security.findOne("WPA3-WPA2-WPA-WEP-WPS-NONE"));
        assertEquals(Security.WPS, Security.findOne("WPA3-WPA2-WPA-WEP-WPS"));
        assertEquals(Security.WEP, Security.findOne("WPA3-WPA2-WPA-WEP"));
        assertEquals(Security.WPA, Security.findOne("WPA3-WPA2-WPA"));
        assertEquals(Security.WPA2, Security.findOne("WPA3-WPA2"));
        assertEquals(Security.WPA3, Security.findOne("WPA3"));
        assertEquals(Security.WPA3, Security.findOne("RSN"));
    }

    @Test
    public void testOrder() {
        Security[] expected = {Security.NONE, Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3};
        assertArrayEquals(expected, Security.values());
    }
}