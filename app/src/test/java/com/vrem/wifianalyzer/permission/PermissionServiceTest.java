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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    private Activity activity;
    @Mock
    private SystemPermission systemPermission;
    @Mock
    private ApplicationPermission applicationPermission;

    private PermissionService fixture;

    @Before
    public void setUp() {
        fixture = new PermissionService(activity);
        fixture.setApplicationPermission(applicationPermission);
        fixture.setSystemPermission(systemPermission);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(activity);
        verifyNoMoreInteractions(applicationPermission);
        verifyNoMoreInteractions(systemPermission);
    }

    @Test
    public void testIsEnabled() {
        // setup
        when(systemPermission.isEnabled()).thenReturn(true);
        when(applicationPermission.isGranted()).thenReturn(true);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verify(systemPermission).isEnabled();
        verify(applicationPermission).isGranted();
    }

    @Test
    public void testIsEnabledWhenSystemPermissionIsNotEnabled() {
        // setup
        when(systemPermission.isEnabled()).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(systemPermission).isEnabled();
    }

    @Test
    public void testIsEnabledWhenApplicationPermissionAreNotGranted() {
        // setup
        when(systemPermission.isEnabled()).thenReturn(true);
        when(applicationPermission.isGranted()).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(systemPermission).isEnabled();
        verify(applicationPermission).isGranted();
    }

    @Test
    public void testIsSystemEnabled() {
        // setup
        when(systemPermission.isEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.isSystemEnabled();
        // validate
        assertTrue(actual);
        verify(systemPermission).isEnabled();
    }

    @Test
    public void testIsPermissionGranted() {
        // setup
        when(applicationPermission.isGranted()).thenReturn(true);
        // execute
        boolean actual = fixture.isPermissionGranted();
        // validate
        assertTrue(actual);
        verify(applicationPermission).isGranted();
    }

    @Test
    public void testPermissionCheck() {
        // execute
        fixture.check();
        // validate
        verify(applicationPermission).check();
    }

    @Test
    public void testIsGranted() {
        // setup
        int requestCode = 111;
        int[] results = new int[]{1, 2, 3};
        when(applicationPermission.isGranted(requestCode, results)).thenReturn(true);
        // execute
        boolean actual = fixture.isGranted(requestCode, results);
        // validate
        assertTrue(actual);
        verify(applicationPermission).isGranted(requestCode, results);
    }

}