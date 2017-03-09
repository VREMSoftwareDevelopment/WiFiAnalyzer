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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class VendorNameUtilsTest {

    @Test
    public void testCleanVendorNameWithNull() throws Exception {
        // execute
        String actual = VendorNameUtils.cleanVendorName(null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testCleanVendorNameWithBlank() throws Exception {
        // execute
        String actual = VendorNameUtils.cleanVendorName("   ");
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testCleanVendorNameAsHTML() throws Exception {
        // execute & validate
        assertEquals(StringUtils.EMPTY, VendorNameUtils.cleanVendorName("X < Y Z"));
        assertEquals(StringUtils.EMPTY, VendorNameUtils.cleanVendorName("X Y > Z"));
    }

    @Test
    public void testCleanVendorNameTrimsBlanks() throws Exception {
        // setup
        String input = " X    Y    Z  ";
        String expected = "X Y Z";
        // execute
        String actual = VendorNameUtils.cleanVendorName(input);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanVendorNameWithSpecialCharacters() throws Exception {
        // setup
        String input = "X ~`!@#$%^&*()_-+={[}]|\\:;\"',.?/Y";
        String expected = "X Y";
        // execute
        String actual = VendorNameUtils.cleanVendorName(input);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanVendorNameWithMaximumLength() throws Exception {
        // setup
        String input = "123456789012345678901234567890123456789012345678901234567890";
        // execute
        String actual = VendorNameUtils.cleanVendorName(input);
        // validate
        assertEquals(input.substring(0, 50), actual);
    }

}