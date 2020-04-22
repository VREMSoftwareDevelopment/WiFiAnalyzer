/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(AndroidJUnit4.class)
public class SettingsFactoryTest {
    @Mock
    private Repository repository;

    @Before
    public void setUp() {
        repository = mock(Repository.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testMakeSettings() {
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(Settings.class.getName(), actual.getClass().getName());
    }

/*
    FIXME: Q requires JAVA 9
    @Test
    @Config(sdk = Build.VERSION_CODES.Q)
    public void testMakeSettingsAndroidQ() {
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(SettingsAndroidQ.class.getName(), actual.getClass().getName());
    }
*/

    @Test
    @Config(sdk = Build.VERSION_CODES.P)
    public void testMakeSettingsAndroidP() {
        // execute
        Settings actual = SettingsFactory.make(repository);
        // validate
        assertEquals(SettingsAndroidP.class.getName(), actual.getClass().getName());
    }

}