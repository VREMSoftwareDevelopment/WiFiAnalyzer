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

package com.vrem.wifianalyzer.wifi.filter.adapter;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WiFiBandAdapterTest {
    @Mock
    private Settings settings;

    private WiFiBandAdapter fixture;

    @Before
    public void setUp() {
        fixture = new WiFiBandAdapter(EnumUtils.values(WiFiBand.class));
    }

    @Test
    public void testIsActive() throws Exception {
        assertFalse(fixture.isActive());
    }

    @Test
    public void testIsActivateWithChanges() throws Exception {
        // setup
        fixture.toggle(WiFiBand.GHZ2);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testContains() throws Exception {
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            assertTrue(fixture.contains(wiFiBand));
        }
    }

    @Test
    public void testToggleRemoves() throws Exception {
        // execute
        boolean actual = fixture.toggle(WiFiBand.GHZ2);
        // validate
        assertTrue(actual);
        assertFalse(fixture.contains(WiFiBand.GHZ2));
    }

    @Test
    public void testToggleAdds() throws Exception {
        // setup
        fixture.toggle(WiFiBand.GHZ5);
        // execute
        boolean actual = fixture.toggle(WiFiBand.GHZ5);
        // validate
        assertTrue(actual);
        assertTrue(fixture.contains(WiFiBand.GHZ5));
    }

    @Test
    public void testRemovingAllWillNotRemoveLast() throws Exception {
        // setup
        WiFiBand[] values = WiFiBand.values();
        // execute
        for (WiFiBand wiFiBand : values) {
            fixture.toggle(wiFiBand);
        }
        // validate
        int index = values.length - 1;
        for (int i = 0; i < index; i++) {
            assertFalse(fixture.contains(values[i]));
        }
        assertTrue(fixture.contains(WiFiBand.values()[index]));
    }

    @Test
    public void testGetColorWithExisting() throws Exception {
        // execute & validate
        assertEquals(R.color.connected, fixture.getColor(WiFiBand.GHZ2));
    }

    @Test
    public void testGetColorWithNonExisting() throws Exception {
        // setup
        fixture.toggle(WiFiBand.GHZ2);
        // execute & validate
        assertEquals(R.color.icons_color, fixture.getColor(WiFiBand.GHZ2));
    }

    @Test
    public void testSave() throws Exception {
        // setup
        Set<WiFiBand> expected = fixture.getValues();
        // execute
        fixture.save(settings);
        // execute
        verify(settings).saveWiFiBands(expected);
    }
}