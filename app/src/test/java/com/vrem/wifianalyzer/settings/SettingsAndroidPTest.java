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

package com.vrem.wifianalyzer.settings;

import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsAndroidPTest {

    @Mock
    private Repository repository;

    private SettingsAndroidP fixture;

    @Before
    public void setUp() {
        fixture = new SettingsAndroidP(repository);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testIsWiFiThrottleDisabled() {
        // setup
        when(repository.getResourceBoolean(R.bool.wifi_throttle_disabled_default)).thenReturn(true);
        when(repository.getBoolean(R.string.wifi_throttle_disabled_key, true)).thenReturn(true);
        // execute
        boolean actual = fixture.isWiFiThrottleDisabled();
        // validate
        assertTrue(actual);
        verify(repository).getBoolean(R.string.wifi_throttle_disabled_key, true);
        verify(repository).getResourceBoolean(R.bool.wifi_throttle_disabled_default);
    }

    @Test
    public void testGetScanSpeedWithWiFiThrottleDisabled() {
        // setup
        fixture = withWiFiThrottle(true);
        int defaultValue = Settings.SCAN_SPEED_DEFAULT - 2;
        int speedValue = Settings.SCAN_SPEED_DEFAULT - 1;
        when(repository.getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)).thenReturn(defaultValue);
        when(repository.getStringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue);
        // execute
        int actual = fixture.getScanSpeed();
        // validate
        assertEquals(Settings.SCAN_SPEED_DEFAULT, actual);
        verify(repository).getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT);
        verify(repository).getStringAsInteger(R.string.scan_speed_key, defaultValue);
    }

    @Test
    public void testGetScanSpeedWithWiFiThrottleEnabled() {
        // setup
        fixture = withWiFiThrottle(false);
        int defaultValue = Settings.SCAN_SPEED_DEFAULT - 2;
        int speedValue = Settings.SCAN_SPEED_DEFAULT - 1;
        when(repository.getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)).thenReturn(defaultValue);
        when(repository.getStringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue);
        // execute
        int actual = fixture.getScanSpeed();
        // validate
        assertEquals(speedValue, actual);
        verify(repository).getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT);
        verify(repository).getStringAsInteger(R.string.scan_speed_key, defaultValue);
    }

    private SettingsAndroidP withWiFiThrottle(boolean isWiFiThrottleDisabled) {
        return new SettingsAndroidP(repository) {
            @Override
            public boolean isWiFiThrottleDisabled() {
                return isWiFiThrottleDisabled;
            }
        };
    }
}