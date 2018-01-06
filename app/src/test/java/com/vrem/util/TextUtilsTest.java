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

package com.vrem.util;

import com.vrem.wifianalyzer.BuildConfig;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TextUtilsTest {

    @Test
    public void testTextToHtmlSmall() throws Exception {
        // setup
        int color = 10;
        String text = "ThisIsText";
        String expected = "<font color='" + color + "'><small>" + text + "</small></font>";
        // execute
        String actual = TextUtils.textToHtml(text, color, true);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testTextToHtml() throws Exception {
        // setup
        int color = 10;
        String text = "ThisIsText";
        String expected = "<font color='" + color + "'><strong>" + text + "</strong></font>";
        // execute
        String actual = TextUtils.textToHtml(text, color, false);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin() throws Exception {
        // setup
        String expected = "ABC JDS";
        Set<String> values = new HashSet<>(Arrays.asList("", " ", "ABC", " JDS "));
        // execute
        String actual = TextUtils.join(values);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNull() throws Exception {
        // execute
        String actual = TextUtils.join(null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testSplit() throws Exception {
        // setup
        String value = "    ABS    ADF    ";
        String[] expected = new String[]{"ABS", "ADF"};
        // execute
        Set<String> actual = TextUtils.split(value);
        // verify
        assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testSplitWithNull() throws Exception {
        // execute
        Set<String> actual = TextUtils.split(null);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testTrim() throws Exception {
        // setup
        String expected = "ABS ADF";
        String value = "    ABS    ADF    ";
        // execute
        String actual = TextUtils.trim(value);
        // verify
        assertEquals(expected, actual);
    }

    @Test
    public void testTrimWithNull() throws Exception {
        // execute
        String actual = TextUtils.trim(null);
        // verify
        assertEquals(StringUtils.EMPTY, actual);
    }

}