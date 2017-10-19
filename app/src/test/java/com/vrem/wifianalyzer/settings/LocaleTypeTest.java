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

package com.vrem.wifianalyzer.settings;

import org.apache.tools.ant.taskdefs.Local;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleTypeTest {

    @Test
    public void testFromString() throws Exception {
        // setup
        LocaleType expected = LocaleType.GERMAN;
        // execute
        LocaleType actual = LocaleType.fromString("DE");
        // validate
        assertEquals(actual, expected);
        assertEquals(actual.getLocale(), expected.getLocale());
    }

    @Test
    public void testFromStringFail() throws Exception {
        // execute
        LocaleType actual = LocaleType.fromString("WW");
        // validate
        assertNull(actual);
    }

}
