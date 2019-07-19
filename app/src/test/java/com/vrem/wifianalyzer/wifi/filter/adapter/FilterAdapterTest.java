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

import android.os.Build;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class FilterAdapterTest {
    private Set<String> ssids;
    private Set<WiFiBand> wiFiBands;
    private Set<Strength> strengths;
    private Set<Security> securities;

    private Settings settings;
    private FilterAdapter fixture;

    @Before
    public void setUp() {
        ssids = Collections.emptySet();
        wiFiBands = EnumUtils.values(WiFiBand.class);
        strengths = EnumUtils.values(Strength.class);
        securities = EnumUtils.values(Security.class);

        RobolectricUtil.INSTANCE.getActivity();
        settings = MainContextHelper.INSTANCE.getSettings();
        when(settings.getSSIDs()).thenReturn(ssids);
        when(settings.getWiFiBands()).thenReturn(wiFiBands);
        when(settings.getStrengths()).thenReturn(strengths);
        when(settings.getSecurities()).thenReturn(securities);

        fixture = new FilterAdapter(settings);

        verify(settings).getSSIDs();
        verify(settings).getWiFiBands();
        verify(settings).getStrengths();
        verify(settings).getSecurities();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testIsActive() {
        // execute & validate
        assertFalse(fixture.isActive());
    }

    @Test
    public void testGetFilterAdapters() {
        // execute
        List<? extends BasicFilterAdapter<? extends Serializable>> actual = fixture.getFilterAdapters(true);
        // validate
        assertEquals(4, actual.size());
    }

    @Test
    public void testGetFilterAdaptersWithNptAccessPoints() {
        // execute
        List<? extends BasicFilterAdapter<? extends Serializable>> actual = fixture.getFilterAdapters(false);
        // validate
        assertEquals(3, actual.size());
    }

    @Test
    public void testIsActiveWhenStrengthFilterIsChanged() {
        // setup
        fixture.getStrengthAdapter().toggle(Strength.THREE);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testIsActiveWhenWiFiBandFilterIsChanged() {
        // setup
        fixture.getWiFiBandAdapter().toggle(WiFiBand.GHZ2);
        // execute & validate
        assertTrue(fixture.isActive());
    }

    @Test
    public void testReset() {
        // execute
        fixture.reset();
        // validate
        verify(settings).saveSSIDs(ssids);
        verify(settings).saveWiFiBands(wiFiBands);
        verify(settings).saveStrengths(strengths);
        verify(settings).saveSecurities(securities);
    }

    @Test
    public void testReload() {
        // execute
        fixture.reload();
        // validate
        verify(settings, times(2)).getSSIDs();
        verify(settings, times(2)).getWiFiBands();
        verify(settings, times(2)).getStrengths();
        verify(settings, times(2)).getSecurities();
    }

    @Test
    public void testSave() {
        // execute
        fixture.save();
        // validate
        verify(settings).saveSSIDs(ssids);
        verify(settings).saveWiFiBands(wiFiBands);
        verify(settings).saveStrengths(strengths);
        verify(settings).saveSecurities(securities);
    }

}