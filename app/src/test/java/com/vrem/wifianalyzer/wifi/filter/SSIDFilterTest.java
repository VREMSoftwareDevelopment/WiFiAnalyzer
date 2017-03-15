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

package com.vrem.wifianalyzer.wifi.filter;

import com.vrem.wifianalyzer.settings.Settings;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SSIDFilterTest {

    private static final Set<String> SSID_VALUES = new HashSet<>(Arrays.asList("value1", "value2", "value3"));

    @Mock
    private Settings settings;

    private SSIDFilter fixture;

    @Before
    public void setUp() {
        fixture = new SSIDFilter(SSID_VALUES);
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(StringUtils.join(SSID_VALUES, ' '), fixture.getValue());
    }

    @Test
    public void testIsActive() throws Exception {
        assertTrue(fixture.isActive());
    }

    @Test
    public void testIsNotActiveWithEmptyValue() throws Exception {
        // execute
        fixture.setValue(StringUtils.EMPTY);
        // validate
        assertFalse(fixture.isActive());
        assertEquals(StringUtils.EMPTY, fixture.getValue());
    }

    @Test
    public void testIsNotActiveWithReset() throws Exception {
        // execute
        fixture.reset();
        // validate
        assertFalse(fixture.isActive());
        assertEquals(StringUtils.EMPTY, fixture.getValue());
    }

    @Test
    public void testSave() throws Exception {
        // execute
        fixture.save(settings);
        // execute
        verify(settings).saveSSIDFilter(SSID_VALUES);
    }
}