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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.WifiManager;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.ActivityUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BuildUtils.class, ActivityUtils.class})
public class WiFiSwitchTest {
    @Mock
    private WifiManager wifiManager;

    private WiFiSwitch fixture;

    @Before
    public void setUp() {
        mockStatic(BuildUtils.class, ActivityUtils.class);
        fixture = new WiFiSwitch(wifiManager);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(BuildUtils.class);
        verifyNoMoreInteractions(ActivityUtils.class);
        verifyNoMoreInteractions(wifiManager);
    }

    @Test
    public void testSetEnabledWithTrue() {
        // setup
        when(BuildUtils.isMinVersionQ()).thenReturn(false);
        when(wifiManager.setWifiEnabled(true)).thenReturn(true);
        // execute
        boolean actual = fixture.setEnabled(true);
        // validate
        assertTrue(actual);
        verify(wifiManager).setWifiEnabled(true);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testSetEnabledWithFalse() {
        // setup
        when(BuildUtils.isMinVersionQ()).thenReturn(false);
        when(wifiManager.setWifiEnabled(false)).thenReturn(true);
        // execute
        boolean actual = fixture.setEnabled(false);
        // validate
        assertTrue(actual);
        verify(wifiManager).setWifiEnabled(false);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testSetEnabledWithAndroidQ() {
        // setup
        when(BuildUtils.isMinVersionQ()).thenReturn(true);
        // execute
        boolean actual = fixture.setEnabled(true);
        // validate
        assertTrue(actual);
        verifyStatic(ActivityUtils.class);
        ActivityUtils.startWiFiSettings();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }
}
