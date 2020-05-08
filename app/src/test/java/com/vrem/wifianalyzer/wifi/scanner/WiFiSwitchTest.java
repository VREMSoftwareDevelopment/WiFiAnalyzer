/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class WiFiSwitchTest {
    @Mock
    private WifiManager wifiManager;

    private WiFiSwitch fixture;

    @Before
    public void setUp() {
        fixture = spy(new WiFiSwitch(wifiManager));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(wifiManager);
    }

    @Test
    public void testSetEnabledWithTrue() {
        // setup
        when(wifiManager.setWifiEnabled(true)).thenReturn(true);
        // execute
        boolean actual = fixture.setEnabled(true);
        // validate
        assertTrue(actual);
        verify(wifiManager).setWifiEnabled(true);
    }

    @Test
    public void testSetEnabledWithFalse() {
        // setup
        when(wifiManager.setWifiEnabled(false)).thenReturn(true);
        // execute
        boolean actual = fixture.setEnabled(false);
        // validate
        assertTrue(actual);
        verify(wifiManager).setWifiEnabled(false);
    }

    @Test
    public void testSetEnabledWithAndroidQ() {
        // setup
        doReturn(true).when(fixture).isMinVersionQ();
        doNothing().when(fixture).startWiFiSettings();
        // execute
        boolean actual = fixture.setEnabled(true);
        // validate
        assertTrue(actual);
        verify(fixture).startWiFiSettings();
        verify(fixture).isMinVersionQ();
    }

}
