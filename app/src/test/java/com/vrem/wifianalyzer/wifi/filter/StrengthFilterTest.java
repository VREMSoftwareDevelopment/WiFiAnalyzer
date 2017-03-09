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

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.Strength;

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
public class StrengthFilterTest {
    @Mock
    private Settings settings;

    private StrengthFilter fixture;

    @Before
    public void setUp() {
        fixture = new StrengthFilter(EnumUtils.values(Strength.class));
    }

    @Test
    public void testIsActive() throws Exception {
        assertFalse(fixture.isActive());
    }

    @Test
    public void testIsActivateWithChanges() throws Exception {
        // setup
        fixture.toggle(Strength.TWO);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testContains() throws Exception {
        for (Strength strength : Strength.values()) {
            assertTrue(fixture.contains(strength));
        }
    }

    @Test
    public void testToggleRemoves() throws Exception {
        // execute
        boolean actual = fixture.toggle(Strength.TWO);
        // validate
        assertTrue(actual);
        assertFalse(fixture.contains(Strength.TWO));
    }

    @Test
    public void testToggleAdds() throws Exception {
        // setup
        fixture.toggle(Strength.THREE);
        // execute
        boolean actual = fixture.toggle(Strength.THREE);
        // validate
        assertTrue(actual);
        assertTrue(fixture.contains(Strength.THREE));
    }

    @Test
    public void testRemovingAllWillNotRemoveLast() throws Exception {
        // setup
        Strength[] values = Strength.values();
        // execute
        for (Strength strength : values) {
            fixture.toggle(strength);
        }
        // validate
        int index = values.length - 1;
        for (int i = 0; i < index; i++) {
            assertFalse(fixture.contains(values[i]));
        }
        assertTrue(fixture.contains(Strength.values()[index]));
    }

    @Test
    public void testGetColorWithExisting() throws Exception {
        // execute & validate
        assertEquals(Strength.TWO.colorResource(), fixture.getColor(Strength.TWO));
    }

    @Test
    public void testGetColorWithNonExisting() throws Exception {
        // setup
        fixture.toggle(Strength.TWO);
        // execute & validate
        assertEquals(Strength.TWO.colorResourceDefault(), fixture.getColor(Strength.TWO));
    }

    @Test
    public void testSave() throws Exception {
        // setup
        Set<Strength> expected = fixture.getValues();
        // execute
        fixture.save(settings);
        // execute
        verify(settings).saveStrengthFilter(expected);
    }

}