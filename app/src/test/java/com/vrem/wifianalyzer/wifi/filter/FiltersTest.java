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
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FiltersTest {
    @Mock
    private Settings settings;

    private String ssid;
    private Set<WiFiBand> wiFiBands;
    private Set<Strength> strengths;
    private Set<Security> securities;

    private Filters fixture;

    @Before
    public void setUp() {
        ssid = StringUtils.EMPTY;
        wiFiBands = EnumUtils.values(WiFiBand.class);
        strengths = EnumUtils.values(Strength.class);
        securities = EnumUtils.values(Security.class);

        when(settings.getSSIDFilter()).thenReturn(ssid);
        when(settings.getWiFiBandFilter()).thenReturn(wiFiBands);
        when(settings.getStrengthFilter()).thenReturn(strengths);
        when(settings.getSecurityFilter()).thenReturn(securities);

        fixture = new Filters(settings);

        verify(settings).getSSIDFilter();
        verify(settings).getWiFiBandFilter();
        verify(settings).getStrengthFilter();
        verify(settings).getSecurityFilter();
    }

    @Test
    public void testIsActive() throws Exception {
        // execute & validate
        assertFalse(fixture.isActive());
    }

    @Test
    public void testIsActiveWhenStrengthFilterIsChanged() throws Exception {
        // setup
        fixture.getStrengthFilter().toggle(Strength.THREE);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testIsActiveWhenWiFiBandFilterIsChanged() throws Exception {
        // setup
        fixture.getWiFiBandFilter().toggle(WiFiBand.GHZ2);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testReset() {
        // execute
        fixture.reset();
        // validate
        verify(settings).saveSSIDFilter(ssid);
        verify(settings).saveWiFiBandFilter(wiFiBands);
        verify(settings).saveStrengthFilter(strengths);
        verify(settings).saveSecurityFilter(securities);
    }

    @Test
    public void testReload() {
        // execute
        fixture.reload();
        // validate
        verify(settings, times(2)).getSSIDFilter();
        verify(settings, times(2)).getWiFiBandFilter();
        verify(settings, times(2)).getStrengthFilter();
        verify(settings, times(2)).getSecurityFilter();
    }

    @Test
    public void testSave() {
        // execute
        fixture.save();
        // validate
        verify(settings).saveSSIDFilter(ssid);
        verify(settings).saveWiFiBandFilter(wiFiBands);
        verify(settings).saveStrengthFilter(strengths);
        verify(settings).saveSecurityFilter(securities);
    }

}