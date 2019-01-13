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

package com.vrem.wifianalyzer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PermissionCheckerTest {

    private Activity activity;
    private PermissionChecker fixture;

    @Before
    public void setUp() {
        activity = mock(Activity.class);
        fixture = new PermissionChecker(activity);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(activity);
    }

    @Test
    public void testCheckWithCoarseLocationGranted() {
        // setup
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(PackageManager.PERMISSION_GRANTED);
        // execute
        fixture.check();
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        verify(activity, never()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        verify(activity, never()).isFinishing();
        verify(activity, never()).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionChecker.REQUEST_CODE);
    }

    @Test
    public void testCheckWithFineLocationGranted() {
        // setup
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_GRANTED);
        // execute
        fixture.check();
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        verify(activity, never()).isFinishing();
        verify(activity, never()).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionChecker.REQUEST_CODE);
    }

    @Test
    public void testCheckWithActivityFinish() {
        // setup
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(activity.isFinishing()).thenReturn(true);
        // execute
        fixture.check();
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        verify(activity).isFinishing();
        verify(activity, never()).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionChecker.REQUEST_CODE);
    }

    @Test
    public void testCheckWithRequestPermissions() {
        // setup
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(activity.isFinishing()).thenReturn(false);
        // execute
        fixture.check();
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        verify(activity).isFinishing();
        verify(activity).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionChecker.REQUEST_CODE);
    }

    @Test
    public void testIsGranted() {
        // setup
        int[] grantResults = new int[]{PackageManager.PERMISSION_GRANTED};
        // execute
        boolean actual = fixture.isGranted(PermissionChecker.REQUEST_CODE, grantResults);
        // validate
        assertTrue(actual);
    }

    @Test
    public void testIsGrantedWithOtherRequestCode() {
        // setup
        int[] grantResults = new int[]{PackageManager.PERMISSION_GRANTED};
        // execute
        boolean actual = fixture.isGranted(-PermissionChecker.REQUEST_CODE, grantResults);
        // validate
        assertFalse(actual);
    }

    @Test
    public void testIsGrantedWithNoResults() {
        // setup
        int[] grantResults = new int[]{};
        // execute
        boolean actual = fixture.isGranted(PermissionChecker.REQUEST_CODE, grantResults);
        // validate
        assertFalse(actual);
    }

    @Test
    public void testIsGrantedWithNoPermissioGranted() {
        // setup
        int[] grantResults = new int[]{PackageManager.PERMISSION_DENIED};
        // execute
        boolean actual = fixture.isGranted(PermissionChecker.REQUEST_CODE, grantResults);
        // validate
        assertFalse(actual);
    }

}