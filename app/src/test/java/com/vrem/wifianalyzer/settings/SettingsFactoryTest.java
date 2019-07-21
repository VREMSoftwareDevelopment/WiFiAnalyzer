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

import com.vrem.util.BuildUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildUtils.class)
public class SettingsFactoryTest {

    @Mock
    private Repository repository;

    @Before
    public void setUp() {
        mockStatic(BuildUtils.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(BuildUtils.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testMakeSettings() {
        // setup
        when(BuildUtils.isVersionP()).thenReturn(false);
        when(BuildUtils.isMinVersionQ()).thenReturn(false);
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(Settings.class.getName(), actual.getClass().getName());
        verifyStatic(BuildUtils.class);
        BuildUtils.isVersionP();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testMakeSettingsAndroidQ() {
        // setup
        when(BuildUtils.isVersionP()).thenReturn(false);
        when(BuildUtils.isMinVersionQ()).thenReturn(true);
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(SettingsAndroidQ.class.getName(), actual.getClass().getName());
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testMakeSettingsAndroidP() {
        // setup
        when(BuildUtils.isVersionP()).thenReturn(true);
        when(BuildUtils.isMinVersionQ()).thenReturn(false);
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(SettingsAndroidP.class.getName(), actual.getClass().getName());
        verifyStatic(BuildUtils.class);
        BuildUtils.isVersionP();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

}