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

package com.vrem.wifianalyzer.permission;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class SystemPermissionTest {

    private Activity activity;
    private LocationManager locationManager;

    private SystemPermission fixture;

    @Before
    public void setUp() {
        activity = mock(Activity.class);
        locationManager = mock(LocationManager.class);
        fixture = new SystemPermission(activity);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(activity);
        verifyNoMoreInteractions(locationManager);
    }

    @Test
    public void testIsEnabledWhenGPSProviderIsEnabled() {
        // setup
        when(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);
        when(locationManager.isLocationEnabled()).thenReturn(false);
        when(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false);
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verify(activity).getSystemService(Context.LOCATION_SERVICE);
        verify(locationManager).isLocationEnabled();
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Test
    public void testIsEnabledWhenLocationIsEnabled() {
        // setup
        when(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);
        when(locationManager.isLocationEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verify(activity).getSystemService(Context.LOCATION_SERVICE);
        verify(locationManager).isLocationEnabled();
    }

    @Test
    public void testIsEnabledWhenNetworkProviderIsEnabled() {
        // setup
        when(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);
        when(locationManager.isLocationEnabled()).thenReturn(false);
        when(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verify(activity).getSystemService(Context.LOCATION_SERVICE);
        verify(locationManager).isLocationEnabled();
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Test
    public void testIsEnabledWhenAllProvidersAreDisabled() {
        // setup
        when(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);
        when(locationManager.isLocationEnabled()).thenReturn(false);
        when(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false);
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(activity).getSystemService(Context.LOCATION_SERVICE);
        verify(locationManager).isLocationEnabled();
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Test
    public void testIsEnabled() {
        // setup
        when(activity.getSystemService(Context.LOCATION_SERVICE)).thenThrow(RuntimeException.class);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(activity).getSystemService(Context.LOCATION_SERVICE);
    }

}
