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

package com.vrem.wifianalyzer.wifi.filter.adapter;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.Security;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SecurityAdapterTest {
    @Mock
    private Settings settings;

    private SecurityAdapter fixture;

    @Before
    public void setUp() {
        fixture = new SecurityAdapter(EnumUtils.values(Security.class));
    }

    @Test
    public void testIsActive() {
        assertFalse(fixture.isActive());
    }

    @Test
    public void testIsActivateWithChanges() {
        // setup
        fixture.toggle(Security.WPA);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testContains() {
        IterableUtils.forEach(EnumUtils.values(Security.class), new ContainsClosure());
    }

    @Test
    public void testToggleRemoves() {
        // execute
        boolean actual = fixture.toggle(Security.WEP);
        // validate
        assertTrue(actual);
        assertFalse(fixture.contains(Security.WEP));
    }

    @Test
    public void testToggleAdds() {
        // setup
        fixture.toggle(Security.WPA);
        // execute
        boolean actual = fixture.toggle(Security.WPA);
        // validate
        assertTrue(actual);
        assertTrue(fixture.contains(Security.WPA));
    }

    @Test
    public void testRemovingAllWillNotRemoveLast() {
        // setup
        Set<Security> values = EnumUtils.values(Security.class);
        // execute
        IterableUtils.forEach(values, new ToggleClosure());
        // validate
        IterableUtils.forEachButLast(values, new RemovedClosure());
        assertTrue(fixture.contains(IterableUtils.get(values, values.size() - 1)));
    }

    @Test
    public void testGetColorWithExisting() {
        // execute & validate
        assertEquals(R.color.selected, fixture.getColor(Security.WPA));
    }

    @Test
    public void testGetColorWithNonExisting() {
        // setup
        fixture.toggle(Security.WPA);
        // execute & validate
        assertEquals(R.color.regular, fixture.getColor(Security.WPA));
    }

    @Test
    public void testSave() {
        // setup
        Set<Security> expected = fixture.getValues();
        // execute
        fixture.save(settings);
        // execute
        verify(settings).saveSecurities(expected);
    }

    private class ContainsClosure implements Closure<Security> {
        @Override
        public void execute(Security security) {
            assertTrue(fixture.contains(security));
        }
    }

    private class ToggleClosure implements Closure<Security> {
        @Override
        public void execute(Security input) {
            fixture.toggle(input);
        }
    }

    private class RemovedClosure implements Closure<Security> {
        @Override
        public void execute(Security input) {
            assertFalse(fixture.contains(input));
        }
    }
}