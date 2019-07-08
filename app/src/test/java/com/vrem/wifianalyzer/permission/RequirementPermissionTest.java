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

import com.vrem.util.BuildUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildUtils.class)
public class RequirementPermissionTest {

    @Mock
    private Activity activity;
    @Mock
    private SystemPermission systemPermission;
    @Mock
    private ApplicationPermission applicationPermission;

    private RequirementPermission fixture;

    @Before
    public void setUp() {
        mockStatic(BuildUtils.class);
        fixture = new RequirementPermission(activity);
        fixture.setApplicationPermission(applicationPermission);
        fixture.setSystemPermission(systemPermission);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(activity);
        verifyNoMoreInteractions(applicationPermission);
        verifyNoMoreInteractions(systemPermission);
        verifyNoMoreInteractions(BuildUtils.class);
    }

    @Test
    public void testIsEnabledWithNoPermissionRequired() {
        // setup
        when(BuildUtils.isMinVersionM()).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionM();
    }

    @Test
    public void testIsEnabled() {
        // setup
        when(BuildUtils.isMinVersionM()).thenReturn(true);
        when(systemPermission.isEnabled()).thenReturn(true);
        when(applicationPermission.isGranted()).thenReturn(true);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertTrue(actual);
        verify(systemPermission).isEnabled();
        verify(applicationPermission).isGranted();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionM();
    }

    @Test
    public void testIsEnabledWhenLocationCheckerIsNotEnabled() {
        // setup
        when(BuildUtils.isMinVersionM()).thenReturn(true);
        when(systemPermission.isEnabled()).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(systemPermission).isEnabled();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionM();
    }

    @Test

    public void testIsEnabledWhenPermissionCheckerIsNotEnabled() {
        // setup
        when(BuildUtils.isMinVersionM()).thenReturn(true);
        when(systemPermission.isEnabled()).thenReturn(true);
        when(applicationPermission.isGranted()).thenReturn(false);
        // execute
        boolean actual = fixture.isEnabled();
        // validate
        assertFalse(actual);
        verify(systemPermission).isEnabled();
        verify(applicationPermission).isGranted();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionM();
    }
}